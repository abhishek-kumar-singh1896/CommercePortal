/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.commerceorgaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gallagher.commerceorgaddon.controllers.GallaghercommerceorgaddonControllerConstants;
import com.gallagher.commerceorgaddon.forms.B2BUserGroupForm;


/**
 * Controller for b2b user group management page.
 */
@Controller
@RequestMapping("/my-company/organization-management/manage-usergroups")
public class B2BUserGroupManagementPageController extends MyCompanyPageController
{

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String getPagedB2BUserGroups(@RequestParam(value = "page", defaultValue = "0")
	final int page, @RequestParam(value = "show", defaultValue = "Page")
	final AbstractSearchPageController.ShowMode showMode, @RequestParam(value = "sort", defaultValue = B2BUserGroupModel.UID)
	final String sortCode, final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel myCompanyPage = getContentPageForLabelOrId(MY_COMPANY_CMS_PAGE);
		storeCmsPageInModel(model, myCompanyPage);
		setUpMetaDataForContentPage(model, myCompanyPage);
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserGroupBreadCrumbs();
		model.addAttribute("breadcrumbs", breadcrumbs);

		// Handle paged search results
		final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
		final SearchPageData<B2BUserGroupData> searchPageData = b2bUserGroupFacade.getPagedB2BUserGroups(pageableData);
		populateModel(model, searchPageData, showMode);
		model.addAttribute("action", "manageB2BUserGroup");
		model.addAttribute("unit", b2bUnitFacade.getParentUnit());
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return GallaghercommerceorgaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUserGroupsPage;
	}

