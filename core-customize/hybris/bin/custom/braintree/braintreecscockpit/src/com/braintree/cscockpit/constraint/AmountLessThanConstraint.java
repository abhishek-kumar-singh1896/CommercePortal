package com.braintree.cscockpit.constraint;

import static org.zkoss.util.resource.Labels.getLabel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;

import java.math.BigDecimal;


public class AmountLessThanConstraint implements Constraint {

    private BigDecimal biggerValue;
    private AmountConstraint amountConstraint;

    public AmountLessThanConstraint(BigDecimal biggerValue) {
        this.biggerValue = biggerValue;
        amountConstraint = new AmountConstraint();
    }

    @Override
    public void validate(final Component paramComponent, final Object paramObject) throws WrongValueException {
        amountConstraint.validate(paramComponent, paramObject);
        final String amount = (String) paramObject;
        if (amount != null && !amount.isEmpty()) {
            BigDecimal current = new BigDecimal(amount);
            if (biggerValue.compareTo(current) < 0) {
                throw new WrongValueException(paramComponent, getLabel("validation.incorrect.amount"));
            }
        }
    }
}
