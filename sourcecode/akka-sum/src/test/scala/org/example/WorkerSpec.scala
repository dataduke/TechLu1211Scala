package org.example

import org.junit.runner.RunWith
import org.scalatest.matchers.MustMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpec
import akka.testkit.TestActorRef
import akka.actor.ActorSystem

@org.junit.runner.RunWith(classOf[org.scalatest.junit.JUnitRunner])
class WorkerSpec extends WordSpec with MustMatchers with BeforeAndAfterAll {

  implicit val system = ActorSystem()

  override def afterAll {
    system.shutdown()
  }

  "Worker" must {
    "calculate pi correctly" in {
      val testActor = TestActorRef[org.example.Sum.Worker]
      val actor = testActor.underlyingActor
      actor.calculate(0, 0) must equal(0)
      actor.calculate(1, 4) must equal(10)
    }
  }
}
