package org.example

import akka.actor._
import akka.kernel.Bootable
import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory
import akka.event.Logging

//#actor
class SimpleCalculatorActor extends Actor {
  def receive = {
    case (x: Int, y: Int) =>
      println("Add %d and %d !".format(x, y))
      sender ! x + y
  }
}
//#actor

class CalculatorApplication extends Bootable {

  //#setup
  val system = ActorSystem("CalculatorApplication", ConfigFactory.load.getConfig("calculator"))
  val actor = system.actorOf(Props[SimpleCalculatorActor], "calculator")
  //#setup

  def startup() {
  }

  def shutdown() {
    system.shutdown()
  }
}

object AkkaCalculator extends App {
  new CalculatorApplication
  println("Started Calculator Application - waiting for messages")
}
