package com.craftsteamg.helix.internal.objects;

public class Warning {


	private String reason;
	private long issuer;
	private long receiver;

	public String getReason() {
		return reason;
	}

	public long getIssuer() {
		return issuer;
	}

	public long getReceiver() {
		return receiver;
	}

	public Warning(String reason, long issuer, long receiver) {
		this.reason = reason;
		this.issuer = issuer;
		this.receiver = receiver;
	}

	public Warning() {

	}
}
