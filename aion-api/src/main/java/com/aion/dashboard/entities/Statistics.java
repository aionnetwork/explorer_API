package com.aion.dashboard.entities;

import org.json.JSONArray;
import org.json.JSONObject;

public class Statistics {

	private JSONArray minedBlks;
	private JSONArray inboundTxns;
	private JSONArray outboundTxns;

	private JSONArray blocks;
	private JSONArray transactions;
	private JSONObject sbMetrics;
	private JSONObject rtMetrics;


	private  final String MESSAGE="message";
	private final String  INVALID_REQUEST="Error: Invalid Request";
	private final String CONTENT="content";


	private Statistics() {
		blocks = new JSONArray();
		sbMetrics = new JSONObject();
		rtMetrics = new JSONObject();
		transactions = new JSONArray();

		minedBlks = new JSONArray();
		inboundTxns = new JSONArray();
		outboundTxns = new JSONArray();
	}

	private static class StatisticsHolder {
		static final Statistics INSTANCE = new Statistics();
	}
	public static Statistics getInstance() {
		return StatisticsHolder.INSTANCE;
	}

	public JSONArray getMinedBlks() {
		return minedBlks;
	}
	public JSONArray getInboundTxns() {
		return inboundTxns;
	}
	public JSONArray getOutboundTxns() {
		return outboundTxns;
	}

	public JSONArray getBlocks()       {
		return blocks;
	}
	public JSONArray getTransactions() {
		return transactions;
	}
	public JSONObject getSBMetrics() {
		return sbMetrics;
	}
	public JSONObject getRTMetrics() {
		return rtMetrics;
	}

	public String getDashboardJSON() {
		try {
			Statistics statistics = Statistics.getInstance();
			JSONObject result = new JSONObject();
			result.put("blocks", statistics.getBlocks());
			result.put("metrics", statistics.getSBMetrics());
			result.put("transactions", statistics.getTransactions());

			JSONArray jsonArray = new JSONArray();
			jsonArray.put(result);

			return new JSONObject().put(CONTENT, jsonArray).toString();

		} catch(Exception e) {
			e.printStackTrace();
			return getJsonString(MESSAGE,INVALID_REQUEST);

		}
	}

	public String getDailyAccountStatistics() {
		try {
			Statistics statistics = Statistics.getInstance();
			JSONObject result = new JSONObject();
			result.put("miners", new JSONObject().put(CONTENT, statistics.getMinedBlks()));
			result.put("txnInbound", new JSONObject().put(CONTENT, statistics.getInboundTxns()));
			result.put("txnOutbound", new JSONObject().put(CONTENT, statistics.getOutboundTxns()));
			return result.toString();
		} catch(Exception e) {
			e.printStackTrace();
			return getJsonString(MESSAGE,INVALID_REQUEST);

		}
	}

	public void setMinedBlks(JSONArray minedBlks) {
		this.minedBlks = minedBlks;
	}
	public void setInboundTxns(JSONArray inboundTxns) {
		this.inboundTxns = inboundTxns;
	}
	public void setOutboundTxns(JSONArray outboundTxns) {
		this.outboundTxns = outboundTxns;
	}

	public void setBlocks(JSONArray blocks) {
		this.blocks = blocks;
	}
	public void setTransactions(JSONArray transactions) {
		this.transactions = transactions;
	}
	public void setSBMetrics(JSONObject sbMetrics) {
		this.sbMetrics = sbMetrics;
	}
	public void setRTMetrics(JSONObject rtMetrics) {
		this.rtMetrics = rtMetrics;
	}

	private String getJsonString(String key,String message) {
		JSONObject result = new JSONObject();
		result.put(key, message);
		return result.toString();
	}
}
