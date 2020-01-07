package com.braintree.customersupportbackoffice.editor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.braintreegateway.Transaction;
import com.hybris.cockpitng.editor.defaultenum.DefaultEnumEditor;

public class TransactionStatusEditor extends DefaultEnumEditor {
    @Override
    protected List<Object> getAllValues(String valueType, Object initialValue) {
        return Arrays.stream(Transaction.Status.values()).collect(Collectors.toList());
    }
}
