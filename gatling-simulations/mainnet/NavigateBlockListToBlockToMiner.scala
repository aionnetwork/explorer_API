
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class NavigateBlockListToBlockToMiner extends Simulation {

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



	val scn = scenario("NavigateBlockListToBlockToMiner")
		.exec(http("request_0")
			.get("/aion/dashboard/view")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/aion/dashboard/getBlockList?page=0&size=25")
			.headers(headers_0),
            http("request_2")
			.get("/aion/dashboard/getBlockList?page=0&size=25")
			.headers(headers_0),
            http("request_3")
			.get("/aion/dashboard/view")
			.headers(headers_0)))
		.pause(1)
		.exec(http("request_4")
			.get("/aion/dashboard/getBlockList?page=1&size=25&timestampStart=1548964955&timestampEnd=1549569755")
			.headers(headers_0))
		.pause(6)
		.exec(http("request_5")
			.get("/aion/dashboard/getBlockList?page=2&size=25&timestampStart=1548964955&timestampEnd=1549569755")
			.headers(headers_0)
			.resources(http("request_6")
			.get("/aion/dashboard/view")
			.headers(headers_0),
            http("request_7")
			.get("/aion/dashboard/getBlockList?page=3&size=25&timestampStart=1548964955&timestampEnd=1549569755")
			.headers(headers_0),
            http("request_8")
			.get("/aion/dashboard/getBlockList?page=4&size=25&timestampStart=1548964955&timestampEnd=1549569755")
			.headers(headers_0),
            http("request_9")
			.get("/aion/dashboard/view")
			.headers(headers_0),
            http("request_10")
			.get("/aion/dashboard/getBlockList?page=5&size=25&timestampStart=1548964955&timestampEnd=1549569755")
			.headers(headers_0),
            http("request_11")
			.get("/aion/dashboard/getBlockList?page=6&size=25&timestampStart=1548964955&timestampEnd=1549569755")
			.headers(headers_0),
            http("request_12")
			.get("/aion/dashboard/getBlockList?page=7&size=25&timestampStart=1548964955&timestampEnd=1549569755")
			.headers(headers_0),
            http("request_13")
			.get("/aion/dashboard/getBlockList?page=8&size=25&timestampStart=1548964955&timestampEnd=1549569755")
			.headers(headers_0),
            http("request_14")
			.get("/aion/dashboard/getBlockList?page=2289&size=25&timestampStart=1548964955&timestampEnd=1549569755")
			.headers(headers_0)))
		.pause(2)
		.exec(http("request_15")
			.get("/aion/dashboard/getBlockList?page=0&size=25&timestampStart=1548964955&timestampEnd=1549569755")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_16")
			.get("/aion/dashboard/getBlockAndTransactionDetailsFromBlockNumberOrBlockHash?searchParam=2378143&undefined=25")
			.headers(headers_0)
			.resources(http("request_17")
			.get("/aion/dashboard/view")
			.headers(headers_0),
            http("request_18")
			.get("/aion/dashboard/view")
			.headers(headers_0)))
		.pause(3)
		.exec(http("request_19")
			.get("/aion/dashboard/getAccountDetails?accountAddress=a00983f07c11ee9160a64dd3ba3dc3d1f88332a2869f25725f56cbd0be32ef7a")
			.headers(headers_0)
			.resources(http("request_20")
			.get("/aion/dashboard/getTransactionsByAddress?accountAddress=a00983f07c11ee9160a64dd3ba3dc3d1f88332a2869f25725f56cbd0be32ef7a&page=0&size=25&timestampStart=1546927380")
			.headers(headers_0),
            http("request_21")
			.get("/aion/dashboard/getBlocksMinedByAddress?searchParam=a00983f07c11ee9160a64dd3ba3dc3d1f88332a2869f25725f56cbd0be32ef7a&page=0&size=25")
			.headers(headers_0)))
		.pause(3)
		.exec(http("request_22")
			.get("/aion/dashboard/view")
			.headers(headers_0)
			.resources(http("request_23")
			.get("/aion/dashboard/view")
			.headers(headers_0)))

	setUp(scn.inject(atOnceUsers(5000))).protocols(httpProtocol)
}
