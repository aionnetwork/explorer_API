
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class NavigateAnalytics extends Simulation {

	val httpProtocol = http
		.baseUrl("https://explorer-staging-api.aion.network")
		.inferHtmlResources(WhiteList(""".*(explorer-staging-api).*"""), BlackList())
		.acceptHeader("application/json, text/plain, */*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:65.0) Gecko/20100101 Firefox/65.0")

	val headers_0 = Map(
		"Origin" -> "https://explorer-staging.aion.network",
		"Pragma" -> "no-cache",
		"TE" -> "Trailers")



	val scn = scenario("NavigateAnalytics")
		.exec(http("request_0")
			.get("/aion/dashboard/view")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_1")
			.get("/aion/dashboard/view")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/aion/dashboard/getGraphingInfo?type=0")
			.headers(headers_0)))
		.pause(4)
		.exec(http("request_3")
			.get("/aion/dashboard/getGraphingInfo?type=5")
			.headers(headers_0))
		.pause(3)
		.exec(http("request_4")
			.get("/aion/dashboard/view")
			.headers(headers_0)
			.resources(http("request_5")
			.get("/aion/dashboard/view")
			.headers(headers_0),
            http("request_6")
			.get("/aion/dashboard/getGraphingInfo?type=3")
			.headers(headers_0)))
		.pause(7)
		.exec(http("request_7")
			.get("/aion/dashboard/view")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_8")
			.get("/aion/dashboard/view")
			.headers(headers_0))
		.pause(3)
		.exec(http("request_9")
			.get("/aion/dashboard/getGraphingInfo?type=2")
			.headers(headers_0))
		.pause(4)
		.exec(http("request_10")
			.get("/aion/dashboard/view")
			.headers(headers_0)
			.resources(http("request_11")
			.get("/aion/dashboard/view")
			.headers(headers_0),
            http("request_12")
			.get("/aion/dashboard/getGraphingInfo?type=1")
			.headers(headers_0)))
		.pause(2)
		.exec(http("request_13")
			.get("/aion/dashboard/getGraphingInfo?type=4")
			.headers(headers_0))
		.pause(5)
		.exec(http("request_14")
			.get("/aion/dashboard/view")
			.headers(headers_0)
			.resources(http("request_15")
			.get("/aion/dashboard/view")
			.headers(headers_0)))

	setUp(scn.inject(atOnceUsers(5000))).protocols(httpProtocol)
}
