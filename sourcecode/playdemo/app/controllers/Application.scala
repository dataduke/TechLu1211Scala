package controllers

import play.api.mvc._
import models.Person
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.db.DB
import play.api.Play.current
import play.api.i18n.Lang

object Application extends Controller {

  /*
  val personForm = Form(mapping(
      "givenname" -> text,
      "surname" -> text,
      "age" -> number
    )(Person.apply)(Person.unapply)
  )
  */

  val defaultLength = nonEmptyText(minLength = 4, maxLength = 10)

  val personForm = Form(mapping(
    "id" -> longNumber,
    "givenname" -> defaultLength,
    "surname" -> text.verifying(minLength(4)).verifying("error", name => !name.equals("Falsch")),
    "age" -> number(min = 0, max = 120)
  )(Person.apply)(Person.unapply).verifying(
    "person.error", person => person.givenname != person.surname
  ))

  override implicit def lang(implicit request : RequestHeader): Lang = {
    val count = request.session.get("counter").getOrElse("0").toInt
    if (count % 2 == 0) { Lang("en") }
      else { Lang("de") }
  }

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def list = Action {
    DB.withTransaction {
      implicit conn => {
        val list = Person.findAll()
        Ok(views.html.list(list))
      }
    }
  }

  def add = Action {
    implicit request => {
      val count = request.session.get("counter").getOrElse("0").toInt
      Ok(views.html.add(personForm)).withSession(session +("counter",(count + 1).toString))
    }
  }

  def submit = Action {
    implicit request => {
      personForm.bindFromRequest.fold(
        error => {
          Ok(views.html.add(error))
        },
        success => {
          // Speichern in DB
          DB.withTransaction {
            implicit conn => {
              Person.insert(success)
            }
          }
          Redirect(routes.Application.list())
        }
      )
    }
  }

}