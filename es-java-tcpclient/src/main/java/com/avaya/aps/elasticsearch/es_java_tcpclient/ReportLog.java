package com.avaya.aps.elasticsearch.es_java_tcpclient;

import java.util.Date;

public class ReportLog {
	private Date date;
	private String logLevel;
	private String status;
	private String module;
	private String name;
	private String type;
	private String node;
	private String info;
	private String message;
	private String sessionID;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	@Override
	public String toString() {
		return "ReportLog [date=" + date + ", logLevel=" + logLevel
				+ ", status=" + status + ", module=" + module + ", name="
				+ name + ", type=" + type + ", node=" + node + ", info=" + info
				+ ", message=" + message + ", sessionID=" + sessionID + "]";
	}

}
