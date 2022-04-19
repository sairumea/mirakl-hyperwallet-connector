package com.paypal.invoices.invoicesextract.service.hmc.impl;

import com.hyperwallet.clientsdk.model.HyperwalletPayment;
import com.paypal.invoices.infraestructure.testing.TestingInvoicesSessionDataHelper;
import com.paypal.invoices.invoicesextract.model.InvoiceModel;
import com.paypal.invoices.invoicesextract.service.hmc.InvoiceProcessService;
import com.paypal.invoices.invoicesextract.service.hyperwallet.HyperWalletPaymentExtractService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Mock implementation of {@link InvoiceProcessService}.
 */
@Service("invoiceProcessService")
@Profile({ "qa" })
public class InvoiceProcessServiceMock extends AbstractInvoiceProcessService {

	private final TestingInvoicesSessionDataHelper testingInvoicesSessionDataHelper;

	protected InvoiceProcessServiceMock(final HyperWalletPaymentExtractService hyperWalletPaymentExtractService,
			final TestingInvoicesSessionDataHelper testingInvoicesSessionDataHelper) {
		super(hyperWalletPaymentExtractService);
		this.testingInvoicesSessionDataHelper = testingInvoicesSessionDataHelper;
	}

	/**
	 * {@inheritDoc}
	 */
	public HyperwalletPayment payOperator(InvoiceModel invoice) {

		HyperwalletPayment operatorPayment = null;
		if (testingInvoicesSessionDataHelper.isOperatorCommissionsEnabled()) {
			operatorPayment = super.hyperWalletPaymentExtractService.payInvoiceOperator(invoice);
		}
		return operatorPayment;
	}

}