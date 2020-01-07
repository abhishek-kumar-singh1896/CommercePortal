/**
 *
 */
package com.enterprisewide.b2badvance.storefront.controllers.cms;

import com.enterprisewide.b2badvance.facades.product.comparison.B2badvanceProductComparisonFacade;
import com.enterprisewide.b2badvance.storefront.controllers.ControllerConstants;
import com.hybris.productcomparison.cms.model.B2badvanceProductComparisonComponentModel;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Enterprise Wide
 *
 */
@Controller("B2badvanceProductComparisonComponentController")
@RequestMapping("/view/B2badvanceProductComparisonComponentController")
public class B2badvanceProductComparisonComponentController extends
        AbstractCMSComponentController<B2badvanceProductComparisonComponentModel>
{
	@Autowired
	B2badvanceProductComparisonFacade productComparisonFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
                             final B2badvanceProductComparisonComponentModel component)
	{
		final List<String> codes = productComparisonFacade.getProductComparisonCodes();
		final Map<String, String> map = new HashMap<String, String>();
		for (final String code : codes)
		{
			map.put(code, code);
		}
		model.addAttribute("pcCodes", map);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController#getView(de.hybris
	 * .platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected String getView(final B2badvanceProductComparisonComponentModel component)
	{
		// build a jsp response based on the component type
		return ControllerConstants.Views.Cms.ComponentPrefix + StringUtils.lowerCase(getTypeCode(component));
	}
}
