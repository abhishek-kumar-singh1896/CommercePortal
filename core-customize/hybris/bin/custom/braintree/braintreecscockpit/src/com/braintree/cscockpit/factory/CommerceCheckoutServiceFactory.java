package com.braintree.cscockpit.factory;

import com.braintree.configuration.service.BrainTreeConfigService;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class CommerceCheckoutServiceFactory{

    @Autowired
    private ApplicationContext appContext;

    private BrainTreeConfigService brainTreeConfigService;

    public CommerceCheckoutService getService() throws Exception {
        String b2bService = "b2bCommerceCheckoutService";
        String serviceBean = "commerceCheckoutService";

        CommerceCheckoutService checkoutService;
        try{
            checkoutService = (CommerceCheckoutService) appContext.getBean(b2bService);
        }catch( NoSuchBeanDefinitionException e ) {
            checkoutService = (CommerceCheckoutService) appContext.getBean(serviceBean);
        }

        return checkoutService;
    }

    public BrainTreeConfigService getBrainTreeConfigService() {
        return brainTreeConfigService;
    }

    public void setBrainTreeConfigService(BrainTreeConfigService brainTreeConfigService) {
        this.brainTreeConfigService = brainTreeConfigService;
    }
}
