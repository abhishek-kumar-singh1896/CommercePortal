package com.braintree.controllers.form;


import com.enterprisewide.b2badvanceacceleratoraddon.forms.PlaceOrderForm;

import java.util.HashMap;
import java.util.Map;

public class BraintreePlaceOrderForm extends PlaceOrderForm{

    private Map<String, String> customFields = new HashMap<>();

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, String> customFields) {
        this.customFields = customFields;
    }
}
