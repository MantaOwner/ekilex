package eki.ekilex.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eki.common.constant.FreeformType;
import eki.common.constant.SourceType;
import eki.ekilex.constant.WebConstant;
import eki.ekilex.data.DatasetPermission;
import eki.ekilex.data.Source;
import eki.ekilex.data.SourceProperty;
import eki.ekilex.data.SourceRequest;
import eki.ekilex.data.UserContextData;
import eki.ekilex.service.SourceService;
import eki.ekilex.web.util.ValueUtil;

@ConditionalOnWebApplication
@Controller
@SessionAttributes(WebConstant.SESSION_BEAN)
public class SourceEditController extends AbstractPageController {

	private static final Logger logger = LoggerFactory.getLogger(SourceEditController.class);

	@Autowired
	private SourceService sourceService;

	@Autowired
	private ValueUtil valueUtil;

	@PostMapping(UPDATE_SOURCE_PROPERTY_URI)
	public String updateSourceProperty(
			@RequestParam("sourcePropertyId") Long sourcePropertyId,
			@RequestParam("valueText") String valueText,
			Model model) throws Exception {

		UserContextData userContextData = getUserContextData();
		DatasetPermission userRole = userContextData.getUserRole();
		valueText = valueUtil.trimAndCleanAndRemoveHtmlAndLimit(valueText);

		Long sourceId = sourceService.getSourceId(sourcePropertyId);
		logger.debug("Updating source property with id: {}, source id: {}", sourcePropertyId, sourceId);

		sourceService.updateSourceProperty(sourcePropertyId, valueText);
		Source source = sourceService.getSource(sourceId, userRole);
		model.addAttribute("source", source);

		return SOURCE_COMPONENTS_PAGE + PAGE_FRAGMENT_ELEM + SOURCE_SEARCH_RESULT;
	}

	@PostMapping(CREATE_SOURCE_PROPERTY_URI)
	public String createSourceProperty(
			@RequestParam("sourceId") Long sourceId,
			@RequestParam("type") FreeformType type,
			@RequestParam("valueText") String valueText,
			Model model) throws Exception {

		logger.debug("Creating property for source with id: {}", sourceId);

		UserContextData userContextData = getUserContextData();
		DatasetPermission userRole = userContextData.getUserRole();

		valueText = valueUtil.trimAndCleanAndRemoveHtmlAndLimit(valueText);
		sourceService.createSourceProperty(sourceId, type, valueText);
		Source source = sourceService.getSource(sourceId, userRole);
		model.addAttribute("source", source);

		return SOURCE_COMPONENTS_PAGE + PAGE_FRAGMENT_ELEM + SOURCE_SEARCH_RESULT;
	}

	@GetMapping(DELETE_SOURCE_PROPERTY_URI + "/{sourcePropertyId}")
	public String deleteSourceProperty(@PathVariable("sourcePropertyId") Long sourcePropertyId, Model model) throws Exception {

		UserContextData userContextData = getUserContextData();
		DatasetPermission userRole = userContextData.getUserRole();
		Long sourceId = sourceService.getSourceId(sourcePropertyId);
		logger.debug("Deleting source property with id: {}, source id: {}", sourcePropertyId, sourceId);

		sourceService.deleteSourceProperty(sourcePropertyId);
		Source source = sourceService.getSource(sourceId, userRole);
		model.addAttribute("source", source);

		return SOURCE_COMPONENTS_PAGE + PAGE_FRAGMENT_ELEM + SOURCE_SEARCH_RESULT;
	}

	@PostMapping(UPDATE_SOURCE_URI)
	public String updateSource(
			@RequestParam("sourceId") Long sourceId,
			@RequestParam("sourceType") SourceType type,
			Model model) throws Exception {

		logger.debug("Updating source type, source id: {}", sourceId);

		UserContextData userContextData = getUserContextData();
		DatasetPermission userRole = userContextData.getUserRole();

		sourceService.updateSource(sourceId, type);
		Source source = sourceService.getSource(sourceId, userRole);
		model.addAttribute("source", source);

		return SOURCE_COMPONENTS_PAGE + PAGE_FRAGMENT_ELEM + SOURCE_SEARCH_RESULT;
	}

