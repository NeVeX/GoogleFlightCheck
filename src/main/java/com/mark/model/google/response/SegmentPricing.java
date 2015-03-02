package com.mark.model.google.response;

import java.util.List;

public class SegmentPricing {
	private String kind;
	private String fareId;
	private String segmentId;
	private List<BaggageOption> freeBaggageOption;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getFareId() {
		return fareId;
	}
	public void setFareId(String fareId) {
		this.fareId = fareId;
	}
	public String getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}
	public List<BaggageOption> getFreeBaggageOption() {
		return freeBaggageOption;
	}
	public void setFreeBaggageOption(List<BaggageOption> freeBaggageOption) {
		this.freeBaggageOption = freeBaggageOption;
	}
	
}
