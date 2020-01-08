package de.hybris.platform.hac.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import braintreehac.services.PayPalButtonConfigurationService;

@Controller
@RequestMapping("/braintreehac")
public class PayPalButtonConfigurationController {

    private static final Logger LOG = Logger.getLogger(PayPalButtonConfigurationController.class);

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String CART_BUTTON_CONFIG = "cart.button.config";
    private static final String MARK_BUTTON_CONFIG = "mark.button.config";
    private static final String MINI_CART_BUTTON_CONFIG = "mini.cart.button.config";
    private static final String BUTTON_CONFIG_PLACEHOLDER = "button.config.placeholder";

    @Autowired
    private PayPalButtonConfigurationService payPalButtonConfigurationService;

    @RequestMapping(value = "/button/config", method = RequestMethod.GET)
    public String showButtonConfigs(final Model model) {

        model.addAttribute("cartButtonConfig", payPalButtonConfigurationService.getProperty(CART_BUTTON_CONFIG));
        model.addAttribute("markButtonConfig", payPalButtonConfigurationService.getProperty(MARK_BUTTON_CONFIG));
        model.addAttribute("miniCartButtonConfig", payPalButtonConfigurationService.getProperty(MINI_CART_BUTTON_CONFIG));
        model.addAttribute("placeholder", payPalButtonConfigurationService.getProperty(BUTTON_CONFIG_PLACEHOLDER));

        return "buttonConfig";
    }

    @RequestMapping(value = "/button/config/cart", method = RequestMethod.GET)
    public String saveCartButtonConfig(final Model model, @RequestParam("cartButtonConfig") final String cartButtonConfig) {

        payPalButtonConfigurationService.setProperty(CART_BUTTON_CONFIG, cartButtonConfig);
        payPalButtonConfigurationService.storeInPropertiesFile();

        return REDIRECT_PREFIX + "/braintreehac/button/config/";
    }

    @RequestMapping(value = "/button/config/minicart", method = RequestMethod.GET)
    public String saveMiniCartButtonConfig(final Model model, @RequestParam("miniCartButtonConfig") final String miniCartButtonConfig) {

        payPalButtonConfigurationService.setProperty(MINI_CART_BUTTON_CONFIG, miniCartButtonConfig);
        payPalButtonConfigurationService.storeInPropertiesFile();

        return REDIRECT_PREFIX + "/braintreehac/button/config/";
    }

    @RequestMapping(value = "/button/config/mark", method = RequestMethod.GET)
    public String saveMarkButtonConfig(final Model model, @RequestParam("markButtonConfig") final String markButtonConfig) {

        payPalButtonConfigurationService.setProperty(MARK_BUTTON_CONFIG, markButtonConfig);
        payPalButtonConfigurationService.storeInPropertiesFile();

        return REDIRECT_PREFIX + "/braintreehac/button/config/";
    }
}
