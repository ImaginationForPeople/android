package org.imaginationforpeople.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class About {
	public static final int API_VERSION = 2;
	
	private int version;
	@JsonProperty("throttle_count")
	private int throttleCount;
	@JsonProperty("throttle_timeframe")
	private int throttleTimeframe;
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getThrottleCount() {
		return throttleCount;
	}
	public void setThrottleCount(int throttleCount) {
		this.throttleCount = throttleCount;
	}
	public int getThrottleTimeframe() {
		return throttleTimeframe;
	}
	public void setThrottleTimeframe(int throttleTimeframe) {
		this.throttleTimeframe = throttleTimeframe;
	}
}
