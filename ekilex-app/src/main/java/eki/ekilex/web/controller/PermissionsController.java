package eki.ekilex.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import eki.common.constant.AuthorityItem;
import eki.common.constant.AuthorityOperation;
import eki.common.constant.OrderingField;
import eki.ekilex.constant.WebConstant;
import eki.ekilex.data.Classifier;
import eki.ekilex.data.EkiUser;
import eki.ekilex.data.EkiUserPermData;
import eki.ekilex.service.CommonDataService;
import eki.ekilex.service.MaintenanceService;
import eki.ekilex.service.PermissionService;
import eki.ekilex.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ConditionalOnWebApplication
@Controller
@SessionAttributes(WebConstant.SESSION_BEAN)
public class PermissionsController extends AbstractPageController {

	private static final Logger logger = LoggerFactory.getLogger(PermissionsController.class);

	private static final OrderingField DEFAULT_ORDER_BY = OrderingField.NAME;

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private CommonDataService commonDataService;

	@Autowired
	private MaintenanceService maintenanceService;

	@GetMapping(PERMISSIONS_URI)
	public String permissions(@RequestParam(name = "orderBy", required = false) OrderingField orderBy, Model model) {
		EkiUser user = userService.getAuthenticatedUser();
		if (!user.isDatasetOwnershipExist() && !user.isAdmin()) {
			return "redirect:" + HOME_URI;
		}
		populateUserPermDataModel(model, orderBy);
		return PERMISSIONS_PAGE;
	}

	@GetMapping(PERMISSIONS_URI + "/enable/{userId}/{orderBy}")
	public String enable(@PathVariable("userId") Long userId, @PathVariable("orderBy") OrderingField orderBy, Model model) {
		userService.enableUser(userId, true);
		populateUserPermDataModel(model, orderBy);
		return PERMISSIONS_PAGE + PAGE_FRAGMENT_ELEM + "permissions";
	}

	@GetMapping(PERMISSIONS_URI + "/disable/{userId}/{orderBy}")
	public String disable(@PathVariable("userId") Long userId, @PathVariable("orderBy") OrderingField orderBy, Model model) {
		userService.enableUser(userId, false);
		populateUserPermDataModel(model, orderBy);
		return PERMISSIONS_PAGE + PAGE_FRAGMENT_ELEM + "permissions";
	}

	@GetMapping(PERMISSIONS_URI + "/setadmin/{userId}/{orderBy}")
	public String setAdmin(@PathVariable("userId") Long userId, @PathVariable("orderBy") OrderingField orderBy, Model model) {
		userService.setAdmin(userId, true);
		populateUserPermDataModel(model, orderBy);
		return PERMISSIONS_PAGE + PAGE_FRAGMENT_ELEM + "permissions";
	}

	@GetMapping(PERMISSIONS_URI + "/remadmin/{userId}/{orderBy}")
	public String remAdmin(@PathVariable("userId") Long userId, @PathVariable("orderBy") OrderingField orderBy, Model model) {
		userService.setAdmin(userId, false);
		populateUserPermDataModel(model, orderBy);
		return PERMISSIONS_PAGE + PAGE_FRAGMENT_ELEM + "permissions";
	}

	@GetMapping(PERMISSIONS_URI + "/setreviewed/{userId}/{orderBy}")
	public String setReviewed(@PathVariable("userId") Long userId, @PathVariable("orderBy") OrderingField orderBy, Model model) {
		userService.setReviewed(userId, true);
		populateUserPermDataModel(model, orderBy);
		return PERMISSIONS_PAGE + PAGE_FRAGMENT_ELEM + "permissions";
	}

	@GetMapping(PERMISSIONS_URI + "/remreviewed/{userId}/{orderBy}")
	public String remReviewed(@PathVariable("userId") Long userId, @PathVariable("orderBy") OrderingField orderBy, Model model) {
		userService.setReviewed(userId, false);
		populateUserPermDataModel(model, orderBy);
		return PERMISSIONS_PAGE + PAGE_FRAGMENT_ELEM + "permissions";
	}

	@PostMapping(PERMISSIONS_URI + "/adddatasetperm")
	public String addDatasetPerm(
			@RequestParam("userId") Long userId,
			@RequestParam(value = "datasetCode", required = false) String datasetCode,
			@RequestParam(value = "authItem", required = false) AuthorityItem authItem,
			@RequestParam(value = "authOp", required = false) AuthorityOperation authOp,
			@RequestParam(value = "authLang", required = false) String authLang,
			@RequestParam("orderBy") OrderingField orderBy) {

		permissionService.createDatasetPermission(userId, datasetCode, authItem, authOp, authLang);
		String redirectUri = createRedirectUri(orderBy);
		return "redirect:" + redirectUri;
	}

	@GetMapping(PERMISSIONS_URI + "/deletedatasetperm/{datasetPermissionId}/{orderBy}")
	public String deleteDatasetPerm(@PathVariable("datasetPermissionId") Long datasetPermissionId, @PathVariable("orderBy") OrderingField orderBy, Model model) {
		permissionService.deleteDatasetPermission(datasetPermissionId);
		populateUserPermDataModel(model, orderBy);
		return PERMISSIONS_PAGE + PAGE_FRAGMENT_ELEM + "permissions";
	}

	@PostMapping(PERMISSIONS_URI + "/updatereviewcomment")
	public String updateReviewComment(@RequestParam("userId") Long userId, @RequestParam("reviewComment") String reviewComment,
			@RequestParam("orderBy") OrderingField orderBy) {

		userService.updateReviewComment(userId, reviewComment);
		String redirectUri = createRedirectUri(orderBy);
		return "redirect:" + redirectUri;
	}

	@GetMapping(PERMISSIONS_URI + "/deletereviewcomment/{userId}/{orderBy}")
	public String deleteReviewComment(@PathVariable("userId") Long userId, @PathVariable("orderBy") OrderingField orderBy) {
		userService.updateReviewComment(userId, null);
		String redirectUri = createRedirectUri(orderBy);
		return "redirect:" + redirectUri;
	}

	@ResponseBody
	@GetMapping(PERMISSIONS_URI + "/clearcache")
	public String clearCache() {
		maintenanceService.clearCache();
		return "OK";
	}

	@GetMapping(PERMISSIONS_URI + "/sendpermissionsemail/{userEmail}")
	@ResponseBody
	public String sendPermissionsEmail(@PathVariable("userEmail") String userEmail) {

		permissionService.sendPermissionsEmail(userEmail);
		return "OK";
	}

	@ResponseBody
	@GetMapping(PERMISSIONS_URI + "/dataset_languages/{datasetCode}")
	public String getDataSetLanguages(@PathVariable String datasetCode) throws Exception {

		List<Classifier> userLanguages = commonDataService.getDatasetLanguages(datasetCode);

		ObjectMapper jsonMapper = new ObjectMapper();
		return jsonMapper.writeValueAsString(userLanguages);
	}

	private void populateUserPermDataModel(Model model, OrderingField orderBy) {

		if (orderBy == null) {
			orderBy = DEFAULT_ORDER_BY;
		}
		List<EkiUserPermData> ekiUserPermissions = permissionService.getEkiUserPermissions(orderBy);
		model.addAttribute("ekiUserPermissions", ekiUserPermissions);
		model.addAttribute("orderBy", orderBy);
	}

	private String createRedirectUri(@RequestParam("orderBy") OrderingField orderBy) {
		return PERMISSIONS_URI + "?orderBy=" + orderBy.name();
	}
}
