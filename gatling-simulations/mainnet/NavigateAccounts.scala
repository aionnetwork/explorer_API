
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class NavigateAccounts extends Simulation {

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



	val scn = scenario("NavigateAccounts")
		.exec(http("request_0")
			.get("/aion/dashboard/view")
			.headers(headers_0))
		.pause(5)
		.exec(http("request_1")
			.get("/aion/dashboard/getDailyAccountStatistics")
			.headers(headers_0))
		.pause(2)
		.exec(http("request_2")
			.get("/aion/dashboard/view")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_3")
			.get("/aion/dashboard/view")
			.headers(headers_0))
		.pause(8)
		.exec(http("request_4")
			.get("/aion/dashboard/view")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_5")
			.get("/aion/dashboard/view")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_6")
			.get("/aion/dashboard/getAccountDetails?accountAddress=a0103ec1927883f7b5407a1ef14c829aaf08dc980cb0ce66dd5055d03c257a4b")
			.headers(headers_0)
			.resources(http("request_7")
			.get("/aion/dashboard/getBlocksMinedByAddress?searchParam=a0103ec1927883f7b5407a1ef14c829aaf08dc980cb0ce66dd5055d03c257a4b&page=0&size=25")
			.headers(headers_0),
            http("request_8")
			.get("/aion/dashboard/getTransactionsByAddress?accountAddress=a0103ec1927883f7b5407a1ef14c829aaf08dc980cb0ce66dd5055d03c257a4b&page=0&size=25&timestampStart=1546927558")
			.headers(headers_0)))
		.pause(2)
		.exec(http("request_9")
			.get("/aion/dashboard/getDailyAccountStatistics")
			.headers(headers_0))
		.pause(2)
		.exec(http("request_10")
			.get("/aion/dashboard/getAccountDetails?accountAddress=a05801fb2de5b76568a9c13f86766ebe3e6c438272e9fc6f5fabfcbea93048bf")
			.headers(headers_0)
			.resources(http("request_11")
			.get("/aion/dashboard/getTransactionsByAddress?accountAddress=a05801fb2de5b76568a9c13f86766ebe3e6c438272e9fc6f5fabfcbea93048bf&page=0&size=25&timestampStart=1546927564")
			.headers(headers_0),
            http("request_12")
			.get("/aion/dashboard/getBlocksMinedByAddress?searchParam=a05801fb2de5b76568a9c13f86766ebe3e6c438272e9fc6f5fabfcbea93048bf&page=0&size=25")
			.headers(headers_0),
            http("request_13")
			.get("/aion/dashboard/view")
			.headers(headers_0)))
		.pause(1)
		.exec(http("request_14")
			.get("/aion/dashboard/view")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_15")
			.get("/aion/dashboard/getDailyAccountStatistics")
			.headers(headers_0))
		.pause(3)
		.exec(http("request_16")
			.get("/aion/dashboard/getAccountDetails?accountAddress=a05213b7ac74b4acc59adec867f079790c88c9f335bbb65d6840ec5254fa43e1")
			.headers(headers_0)
			.resources(http("request_17")
			.get("/aion/dashboard/getBlocksMinedByAddress?searchParam=a05213b7ac74b4acc59adec867f079790c88c9f335bbb65d6840ec5254fa43e1&page=0&size=25")
			.headers(headers_0),
            http("request_18")
			.get("/aion/dashboard/getTransactionsByAddress?accountAddress=a05213b7ac74b4acc59adec867f079790c88c9f335bbb65d6840ec5254fa43e1&page=0&size=25&timestampStart=1546927571")
			.headers(headers_0)))
		.pause(1)
		.exec(http("request_19")
			.get("/aion/dashboard/getTransactionDetailsByTransactionHash?searchParam=3d02a97bf22852c2f1a1d2da52220513a9e9ec68d6f62fa276fd66b8c3d0ce37")
			.headers(headers_0)
			.resources(http("request_20")
			.get("/aion/dashboard/getInternalTransfersByTransactionHash?transactionHash=3d02a97bf22852c2f1a1d2da52220513a9e9ec68d6f62fa276fd66b8c3d0ce37&page=0&size=25")
			.headers(headers_0)))
		.pause(1)
		.exec(http("request_21")
			.get("/aion/dashboard/view")
			.headers(headers_0))

	setUp(scn.inject(atOnceUsers(5000))).protocols(httpProtocol)
}
