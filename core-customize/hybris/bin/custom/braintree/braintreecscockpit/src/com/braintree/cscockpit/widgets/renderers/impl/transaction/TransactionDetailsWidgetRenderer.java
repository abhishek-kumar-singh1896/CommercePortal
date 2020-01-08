package com.braintree.cscockpit.widgets.renderers.impl.transaction;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.renderers.details.WidgetDetailRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import com.braintree.cscockpit.widgets.controllers.TransactionController;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;
import com.braintree.model.BrainTreeTransactionDetailModel;


public class TransactionDetailsWidgetRenderer extends
		AbstractCsWidgetRenderer<Widget<TransactionItemWidgetModel, TransactionController>>
{
	private WidgetDetailRenderer<TypedObject, Widget> detailRenderer;

	public TransactionDetailsWidgetRenderer()
	{
	}

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final HtmlBasedComponent htmlBasedComponent)
	{
		final Div content = new Div();

		final TypedObject transaction = widget.getWidgetController().getCurrentTransaction();
		if (transaction != null && transaction.getObject() instanceof BrainTreeTransactionDetailModel)
		{
			final HtmlBasedComponent dummyLabel1 = this.getDetailRenderer().createContent((Object) null, transaction, widget);
			if (dummyLabel1 != null)
			{
				dummyLabel1.setParent(content);
			}
		}
		else
		{
			final Label dummyLabel = new Label(LabelUtils.getLabel(widget, "noTransactionSelected", new Object[0]));
			dummyLabel.setParent(content);
		}

		return content;
	}

	protected WidgetDetailRenderer<TypedObject, Widget> getDetailRenderer()
	{
		return this.detailRenderer;
	}

	@Required
	public void setDetailRenderer(final WidgetDetailRenderer<TypedObject, Widget> detailRenderer)
	{
		this.detailRenderer = detailRenderer;
	}
}
