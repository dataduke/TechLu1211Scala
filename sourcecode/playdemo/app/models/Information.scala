package models

import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.Logger
import play.api.cache.Cache

object Information {

  val cacheKey = "messageOfTheDay"

  def getMessage: String = {
    Cache.getOrElse(cacheKey) {
      DB.withTransaction {
        implicit conn => {
          Logger.info("Get data from database!")
          SQL("select message from information where id = (select max(id) from information)")
            .as(scalar[String].single)
        }
      }
    }
  }

}