	@PostMapping(CREATE_SOURCE_URI)
	@ResponseBody
	public String createSource(@RequestBody SourceRequest source) throws Exception {

		String sourceName = source.getName();
		SourceType sourceType = source.getType();
		List<SourceProperty> properties = source.getProperties();

		logger.debug("Creating new source, source name: {}", sourceName);

		SourceProperty name = new SourceProperty();
		name.setType(FreeformType.SOURCE_NAME);
		name.setValueText(sourceName);
		properties.add(0, name);

		properties.forEach(property -> {
			String valueText = property.getValueText();
			valueText = valueUtil.trimAndCleanAndRemoveHtmlAndLimit(valueText);
			property.setValueText(valueText);
		});

		Long sourceId = sourceService.createSource(sourceType, properties);

		return String.valueOf(sourceId);
	}

	@GetMapping(VALIDATE_DELETE_SOURCE_URI + "/{sourceId}")
	@ResponseBody
	public String validateSourceDelete(@PathVariable("sourceId") Long sourceId) throws JsonProcessingException {

		logger.debug("Validating source delete, source id: {}", sourceId);

		Map<String, String> response = new HashMap<>();
		if (sourceService.validateSourceDelete(sourceId)) {
			response.put("status", "ok");
		} else {
			response.put("status", "invalid");
			response.put("message", "Allikat ei saa kustutada, sest sellele on viidatud.");
		}
		ObjectMapper jsonMapper = new ObjectMapper();
		return jsonMapper.writeValueAsString(response);
	}

	@GetMapping(DELETE_SOURCE_URI + "/{sourceId}")
	@ResponseBody
	public String deleteSource(@PathVariable("sourceId") Long sourceId) throws Exception {

		logger.debug("Deleting source with id: {}", sourceId);

		sourceService.deleteSource(sourceId);
		return RESPONSE_OK_VER1;
	}

	@PostMapping(SOURCE_JOIN_URI)
	public String joinSources(
			@RequestParam("sourceId") Long sourceId,
			@RequestParam("previousSearch") String previousSearch,
			Model model) {

		UserContextData userContextData = getUserContextData();
		DatasetPermission userRole = userContextData.getUserRole();
		Source targetSource = sourceService.getSource(sourceId, userRole);
		model.addAttribute("targetSource", targetSource);
		model.addAttribute("previousSearch", previousSearch);

		return SOURCE_JOIN_PAGE;
	}

	@PostMapping(JOIN_SOURCES_URI)
	public String joinSources(
			@RequestParam("targetSourceId") Long targetSourceId,
			@RequestParam("originSourceId") Long originSourceId) throws Exception {

		sourceService.joinSources(targetSourceId, originSourceId);
		return "redirect:" + SOURCE_SEARCH_URI + "/" + targetSourceId;
	}

	@PostMapping(SEARCH_SOURCES_URI)
	public String searchSources(
			@RequestParam("targetSourceId") Long targetSourceId,
			@RequestParam(name = "searchFilter", required = false) String searchFilter,
			@RequestParam("previousSearch") String previousSearch,
			Model model) {

		UserContextData userContextData = getUserContextData();
		DatasetPermission userRole = userContextData.getUserRole();
		Source targetSource = sourceService.getSource(targetSourceId, userRole);
		List<Source> sources = sourceService.getSourcesExcludingOne(searchFilter, targetSource, userRole);
		model.addAttribute("targetSource", targetSource);
		model.addAttribute("sources", sources);
		model.addAttribute("searchFilter", searchFilter);
		model.addAttribute("previousSearch", previousSearch);

		return SOURCE_JOIN_PAGE;
	}

}