	@RequestMapping(value = "/disable", method = RequestMethod.GET)
	@RequireHardLogIn
	public String disableUsergroupConfirmation(@RequestParam("usergroup")
	final String usergroup, final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel manageUsergroupsPage = getContentPageForLabelOrId(MANAGE_USERGROUPS_CMS_PAGE);
		storeCmsPageInModel(model, manageUsergroupsPage);
		setUpMetaDataForContentPage(model, manageUsergroupsPage);
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserGroupDetailsBreadCrumbs(usergroup);
		breadcrumbs.add(new Breadcrumb(
				String.format("/my-company/organization-management/manage-usergroups/disable?usergroup=%s", urlEncode(usergroup)),
				getMessageSource().getMessage("text.company.manageUsergroups.disable.breadcrumb", new Object[]
				{ usergroup }, "Disable {0} Usergroup ", getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		final B2BUserGroupData userGroupData = b2bUserGroupFacade.getB2BUserGroup(usergroup);
		model.addAttribute("usergroup", userGroupData);
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return GallaghercommerceorgaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUsergroupDisableConfirmationPage;
	}

	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@RequireHardLogIn
	public String disableUserGroup(@RequestParam("usergroup")
	final String usergroup) throws CMSItemNotFoundException
	{
		b2bUserGroupFacade.disableUserGroup(usergroup);
		return String.format(REDIRECT_TO_USERGROUP_DETAILS, urlEncode(usergroup));
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	@RequireHardLogIn
	public String removeUsergroupConfirmation(@RequestParam("usergroup")
	final String usergroup, final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel manageUsergroupsPage = getContentPageForLabelOrId(MANAGE_USERGROUPS_CMS_PAGE);
		storeCmsPageInModel(model, manageUsergroupsPage);
		setUpMetaDataForContentPage(model, manageUsergroupsPage);
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserGroupDetailsBreadCrumbs(usergroup);
		breadcrumbs.add(new Breadcrumb(
				String.format("/my-company/organization-management/manage-usergroups/remove?usergroup=%s", urlEncode(usergroup)),
				getMessageSource().getMessage("text.company.manageUsergroups.remove.breadcrumb", new Object[]
				{ usergroup }, "Remove {0} Usergroup ", getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		final B2BUserGroupData userGroupData = b2bUserGroupFacade.getB2BUserGroup(usergroup);
		model.addAttribute("usergroup", userGroupData);
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return GallaghercommerceorgaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUsergroupRemoveConfirmationPage;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@RequireHardLogIn
	public String removeUserGroup(@RequestParam("usergroup")
	final String usergroup, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		b2bUserGroupFacade.removeUserGroup(usergroup);
		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
				"text.company.manageUsergroups.remove.success");
		return REDIRECT_TO_USER_GROUPS_PAGE;
	}

	@RequestMapping(value = "/permissions", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getPagedPermissionsForUserGroup(@RequestParam(value = "page", defaultValue = "0")
	final int page, @RequestParam(value = "show", defaultValue = "Page")
	final AbstractSearchPageController.ShowMode showMode, @RequestParam(value = "sort", defaultValue = UserModel.NAME)
	final String sortCode, @RequestParam("usergroup")
	final String usergroup, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final ContentPageModel myCompanyPage = getContentPageForLabelOrId(MY_COMPANY_CMS_PAGE);
		storeCmsPageInModel(model, myCompanyPage);
		setUpMetaDataForContentPage(model, myCompanyPage);

		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserGroupDetailsBreadCrumbs(usergroup);
		breadcrumbs.add(new Breadcrumb(
				String.format("/my-company/organization-management/manage-usergroups/permissions?usergroup=%s", urlEncode(usergroup)),
				getMessageSource().getMessage("text.company.usergroups.permissions.breadcrumb", new Object[]
				{ usergroup }, "Usergroup {0} Permissions", getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);

		// Handle paged search results
		final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
		final SearchPageData<B2BPermissionData> searchPageData = b2bPermissionFacade.getPagedPermissionsForUserGroup(pageableData,
				usergroup);
		populateModel(model, searchPageData, showMode);
		model.addAttribute("action", "permissions");
		model.addAttribute("baseUrl", MANAGE_USERGROUPS_BASE_URL);
		model.addAttribute("cancelUrl", getCancelUrl(MANAGE_USERGROUP_DETAILS_URL, request.getContextPath(), usergroup));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return GallaghercommerceorgaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUserGroupPermissionsPage;
	}

	@ResponseBody
	@RequestMapping(value = "/permissions/select", method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public B2BSelectionData selectPermissonForUserGroup(@RequestParam("usergroup")
	final String usergroup, @RequestParam("permission")
	final String permission) throws CMSItemNotFoundException
	{
		return b2bPermissionFacade.addPermissionToUserGroup(usergroup, permission);
	}

	@ResponseBody
	@RequestMapping(value = "/permissions/deselect", method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public B2BSelectionData deselectPermissonForUserGroup(@RequestParam("usergroup")
	final String usergroup, @RequestParam("permission")
	final String permission) throws CMSItemNotFoundException
	{
		return b2bPermissionFacade.removePermissionFromUserGroup(usergroup, permission);
	}

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	@RequireHardLogIn
	public String viewUserGroupDetails(@RequestParam("usergroup")
	final String usergroup, final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel manageUsergroupsPage = getContentPageForLabelOrId(MANAGE_USERGROUPS_CMS_PAGE);
		storeCmsPageInModel(model, manageUsergroupsPage);
		setUpMetaDataForContentPage(model, manageUsergroupsPage);
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserGroupDetailsBreadCrumbs(usergroup);
		model.addAttribute("breadcrumbs", breadcrumbs);

		final B2BUserGroupData userGroupData = b2bUserGroupFacade.getB2BUserGroup(usergroup);
		if (userGroupData == null)
		{
			GlobalMessages.addErrorMessage(model, "usergroup.notfound");
		}
		else if (CollectionUtils.isEmpty(userGroupData.getMembers()))
		{
			GlobalMessages.addInfoMessage(model, "usergroup.disabled");

		}
		model.addAttribute("usergroup", userGroupData);
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return GallaghercommerceorgaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUsergroupViewPage;
	}

	@RequestMapping(value = "/members", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getPagedCustomersForUserGroup(@RequestParam(value = "page", defaultValue = "0")
	final int page, @RequestParam(value = "show", defaultValue = "Page")
	final AbstractSearchPageController.ShowMode showMode, @RequestParam(value = "sort", defaultValue = UserModel.NAME)
	final String sortCode, @RequestParam("usergroup")
	final String usergroup, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final ContentPageModel manageUsergroupsPage = getContentPageForLabelOrId(MANAGE_USERGROUPS_CMS_PAGE);
		storeCmsPageInModel(model, manageUsergroupsPage);
		setUpMetaDataForContentPage(model, manageUsergroupsPage);

		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserGroupDetailsBreadCrumbs(usergroup);
		breadcrumbs.add(new Breadcrumb(
				String.format("/my-company/organization-management/manage-usergroups/members?usergroup=%s", urlEncode(usergroup)),
				getMessageSource().getMessage("text.company.usergroups.members.breadcrumb", new Object[]
				{ usergroup }, "Usergroup {0} members", getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		final B2BUserGroupData userGroupData = b2bUserGroupFacade.getB2BUserGroup(usergroup);
		model.addAttribute("usergroup", userGroupData);

		// Handle paged search results
		final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
		final SearchPageData<CustomerData> searchPageData = b2bUserGroupFacade.getPagedCustomersForUserGroup(pageableData,
				usergroup);
		populateModel(model, searchPageData, showMode);
		model.addAttribute("action", "members");
		model.addAttribute("baseUrl", MANAGE_USERGROUPS_BASE_URL);
		model.addAttribute("cancelUrl", getCancelUrl(MANAGE_USERGROUP_DETAILS_URL, request.getContextPath(), usergroup));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return GallaghercommerceorgaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUserGroupMembersPage;
	}

	@ResponseBody
	@RequestMapping(value = "/members/select", method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public CustomerData selectMemberOfUnitGroup(@RequestParam("usergroup")
	final String usergroup, @RequestParam("user")
	final String user) throws CMSItemNotFoundException
	{
		return populateDisplayNamesForRoles(b2bUserGroupFacade.addMemberToUserGroup(usergroup, user));
	}

	@ResponseBody
	@RequestMapping(value = "/members/deselect", method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public CustomerData deselectMemberOfUnitGroup(@RequestParam("usergroup")
	final String usergroup, @RequestParam("user")
	final String user) throws CMSItemNotFoundException
	{
		return populateDisplayNamesForRoles(b2bUserGroupFacade.removeMemberFromUserGroup(usergroup, user));
	}

	protected CustomerData populateDisplayNamesForRoles(final CustomerData userData)
	{
		final Collection<String> roles = userData.getRoles();
		final List<String> displayRoles = new ArrayList<String>(roles.size());
		for (final String role : roles)
		{
			displayRoles.add(
					getMessageSource().getMessage("b2busergroup." + role + ".name", null, role, getI18nService().getCurrentLocale()));
		}
		userData.setDisplayRoles(displayRoles);
		return userData;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	@RequireHardLogIn
	public String editUserGroup(@RequestParam("usergroup")
	final String usergroup, final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel manageUsergroupsPage = getContentPageForLabelOrId(MANAGE_USERGROUPS_CMS_PAGE);
		storeCmsPageInModel(model, manageUsergroupsPage);
		setUpMetaDataForContentPage(model, manageUsergroupsPage);
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserGroupDetailsBreadCrumbs(usergroup);
		breadcrumbs.add(new Breadcrumb(
				String.format("/my-company/organization-management/manage-usergroups/edit/?usergroup=%s", urlEncode(usergroup)),
				getMessageSource().getMessage("text.company.manageUsergroups.editUsergroup.breadcrumb", new Object[]
				{ usergroup }, "Edit {0} Usergroup ", getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);

		if (!model.containsAttribute("b2BUserGroupForm"))
		{
			final B2BUserGroupForm b2BUserGroupForm = new B2BUserGroupForm();
			final B2BUserGroupData userGroupData = b2bUserGroupFacade.getB2BUserGroup(usergroup);
			if (userGroupData == null)
			{
				GlobalMessages.addErrorMessage(model, "usergroup.notfound");
			}
			else
			{
				b2BUserGroupForm.setOriginalUid(userGroupData.getUid());
				if (userGroupData.getUnit() != null)
				{
					b2BUserGroupForm.setParentUnit(userGroupData.getUnit().getUid());
				}
				b2BUserGroupForm.setUid(userGroupData.getUid());
				b2BUserGroupForm.setName(userGroupData.getName());
			}
			model.addAttribute(b2BUserGroupForm);
		}

		model.addAttribute("branchSelectOptions", getBranchSelectOptions(b2bUnitFacade.getBranchNodes()));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return GallaghercommerceorgaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUsergroupEditPage;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@RequireHardLogIn
	public String editUserGroup(@RequestParam("usergroup")
	final String usergroup, @Valid
	final B2BUserGroupForm userGroupForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final ContentPageModel manageUsergroupsPage = getContentPageForLabelOrId(MANAGE_USERGROUPS_CMS_PAGE);
		storeCmsPageInModel(model, manageUsergroupsPage);
		setUpMetaDataForContentPage(model, manageUsergroupsPage);
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserGroupDetailsBreadCrumbs(usergroup);
		breadcrumbs.add(new Breadcrumb(
				String.format("/my-company/organization-management/manage-usergroups/edit?usergroup=%s", urlEncode(usergroup)),
				getMessageSource().getMessage("text.company.manageUsergroups.editUsergroup.breadcrumb", new Object[]
				{ usergroup }, "Edit {0} Usergroup ", getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			model.addAttribute(userGroupForm);
			return editUserGroup(usergroup, model);

		}
		if (!userGroupForm.getUid().equals(usergroup) && b2bUserGroupFacade.getB2BUserGroup(userGroupForm.getUid()) != null)
		{
			// a unit uid is not unique
			GlobalMessages.addErrorMessage(model, "form.global.error");
			bindingResult.rejectValue("uid", "form.b2busergroup.notunique");
			model.addAttribute(userGroupForm);
			return editUserGroup(usergroup, model);
		}

		final B2BUserGroupData userGroupData = b2bUserGroupFacade.getB2BUserGroup(usergroup);
		if (userGroupData != null)
		{
			boolean userGroupUpdated = false;

			userGroupData.setUid(userGroupForm.getUid());
			userGroupData.setName(userGroupForm.getName());
			if (StringUtils.isNotBlank(userGroupForm.getParentUnit()))
			{
				final B2BUnitData newUserGroup = b2bUnitFacade.getUnitForUid(userGroupForm.getParentUnit());
				if (!newUserGroup.getUid().equals(userGroupData.getUnit().getUid()))
				{
					userGroupUpdated = true;
				}

				userGroupData.setUnit(newUserGroup);
			}

			b2bUserGroupFacade.updateUserGroup(userGroupForm.getOriginalUid(), userGroupData);

			if (userGroupUpdated)
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER,
						"form.b2busergroup.parentunit.updated");
			}
			else
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "form.b2busergroup.success");
			}

			return String.format(REDIRECT_TO_USERGROUP_DETAILS, urlEncode(userGroupForm.getUid()));

		}
		else
		{
			// user has no permissions to edit the group.
			GlobalMessages.addErrorMessage(model, "form.b2busergroup.noeditpermissions");
			model.addAttribute(userGroupForm);
			return editUserGroup(usergroup, model);
		}

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	@RequireHardLogIn
	public String createUserGroup(final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel manageUsergroupsPage = getContentPageForLabelOrId(MANAGE_USERGROUPS_CMS_PAGE);
		storeCmsPageInModel(model, manageUsergroupsPage);
		setUpMetaDataForContentPage(model, manageUsergroupsPage);
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserGroupBreadCrumbs();
		breadcrumbs.add(new Breadcrumb("/my-company/organization-management/manage-usergroups/create",
				getMessageSource().getMessage("text.company.manageUsergroups.createUsergroup.breadcrumb", null, "Create Usergroup ",
						getI18nService().getCurrentLocale()),
				null));
		model.addAttribute("breadcrumbs", breadcrumbs);

		if (!model.containsAttribute("b2BUserGroupForm"))
		{
			final B2BUnitData unitData = b2bUnitFacade.getParentUnit();
			final B2BUserGroupForm b2BUserGroupForm = new B2BUserGroupForm();
			b2BUserGroupForm.setParentUnit(unitData.getUid());
			model.addAttribute(b2BUserGroupForm);
		}

		model.addAttribute("branchSelectOptions", getBranchSelectOptions(b2bUnitFacade.getBranchNodes()));
		model.addAttribute("unit", b2bUnitFacade.getParentUnit());
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return GallaghercommerceorgaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUsergroupCreatePage;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@RequireHardLogIn
	public String createUserGroup(@Valid
	final B2BUserGroupForm userGroupForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final ContentPageModel manageUsergroupsPage = getContentPageForLabelOrId(MANAGE_USERGROUPS_CMS_PAGE);
		storeCmsPageInModel(model, manageUsergroupsPage);
		setUpMetaDataForContentPage(model, manageUsergroupsPage);
		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUserGroupBreadCrumbs();
		breadcrumbs.add(new Breadcrumb("/my-company/organization-management/manage-usergroups/create",
				getMessageSource().getMessage("text.company.manageUsergroups.createUsergroup.breadcrumb", null, "Create Usergroup ",
						getI18nService().getCurrentLocale()),
				null));
		model.addAttribute("breadcrumbs", breadcrumbs);

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			model.addAttribute(userGroupForm);
			return createUserGroup(model);

		}
		if (b2bUserGroupFacade.getUserGroupDataForUid(userGroupForm.getUid()) != null)
		{
			// a unit uid is not unique
			GlobalMessages.addErrorMessage(model, "form.global.error");
			bindingResult.rejectValue("uid", "form.b2busergroup.notunique");
			model.addAttribute(userGroupForm);
			return createUserGroup(model);
		}

		final B2BUserGroupData userGroupData = new B2BUserGroupData();
		userGroupData.setUid(userGroupForm.getUid());
		userGroupData.setName(userGroupForm.getName());
		if (StringUtils.isNotBlank(userGroupForm.getParentUnit()))
		{
			userGroupData.setUnit(b2bUnitFacade.getUnitForUid(userGroupForm.getParentUnit()));
		}

		b2bUserGroupFacade.updateUserGroup(userGroupForm.getUid(), userGroupData);

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "form.b2busergroup.success");

		return String.format(REDIRECT_TO_USERGROUP_DETAILS, urlEncode(userGroupForm.getUid()));
	}
}
