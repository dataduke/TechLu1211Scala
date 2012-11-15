package controllers

import play.api.mvc.{Action, Controller}
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.duration._
import com.typesafe.config.ConfigFactory
import akka.dispatch.Await
import akka.util.Timeout

object AkkaCalc extends Controller {

  implicit val timeout = Timeout(10 seconds)

  def add(x: Int, y: Int) = Action {

    val localsystem = ActorSystem("Demo", ConfigFactory.load.getConfig("demo"))

    val actor = localsystem.actorFor("akka://CalculatorApplication@127.0.0.1:9123/user/calculator")

    val result = Await.result((actor ? (x, y)).mapTo[Int], 10 seconds)

    localsystem.shutdown()

    Ok("Ergebnis: " + result)

  }

}


