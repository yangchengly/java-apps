package com.avaya.welljoint.importexceltodb.pachira;

public class DataAttached {
	private String id;
	private String mobile;
	private String callingDate;
	private String startTime;
	private String endTime;
	private String duration;
	private String filePath;
	private String callingType;
	private String businessId;
	private String custNo;
	private String comment;
	private String agentNo;
	private String gender;
	private String channelType;
	private String ensureNo;
	private String businessType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCallingDate() {
		return callingDate;
	}

	public void setCallingDate(String callingDate) {
		this.callingDate = callingDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getCallingType() {
		return callingType;
	}

	public void setCallingType(String callingType) {
		this.callingType = callingType;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getEnsureNo() {
		return ensureNo;
	}

	public void setEnsureNo(String ensureNo) {
		this.ensureNo = ensureNo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	@Override
	public String toString() {
		return "DataAttached [id=" + id + ", mobile=" + mobile + ", callingDate=" + callingDate + ", startTime="
				+ startTime + ", endTime=" + endTime + ", duration=" + duration + ", filePath=" + filePath
				+ ", callingType=" + callingType + ", businessId=" + businessId + ", custNo=" + custNo + ", comment="
				+ comment + ", agentNo=" + agentNo + ", gender=" + gender + ", channelType=" + channelType
				+ ", ensureNo=" + ensureNo + ", businessType=" + businessType + "]";
	}
	
	
}
