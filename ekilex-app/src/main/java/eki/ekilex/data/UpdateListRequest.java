package eki.ekilex.data;

import java.util.List;

import eki.common.data.AbstractDataObject;

public class UpdateListRequest extends AbstractDataObject {

	private static final long serialVersionUID = 1L;

	private String opCode;

	private List<ListData> items;

	private Long additionalInfo;

	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public List<ListData> getItems() {
		return items;
	}

	public void setItems(List<ListData> items) {
		this.items = items;
	}

	public Long getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(Long additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
