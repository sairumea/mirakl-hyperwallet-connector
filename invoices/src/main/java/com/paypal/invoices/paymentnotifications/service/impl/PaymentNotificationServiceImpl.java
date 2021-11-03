package com.paypal.invoices.paymentnotifications.service.impl;

import com.hyperwallet.clientsdk.model.HyperwalletWebhookNotification;
import com.paypal.infrastructure.converter.Converter;
import com.paypal.invoices.paymentnotifications.model.PaymentNotificationBodyModel;
import com.paypal.invoices.paymentnotifications.service.PaymentNotificationExecutor;
import com.paypal.invoices.paymentnotifications.service.PaymentNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentNotificationServiceImpl implements PaymentNotificationService {

	private final Converter<Object, PaymentNotificationBodyModel> hyperWalletObjectToPaymentNotificationBodyModelConverter;

	private final PaymentNotificationExecutor paymentNotificationExecutor;

	public PaymentNotificationServiceImpl(
			final Converter<Object, PaymentNotificationBodyModel> hyperWalletObjectToPaymentNotificationBodyModelConverter,
			final PaymentNotificationExecutor paymentNotificationExecutor) {
		this.hyperWalletObjectToPaymentNotificationBodyModelConverter = hyperWalletObjectToPaymentNotificationBodyModelConverter;
		this.paymentNotificationExecutor = paymentNotificationExecutor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processPaymentNotification(final HyperwalletWebhookNotification incomingNotificationDTO) {
		final PaymentNotificationBodyModel paymentNotificationBodyModel = hyperWalletObjectToPaymentNotificationBodyModelConverter
				.convert(incomingNotificationDTO.getObject());
		paymentNotificationExecutor.execute(paymentNotificationBodyModel);
	}

}
