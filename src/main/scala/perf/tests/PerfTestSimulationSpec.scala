package perf.tests

import io.gatling.app.Gatling
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.time.Duration
import scala.compat.java8.DurationConverters.DurationOps
import scala.util.Random

object PerfTestSimulationSpec {
  def main(args: Array[String]): Unit = {
    val args1 = args :+ "-s" :+ "perf.tests.PerfTestSimulationSpec"
    Gatling.main(args1)
  }
}

class PerfTestSimulationSpec extends Simulation {

  val randomNumber = new Random()

  val mockScenario = scenario("Mock Load Scenario")
//    .during(Duration.ofMinutes(2).toScala)( // Invalid to do - does not make much sense since it goes on firing requests in a loop indefinitely and does not simulate user behaviour
    .exec(
      exec(session => session.set("randomNumber", randomNumber.nextInt(2)))
        .doIfOrElse(session => session("randomNumber").as[Int] == 1)
        (exec(http("Hello Request1")
          .get("/")
          .check(status.is(400)) // Will lead to Knock Outs
//          .check(status.is(200))
        ).exec(session => {
//          println(session("randomNumber"))
          session
        }))
        (exec(http("Hello Request2")
          .get("/")
          .check(status.is(200))
        ).exec(session => {
//          println(session("randomNumber"))
          session
        })
        )
    )

  setUp(mockScenario
    .inject(
      //      nothingFor(10.seconds),
      rampUsers(30).during(Duration.ofSeconds(60).toScala)
      //      nothingFor(20.seconds),
      //      atOnceUsers(10)
    ).protocols(http.baseUrl("http://localhost:9000"))
  )

}
