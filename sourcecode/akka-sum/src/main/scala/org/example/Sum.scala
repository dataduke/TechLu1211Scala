package org.example

import akka.actor._
import akka.routing.RoundRobinRouter
import akka.util.Duration
import akka.util.duration._

object Sum extends App {

  calculate(nrOfWorkers = 4, nrOfElements = 100, nrOfMessages = 100)

  sealed trait SumMessage
  case object Calculate extends SumMessage
  case class Work(start: Int, nrOfElements: Int) extends SumMessage
  case class Result(value: Long) extends SumMessage
  case class Sum(sum: Long, duration: Duration)

  class Worker extends Actor {

    def calculate(start: Int, nrOfElements: Int): Long = {
      var acc = 0L
      for (i <- start until (start + nrOfElements))
        acc += i
      acc
    }

    def receive = {
      case Work(start, nrOfElements) =>
        sender ! Result(calculate(start, nrOfElements)) // perform the work
    }
  }

  class Master(nrOfWorkers: Int, 
    nrOfMessages: Int, 
    nrOfElements: Int, 
    listener: ActorRef) extends Actor {

    var sum: Long = _
    var nrOfResults: Int = _
    val start: Long = System.currentTimeMillis

    val workerRouter = context.actorOf(
      Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")

    def receive = {
      case Calculate =>
        for (i <- 0 until nrOfMessages) 
          workerRouter ! Work(i * nrOfElements, nrOfElements)
      case Result(value) =>        
        sum += value
        nrOfResults += 1
        if (nrOfResults == nrOfMessages) {
          listener ! Sum(sum, duration = (System.currentTimeMillis - start).millis)
          context.stop(self)
        }
    }
  }

  class Listener extends Actor {
    def receive = {
      case Sum(sum, duration) =>
        println("\n\tSum: \t\t\t\t%s\n\tCalculation time: \t%s".format(sum, duration))
        context.system.shutdown()
    }
  }

  def calculate(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) {
    // Create an Akka system
    val system = ActorSystem("SumSystem")

    // create the result listener, which will print the result and 
    // shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")

    // create the master
    val master = system.actorOf(Props(new Master(
      nrOfWorkers, nrOfMessages, nrOfElements, listener)),
      name = "master")

    // start the calculation
    master ! Calculate

  }
}