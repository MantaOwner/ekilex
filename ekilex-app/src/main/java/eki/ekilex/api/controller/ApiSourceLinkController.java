package eki.ekilex.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eki.ekilex.data.SourceLink;
import eki.ekilex.data.api.ApiResponse;
import eki.ekilex.service.SourceLinkService;

@ConditionalOnWebApplication
@RestController
public class ApiSourceLinkController extends AbstractApiController {

	@Autowired
	private SourceLinkService sourceLinkService;

	@Order(301)
	@PreAuthorize("@permEval.isSourceLinkCrudGranted(authentication, #crudRoleDataset, #sourceLink)")
	@GetMapping(value = API_SERVICES_URI + SOURCE_LINK_URI + CREATE_URI)
	@ResponseBody
	public ApiResponse createSourceLink(
			@RequestParam("crudRoleDataset") String crudRoleDataset,
			@RequestBody SourceLink sourceLink) {

		try {
			String name = sourceLink.getName();
			name = valueUtil.trimAndCleanAndRemoveHtmlAndLimit(name);
			sourceLink.setName(name);
			String value = sourceLink.getValue();
			value = valueUtil.trimAndCleanAndRemoveHtmlAndLimit(value);
			sourceLink.setValue(value);
			Long sourceLinkId = sourceLinkService.createSourceLink(sourceLink);
			if (sourceLinkId == null) {
				return getOpFailResponse("Invalid or unsupported source link composition");
			}
			return getOpSuccessResponse(sourceLinkId);
		} catch (Exception e) {
			return getOpFailResponse(e);
		}
	}
}
