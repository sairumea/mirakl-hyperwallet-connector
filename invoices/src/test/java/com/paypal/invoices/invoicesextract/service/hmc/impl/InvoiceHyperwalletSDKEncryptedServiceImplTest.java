package com.paypal.invoices.invoicesextract.service.hmc.impl;

import com.hyperwallet.clientsdk.Hyperwallet;
import com.hyperwallet.clientsdk.util.HyperwalletEncryption;
import com.paypal.invoices.infraestructure.configuration.InvoicesHyperwalletApiConfig;
import com.paypal.invoices.invoicesextract.service.hyperwallet.impl.InvoiceHyperwalletSDKEncryptedServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceHyperwalletSDKEncryptedServiceImplTest {

	private static final String PROGRAM_TOKEN = "programToken";

	private static final String USER_NAME = "userName";

	private static final String PASSWORD = "password";

	private static final String SERVER = "server";

	private static final String ISSUING_STORE = "issuingStore";

	private static final String API_CLIENT_HYPERWALLET_ENCRYPTION = "apiClient.hyperwalletEncryption";

	@Spy
	@InjectMocks
	private InvoiceHyperwalletSDKEncryptedServiceImpl testObj;

	@Mock
	private HyperwalletEncryption hyperwalletEncryptionMock;

	@Mock
	private InvoicesHyperwalletApiConfig invoicesHyperwalletApiConfigMock;

	@Test
	void getHyperwalletInstanceWithProgramToken_shouldReturnAnHyperwalletInstanceWithEncryptedOption() {
		when(invoicesHyperwalletApiConfigMock.getUsername()).thenReturn(USER_NAME);
		when(invoicesHyperwalletApiConfigMock.getPassword()).thenReturn(PASSWORD);
		when(invoicesHyperwalletApiConfigMock.getServer()).thenReturn(SERVER);

		final Hyperwallet result = testObj.getHyperwalletInstanceWithProgramToken(PROGRAM_TOKEN);

		assertThat(result).hasFieldOrPropertyWithValue(API_CLIENT_HYPERWALLET_ENCRYPTION, hyperwalletEncryptionMock);
	}

	@Test
	void getHyperwalletInstanceByIssuingStore_shouldReturnAnHyperwalletInstance() {
		when(invoicesHyperwalletApiConfigMock.getPaymentStoreTokens()).thenReturn(Map.of(ISSUING_STORE, PROGRAM_TOKEN));
		when(invoicesHyperwalletApiConfigMock.getUsername()).thenReturn(USER_NAME);
		when(invoicesHyperwalletApiConfigMock.getPassword()).thenReturn(PASSWORD);
		when(invoicesHyperwalletApiConfigMock.getServer()).thenReturn(SERVER);
		when(invoicesHyperwalletApiConfigMock.getPaymentStoreTokens()).thenReturn(Map.of(ISSUING_STORE, PROGRAM_TOKEN));
		when(invoicesHyperwalletApiConfigMock.getUsername()).thenReturn(USER_NAME);
		when(invoicesHyperwalletApiConfigMock.getPassword()).thenReturn(PASSWORD);
		when(invoicesHyperwalletApiConfigMock.getServer()).thenReturn(SERVER);

		final Hyperwallet result = testObj.getHyperwalletInstanceByHyperwalletProgram(ISSUING_STORE);

		assertThat(result).hasFieldOrPropertyWithValue("programToken", PROGRAM_TOKEN)
				.hasFieldOrPropertyWithValue("apiClient.username", USER_NAME)
				.hasFieldOrPropertyWithValue("url", SERVER + "/rest/v4");
	}

}
