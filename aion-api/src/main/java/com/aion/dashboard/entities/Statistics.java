package com.aion.dashboard.entities;

import org.json.JSONArray;
import org.json.JSONObject;

public class Statistics {

	private JSONArray blocks;
	private JSONArray transactions;
	private JSONObject sbMetrics;
	private JSONObject rtMetrics;

	private Statistics() {
		blocks = new JSONArray();
		sbMetrics = new JSONObject();
		rtMetrics = new JSONObject();
		transactions = new JSONArray();
	}

	private static class StatisticsHolder {
		static final Statistics INSTANCE = new Statistics();
	}
	public static Statistics getInstance() {
		return StatisticsHolder.INSTANCE;
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

			return new JSONObject().put("content", jsonArray).toString();

		} catch(Exception e) {
			e.printStackTrace();
			JSONObject result = new JSONObject();
			result.put("message", "Error: Invalid Request");
			return result.toString();
		}
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
}
