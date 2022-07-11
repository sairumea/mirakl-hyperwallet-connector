package com.paypal.observability.miraklschemadiffs.service;

import com.paypal.observability.miraklschemadiffs.model.MiraklSchema;
import com.paypal.observability.miraklschemadiffs.model.MiraklSchemaItem;
import com.paypal.observability.miraklschemadiffs.model.diff.MiraklSchemaDiff;
import com.paypal.observability.miraklschemadiffs.model.diff.MiraklSchemaDiffEntry;
import com.paypal.observability.miraklschemadiffs.model.diffevaluators.MiraklSchemaDiffEvaluatorRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MiraklSchemaComparatorImpl implements MiraklSchemaComparator {

	private static final String HW_CUSTOMFIELD_PREFIX = "hw-";

	private final MiraklSchemaDiffEvaluatorRegistry miraklSchemaDiffEvaluatorRegistry;

	public MiraklSchemaComparatorImpl(MiraklSchemaDiffEvaluatorRegistry miraklSchemaDiffEvaluatorRegistry) {
		this.miraklSchemaDiffEvaluatorRegistry = miraklSchemaDiffEvaluatorRegistry;
	}

	@Override
	public MiraklSchemaDiff compareSchemas(MiraklSchema expected, MiraklSchema actual) {
		MiraklSchema filteredExpected = filterNonHwFields(expected);
		MiraklSchema filteredActual = filterNonHwFields(actual);

		MiraklSchemaDiff miraklSchemaDiff = new MiraklSchemaDiff(expected.getType());

		evaluateSetDiffs(miraklSchemaDiff, filteredExpected, filteredActual);
		evaluateFieldDiffs(miraklSchemaDiff, filteredExpected, filteredActual);

		return miraklSchemaDiff;
	}

	private MiraklSchema filterNonHwFields(MiraklSchema miraklSchema) {
		//@formatter:off
		return new MiraklSchema(
				miraklSchema.getItems().stream()
						.filter(f -> f.getCode().startsWith(HW_CUSTOMFIELD_PREFIX))
						.collect(Collectors.toList()), miraklSchema.getType());
		//@formatter:on
	}

	private void evaluateSetDiffs(MiraklSchemaDiff miraklSchemaDiff, MiraklSchema filteredExpected,
			MiraklSchema filteredActual) {
		//@formatter:off
		miraklSchemaDiffEvaluatorRegistry.getSetDiffEvaluators(miraklSchemaDiff).stream()
				.map(e -> e.getDifferences(filteredExpected, filteredActual))
				.forEach(miraklSchemaDiff::addDifferences);
		//@formatter:on
	}

	private void evaluateFieldDiffs(MiraklSchemaDiff miraklSchemaDiff, MiraklSchema expected, MiraklSchema actual) {
		Map<String, MiraklSchemaItem> expectedCustomFieldsMap = buildCustomFieldMap(expected);

		//@formatter:off
		actual.getItems().stream()
				.filter(f -> expectedCustomFieldsMap.containsKey(f.getCode()))
				.map(f -> evaluateFieldDiffs(expectedCustomFieldsMap.get(f.getCode()), f))
				.forEach(miraklSchemaDiff::addDifferences);
		//@formatter:on
	}

	private List<MiraklSchemaDiffEntry> evaluateFieldDiffs(MiraklSchemaItem expected, MiraklSchemaItem actual) {
		//@formatter:off
		return miraklSchemaDiffEvaluatorRegistry.getItemDiffEvaluators(expected).stream()
				.map(e -> e.check(expected, actual))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
		//@formatter:on
	}

	private Map<String, MiraklSchemaItem> buildCustomFieldMap(MiraklSchema miraklSchema) {
		return miraklSchema.getItems().stream()
				.collect(Collectors.toMap(MiraklSchemaItem::getCode, Function.identity()));
	}

}
