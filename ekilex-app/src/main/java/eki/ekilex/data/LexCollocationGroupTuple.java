package eki.ekilex.data;

import eki.common.data.AbstractDataObject;

public class LexCollocationGroupTuple extends AbstractDataObject {

	private static final long serialVersionUID = 1L;

	private Long relGroupId;

	private String relGroupName;

	private Float relGroupFrequency;

	private Float relGroupScore;

	private Long relGroupOrderBy;

	private Long posGroupId;

	private String posGroupCode;

	private Long posGroupOrderBy;

	public Long getRelGroupId() {
		return relGroupId;
	}

	public void setRelGroupId(Long relGroupId) {
		this.relGroupId = relGroupId;
	}

	public String getRelGroupName() {
		return relGroupName;
	}

	public void setRelGroupName(String relGroupName) {
		this.relGroupName = relGroupName;
	}

	public Float getRelGroupFrequency() {
		return relGroupFrequency;
	}

	public void setRelGroupFrequency(Float relGroupFrequency) {
		this.relGroupFrequency = relGroupFrequency;
	}

	public Float getRelGroupScore() {
		return relGroupScore;
	}

	public void setRelGroupScore(Float relGroupScore) {
		this.relGroupScore = relGroupScore;
	}

	public Long getRelGroupOrderBy() {
		return relGroupOrderBy;
	}

	public void setRelGroupOrderBy(Long relGroupOrderBy) {
		this.relGroupOrderBy = relGroupOrderBy;
	}

	public Long getPosGroupId() {
		return posGroupId;
	}

	public void setPosGroupId(Long posGroupId) {
		this.posGroupId = posGroupId;
	}

	public String getPosGroupCode() {
		return posGroupCode;
	}

	public void setPosGroupCode(String posGroupCode) {
		this.posGroupCode = posGroupCode;
	}

	public Long getPosGroupOrderBy() {
		return posGroupOrderBy;
	}

	public void setPosGroupOrderBy(Long posGroupOrderBy) {
		this.posGroupOrderBy = posGroupOrderBy;
	}
}