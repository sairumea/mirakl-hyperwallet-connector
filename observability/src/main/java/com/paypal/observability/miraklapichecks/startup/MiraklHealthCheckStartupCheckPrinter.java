package com.paypal.observability.miraklapichecks.startup;

import com.paypal.observability.startupchecks.model.StartupCheck;
import com.paypal.observability.startupchecks.model.StartupCheckPrinter;
import com.paypal.observability.startupchecks.model.StartupCheckProvider;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

@Component
public class MiraklHealthCheckStartupCheckPrinter implements StartupCheckPrinter {

	@Override
	public String[] print(final StartupCheck check) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		check.getStatusMessage().ifPresent(printWriter::println);
		//@formatter:off
		printWriter.print(check.getDetails().entrySet().stream()
				.filter(x -> x.getValue() != null)
				.map(x -> String.format("%s: %s", x.getKey(), x.getValue()))
				.collect(Collectors.joining(System.lineSeparator())));
		//@formatter:on

		printWriter.flush();
		return new String[] { stringWriter.toString() };
	}

	@Override
	public Class<? extends StartupCheckProvider> getAssociatedStartupCheck() {
		return MiraklHealthCheckStartupProvider.class;
	}

}
