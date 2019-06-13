package eki.ekilex.runner.util;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import eki.common.constant.FreeformType;
import eki.ekilex.data.transform.Freeform;

public class FreeformRowMapper implements RowMapper<Freeform> {

	@Override
	public Freeform mapRow(ResultSet rs, int rowNum) throws SQLException {

		Long freeformId = rs.getLong("id");
		Long parentId = rs.getLong("parent_id");
		String typeStr = rs.getString("type");
		FreeformType type = null;
		if (StringUtils.isNotBlank(typeStr)) {
			type = FreeformType.valueOf(typeStr);
		}
		String valueText = rs.getString("value_text");
		String valuePrese = rs.getString("value_prese");
		Timestamp valueDate = rs.getTimestamp("value_date");
		Float valueNumber = rs.getFloat("value_number");
		Array valueArrayObj = rs.getArray("value_array");
		String[] valueArray = null;
		if (valueArrayObj != null) {
			valueArray = (String[]) valueArrayObj.getArray();
		}
		String classifName = rs.getString("classif_name");
		String classifCode = rs.getString("classif_code");
		String langCode = rs.getString("lang");
		Long orderBy = rs.getLong("order_by");
		boolean childrenExist = rs.getBoolean("children_exist");
		boolean sourceLinksExist = rs.getBoolean("source_links_exist");

		Freeform freeform = new Freeform();
		freeform.setFreeformId(freeformId);
		freeform.setParentId(parentId);
		freeform.setType(type);
		freeform.setValueText(valueText);
		freeform.setValuePrese(valuePrese);
		freeform.setValueDate(valueDate);
		freeform.setValueNumber(valueNumber);
		freeform.setValueArray(valueArray);
		freeform.setClassifName(classifName);
		freeform.setClassifCode(classifCode);
		freeform.setLangCode(langCode);
		freeform.setOrderBy(orderBy);
		freeform.setChildrenExist(childrenExist);
		freeform.setSourceLinksExist(sourceLinksExist);
		return freeform;
	}

}
