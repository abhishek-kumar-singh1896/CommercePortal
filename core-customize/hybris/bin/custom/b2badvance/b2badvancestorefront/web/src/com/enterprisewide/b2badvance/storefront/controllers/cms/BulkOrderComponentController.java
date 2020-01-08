/**
 *
 */
package com.enterprisewide.b2badvance.storefront.controllers.cms;

import com.enterprisewide.b2badvance.cms.model.BulkOrderComponentModel;
import com.enterprisewide.b2badvance.facades.bulkorder.BulkOrderFacade;
import com.enterprisewide.b2badvance.facades.bulkorder.data.BulkOrderProductData;
import com.enterprisewide.b2badvance.storefront.controllers.ControllerConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author Enterprise Wide
 *
 */
@Controller("BulkOrderComponentController")
@RequestMapping("/view/BulkOrderComponentController")
public class BulkOrderComponentController extends AbstractCMSComponentController<BulkOrderComponentModel>
{

	@Autowired
	BulkOrderFacade bulkOrderFacade;


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController#fillModel(javax.servlet.http
	 * .HttpServletRequest, org.springframework.ui.Model,
	 * de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final BulkOrderComponentModel component)
	{
		final List<BulkOrderProductData> list = bulkOrderFacade.getBulkOrderList(component.getMaximumNumberRows());
		model.addAttribute("qoList", list);

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController#getView(de.hybris
	 * .platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected String getView(final BulkOrderComponentModel component)
	{
		// build a jsp response based on the component type
		return ControllerConstants.Views.Cms.ComponentPrefix + StringUtils.lowerCase(getTypeCode(component));
	}

}
