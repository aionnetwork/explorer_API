
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class NavigateTransactions extends Simulation {

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



	val scn = scenario("NavigateTransactions")
		.exec(http("request_0")
			.get("/aion/dashboard/view")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/aion/dashboard/getTransactionList?page=0&size=25")
			.headers(headers_0),
            http("request_2")
			.get("/aion/dashboard/getTransactionList?page=0&size=25")
			.headers(headers_0),
            http("request_3")
			.get("/aion/dashboard/view")
			.headers(headers_0)))
		.pause(2)
		.exec(http("request_4")
			.get("/aion/dashboard/getTransactionList?page=1&size=25&timestampStart=1548965046&timestampEnd=1549569846")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_5")
			.get("/aion/dashboard/getTransactionList?page=2&size=25&timestampStart=1548965046&timestampEnd=1549569846")
			.headers(headers_0)
			.resources(http("request_6")
			.get("/aion/dashboard/getTransactionList?page=3&size=25&timestampStart=1548965046&timestampEnd=1549569846")
			.headers(headers_0),
            http("request_7")
			.get("/aion/dashboard/getTransactionList?page=4&size=25&timestampStart=1548965046&timestampEnd=1549569846")
			.headers(headers_0)))
		.pause(1)
		.exec(http("request_8")
			.get("/aion/dashboard/getTransactionList?page=5&size=25&timestampStart=1548965046&timestampEnd=1549569846")
			.headers(headers_0)
			.resources(http("request_9")
			.get("/aion/dashboard/view")
			.headers(headers_0),
            http("request_10")
			.get("/aion/dashboard/getTransactionList?page=6&size=25&timestampStart=1548965046&timestampEnd=1549569846")
			.headers(headers_0),
            http("request_11")
			.get("/aion/dashboard/view")
			.headers(headers_0),
            http("request_12")
			.get("/aion/dashboard/getTransactionList?page=7&size=25&timestampStart=1548965046&timestampEnd=1549569846")
			.headers(headers_0)))
		.pause(1)
		.exec(http("request_13")
			.get("/aion/dashboard/getTransactionList?page=2667&size=25&timestampStart=1548965046&timestampEnd=1549569846")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_14")
			.get("/aion/dashboard/getTransactionList?page=2666&size=25&timestampStart=1548965046&timestampEnd=1549569846")
			.headers(headers_0))
		.pause(4)
		.exec(http("request_15")
			.get("/aion/dashboard/view")
			.headers(headers_0)
			.resources(http("request_16")
			.get("/aion/dashboard/getTransactionDetailsByTransactionHash?searchParam=b92bcbf9c57bb53a0f36ac779e76b6ff243356b3c294a2994f19a4c23f225013")
			.headers(headers_0),
            http("request_17")
			.get("/aion/dashboard/getInternalTransfersByTransactionHash?transactionHash=b92bcbf9c57bb53a0f36ac779e76b6ff243356b3c294a2994f19a4c23f225013&page=0&size=25")
			.headers(headers_0),
            http("request_18")
			.get("/aion/dashboard/view")
			.headers(headers_0)))
		.pause(4)
		.exec(http("request_19")
			.get("/aion/dashboard/getAccountDetails?accountAddress=a062cb08fedc61f879996f2ad337c144cc3f46e491e9e1f9af92b25402679056")
			.headers(headers_0)
			.resources(http("request_20")
			.get("/aion/dashboard/getBlocksMinedByAddress?searchParam=a062cb08fedc61f879996f2ad337c144cc3f46e491e9e1f9af92b25402679056&page=0&size=25")
			.headers(headers_0),
            http("request_21")
			.get("/aion/dashboard/getTransactionsByAddress?accountAddress=a062cb08fedc61f879996f2ad337c144cc3f46e491e9e1f9af92b25402679056&page=0&size=25&timestampStart=1546927471")
			.headers(headers_0)))
		.pause(1)
		.exec(http("request_22")
			.get("/aion/dashboard/getTransactionDetailsByTransactionHash?searchParam=a188147907e5dbc5b226b07ecd1fa25bd5e45678b319a6d7e74030fc478cf1a9")
			.headers(headers_0)
			.resources(http("request_23")
			.get("/aion/dashboard/getInternalTransfersByTransactionHash?transactionHash=a188147907e5dbc5b226b07ecd1fa25bd5e45678b319a6d7e74030fc478cf1a9&page=0&size=25")
			.headers(headers_0),
            http("request_24")
			.get("/aion/dashboard/view")
			.headers(headers_0)))
		.pause(1)
		.exec(http("request_25")
			.get("/aion/dashboard/view")
			.headers(headers_0))
		.pause(6)
		.exec(http("request_26")
			.get("/aion/dashboard/getAccountDetails?accountAddress=a022a68ef27e5febe4570edb2ce5586974cb326f24fce2ebb23012c07dac90e0")
			.headers(headers_0)
			.resources(http("request_27")
			.get("/aion/dashboard/getTransactionsByAddress?accountAddress=a022a68ef27e5febe4570edb2ce5586974cb326f24fce2ebb23012c07dac90e0&page=0&size=25&timestampStart=1546927483")
			.headers(headers_0),
            http("request_28")
			.get("/aion/dashboard/getBlocksMinedByAddress?searchParam=a022a68ef27e5febe4570edb2ce5586974cb326f24fce2ebb23012c07dac90e0&page=0&size=25")
			.headers(headers_0),
            http("request_29")
			.get("/aion/dashboard/view")
			.headers(headers_0)))
		.pause(1)
		.exec(http("request_30")
			.get("/aion/dashboard/view")
			.headers(headers_0)
			.resources(http("request_31")
			.get("/aion/dashboard/getTransactionDetailsByTransactionHash?searchParam=aa5b7f12249920a779ec2d61f099ad95b0e3bec6494467c6b8cad6921a316081")
			.headers(headers_0),
            http("request_32")
			.get("/aion/dashboard/getInternalTransfersByTransactionHash?transactionHash=aa5b7f12249920a779ec2d61f099ad95b0e3bec6494467c6b8cad6921a316081&page=0&size=25")
			.headers(headers_0)))
		.pause(2)
		.exec(http("request_33")
			.get("/aion/dashboard/getAccountDetails?accountAddress=a0d2024dca3c505d1a62b663a084dd7570f6f39a690ae981f1d7cb234a5b4f82")
			.headers(headers_0)
			.resources(http("request_34")
			.get("/aion/dashboard/getTransactionsByAddress?accountAddress=a0d2024dca3c505d1a62b663a084dd7570f6f39a690ae981f1d7cb234a5b4f82&page=0&size=25&timestampStart=1546927490")
			.headers(headers_0),
            http("request_35")
			.get("/aion/dashboard/getBlocksMinedByAddress?searchParam=a0d2024dca3c505d1a62b663a084dd7570f6f39a690ae981f1d7cb234a5b4f82&page=0&size=25")
			.headers(headers_0)))
		.pause(2)
		.exec(http("request_36")
			.get("/aion/dashboard/getTransactionDetailsByTransactionHash?searchParam=d7a4c5778e44ae10779b7e0f84532bab5b17d819d1e46219e7e698562b8bf684")
			.headers(headers_0)
			.resources(http("request_37")
			.get("/aion/dashboard/getInternalTransfersByTransactionHash?transactionHash=d7a4c5778e44ae10779b7e0f84532bab5b17d819d1e46219e7e698562b8bf684&page=0&size=25")
			.headers(headers_0)))
		.pause(2)
		.exec(http("request_38")
			.get("/aion/dashboard/view")
			.headers(headers_0))

	setUp(scn.inject(atOnceUsers(5000))).protocols(httpProtocol)
}
