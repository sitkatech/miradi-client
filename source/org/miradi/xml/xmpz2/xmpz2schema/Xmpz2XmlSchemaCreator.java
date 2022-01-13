/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.main.Miradi;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.*;
import org.miradi.utils.*;
import org.miradi.xml.xmpz2.Xmpz2TagToElementNameMap;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

import java.util.*;

public class Xmpz2XmlSchemaCreator implements Xmpz2XmlConstants
{
	public static void main(String[] args) throws Exception
	{
		Miradi.addThirdPartyJarsToClasspath();
		Translation.initialize();

		Xmpz2SchemaWriter writer = new Xmpz2SchemaWriter(System.out);
		Xmpz2XmlSchemaCreator creator = new Xmpz2XmlSchemaCreator(writer);
		
		creator.writeRncSchema();
		creator.getSchemaWriter().flush();
	}

	public Xmpz2XmlSchemaCreator(Xmpz2SchemaWriter writerToUse) throws Exception
	{
		writer = writerToUse;
		tagToElementNameMap = new Xmpz2TagToElementNameMap();
		choiceQuestionToSchemaElementNameMap = new ChoiceQuestionToSchemaElementNameMap();
		project = new Project();
		baseObjectSchemaWriters = getTopLevelBaseObjectSchemas();
		codelistSchemaElements = new Vector<String>();
	}

	public void writeRncSchema() throws Exception
	{
		Vector<Xmpz2CustomSchemaDefinitionCreator> creators = new Vector<Xmpz2CustomSchemaDefinitionCreator>(); 
		writeHeader();
		writeConservationProjectElement();
		writeSingletonElements();
		writeBaseObjectSchemaElements();
		writeCodelistSchemaElements();
		writeVocabularyDefinitions();
		writeUriRestrictedSchemaElement();
		writeNonEmptyStringSchemaElement();
		writeUUIDSchemaElement();
		writeObjectTypeIdElements();
		writeWrappedByDiagramFactorSchemaElement();
		writeLinkableFactorIds();
		writeDiagramIds();
		writeHtmlTagSchemaElements();
		writeGeospacialLocationElement();
		writeDiagramPointElement();
		writeDiagramSizeElement();
		writeDateUnitSchemaElements();
		writeMiradiShareTaxonomyAssociationsPools();
		writeMiradiShareAccountingClassificationAssociationsPools();
		writeTaxonomyElementCode();
		creators.add(createExporterDetailsElementCreator());
		creators.add(creatorThresholdsElementSchemaCreator());
		creators.add(createTimePeriodCostsElementSchemaCreator());
		creators.add(createCalculatedTimeframeElementSchemaCreator());
		creators.add(createExpenseEntryElementSchemaCreator());
		creators.add(createWorkUnitsEntryElementSchemaCreator());
		creators.add(createExternalIdSchemaElementSchemaCreator());
		creators.add(createDiagramFactorUiSettingsSchemaCreator());
		creators.add(createDashboardUserChoiceMapSchemaCreator());
		creators.add(createExtraDataElement());
		creators.add(writeExtraDataSectionElement());
		creators.add(defineExtraDataItemElement());
		creators.add(defineThreatStressRatingElement());
		creators.add(defineTaxonomyAssociationsElement());
		creators.add(defineTaxonomyClassificationContainerElement());
		creators.add(defineTaxonomyElement());
		creators.add(defineAccountingClassificationAssociationsElement());
		creators.add(defineAccountingClassificationContainerElement());

		for(Xmpz2CustomSchemaDefinitionCreator creator : creators)
		{
			getSchemaWriter().write(creator.createSchemaElement());
		}
		
		writeDashboardFlagsContainer();
		writeTaxonomyClassificationTaxonomyElementCodeContainer();
		writeAccountingClassificationTaxonomyElementCodeContainer();
		writeTaxonomyElementChildElementCodeContainer();
	}

	private void writeMiradiShareTaxonomyAssociationsPools()
	{
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(MIRADI_SHARE_PROJECT_DATA_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(BIODIVERSITY_TARGET_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(HUMAN_WELLBEING_TARGET_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(BIOPHYSICAL_FACTOR_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(BIOPHYSICAL_RESULT_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(CONTRIBUTING_FACTOR_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(DIRECT_THREAT_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(INTERMEDIATE_RESULT_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(STRATEGY_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(RESULTS_CHAIN_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(CONCEPTUAL_MODEL_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(THREAT_REDUCTION_RESULT_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(GOAL_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(KEY_ECOLOGICAL_ATTRIBUTE_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(INDICATOR_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(METHOD_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(OBJECTIVE_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(STRESS_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(TASK_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(PROJECT_RESOURCE_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(RESOURCE_ASSIGNMENT_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(EXPENSE_ASSIGNMENT_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(OUTPUT_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(ANALYTICAL_QUESTION_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(ASSUMPTION_TAXONOMY_ASSOCIATION_POOL, TAXONOMY_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(TAXONOMY_CLASSIFICATION_CONTAINER, TAXONOMY_CLASSIFICATION);
	}

	private void writeMiradiShareAccountingClassificationAssociationsPools()
	{
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(RESOURCE_ASSIGNMENT_ACCOUNTING_CLASSIFICATION_ASSOCIATION_POOL, ACCOUNTING_CLASSIFICATION_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(EXPENSE_ASSIGNMENT_ACCOUNTING_CLASSIFICATION_ASSOCIATION_POOL, ACCOUNTING_CLASSIFICATION_ASSOCIATION);
		getSchemaWriter().writeElementWithZeroOrMoreDotElementType(ACCOUNTING_CLASSIFICATION_CONTAINER, ACCOUNTING_CLASSIFICATION);
	}

	private void writeHeader()
	{
		getSchemaWriter().writeHeaderComments();
		getSchemaWriter().writeNamespace(NAME_SPACE);
		getSchemaWriter().defineAlias("start", createElementName(CONSERVATION_PROJECT));
	}

	private void writeConservationProjectElement()
	{
		getSchemaWriter().startElementDefinition(CONSERVATION_PROJECT);

		Vector<String> elementNames = new Vector<String>();
		elementNames.add(createElementName(EXPORT_DETAILS));
		elementNames.add(createElementName(PROJECT_SUMMARY));
		elementNames.add(createElementName(PROJECT_SUMMARY_SCOPE));
		elementNames.add(createElementName(PROJECT_SUMMARY_LOCATION));
		elementNames.add(createElementName(PROJECT_SUMMARY_PLANNING));
		elementNames.add(createOptionalSchemaElement(TNC_PROJECT_DATA));
		elementNames.add(createOptionalSchemaElement(WWF_PROJECT_DATA));
		elementNames.add(createOptionalSchemaElement(WCS_PROJECT_DATA));
		elementNames.add(createOptionalSchemaElement(RARE_PROJECT_DATA));
		elementNames.add(createOptionalSchemaElement(FOS_PROJECT_DATA));
		elementNames.add(createElementName(EXTRA_DATA));
		elementNames.add(createOptionalSchemaElement(MIRADI_SHARE_PROJECT_DATA));
		for(BaseObjectSchemaWriter baseObjectSchemaWriter : baseObjectSchemaWriters)
		{
			String poolName = baseObjectSchemaWriter.getPoolName();
			elementNames.add(createOptionalSchemaElement(poolName));
		}
		
		elementNames.add(createOptionalSchemaElement(MIRADI_SHARE_PROJECT_DATA_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(BIODIVERSITY_TARGET_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(HUMAN_WELLBEING_TARGET_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(BIOPHYSICAL_FACTOR_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(BIOPHYSICAL_RESULT_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(CONTRIBUTING_FACTOR_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(DIRECT_THREAT_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(INTERMEDIATE_RESULT_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(STRATEGY_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(RESULTS_CHAIN_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(CONCEPTUAL_MODEL_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(THREAT_REDUCTION_RESULT_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(GOAL_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(KEY_ECOLOGICAL_ATTRIBUTE_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(INDICATOR_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(METHOD_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(OBJECTIVE_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(STRESS_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(TASK_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(PROJECT_RESOURCE_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(RESOURCE_ASSIGNMENT_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(RESOURCE_ASSIGNMENT_ACCOUNTING_CLASSIFICATION_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(EXPENSE_ASSIGNMENT_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(EXPENSE_ASSIGNMENT_ACCOUNTING_CLASSIFICATION_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(OUTPUT_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(ANALYTICAL_QUESTION_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(createOptionalSchemaElement(ASSUMPTION_TAXONOMY_ASSOCIATION_POOL));
		elementNames.add(ELEMENT_NAME + PREFIX + DELETED_ORPHANS_ELEMENT_NAME +  "{ text }?");
		getSchemaWriter().defineElements(elementNames);
		
		getSchemaWriter().endElementDefinition(CONSERVATION_PROJECT);
		getSchemaWriter().flush();
	}

	private String createOptionalSchemaElement(String poolName)
	{
		return getSchemaWriter().createOptionalSchemaElement(createElementName(poolName));
	}
	
	private void writeSingletonElements() throws Exception
	{
		writeSingletonObjectSchema(new ProjectSummarySchema());
		writeSingletonObjectSchema(new ProjectSummaryScopeSchema());
		writeSingletonObjectSchema(new ProjectSummaryLocationSchemaWriter(this, new ProjectSummaryLocationSchema()));
		writeSingletonObjectSchema(new ProjectSummaryPlanningSchema());
		writeSingletonObjectSchema(new ProjectSummaryTncProjectDataSchema());
		writeSingletonObjectSchema(new WwfProjectDataSchema());
		writeSingletonObjectSchema(new WcsProjectDataSchema());
		writeSingletonObjectSchema(new FosProjectDataSchema());
		writeSingletonObjectSchema(new RareProjectDataSchema());
		writeSingletonObjectSchema(new MiradiShareProjectDataSchemaWriter(this, new MiradiShareProjectDataSchema()));
	}
	
	private void writeSingletonObjectSchema(BaseObjectSchema baseObjectSchema) throws Exception
	{
		final SingletonSchemaWriter baseObjectSchemaWriter = new SingletonSchemaWriter(this, baseObjectSchema);
		writeSingletonObjectSchema(baseObjectSchemaWriter);
	}

	private void writeSingletonObjectSchema(final SingletonSchemaWriter baseObjectSchemaWriter) throws Exception
	{
		getSchemaWriter().startElementDefinition(baseObjectSchemaWriter.getXmpz2ElementName());
		writeElementContent(baseObjectSchemaWriter);
		getSchemaWriter().endBlock();
	}

	private void writeBaseObjectSchemaElements() throws Exception
	{
		for(BaseObjectSchemaWriter baseObjectSchemaWriter : baseObjectSchemaWriters)
		{
			writeBaseObjectSchema(baseObjectSchemaWriter);
		}		
	}

	private void writeBaseObjectSchema(BaseObjectSchemaWriter baseObjectSchemaWriter) throws Exception
	{
		writeBaseObjectSchemaHeader(baseObjectSchemaWriter);
		getSchemaWriter().startBlock();
		writeElementContent(baseObjectSchemaWriter);
		getSchemaWriter().endBlock();
	}

	private void writeElementContent(BaseObjectSchemaWriter baseObjectSchemaWriter) throws Exception
	{
		Vector<String> schemaFieldElements = baseObjectSchemaWriter.createFieldSchemas();
		Collections.sort(schemaFieldElements, new IgnoreCaseStringComparator());
		getSchemaWriter().defineElements(schemaFieldElements);
	}
	
	private String getRelevantTypeName(AbstractFieldSchema fieldSchema)
	{
		if (fieldSchema.getTag().equals(Desire.TAG_RELEVANT_INDICATOR_SET))
			return INDICATOR;

		if (fieldSchema.getTag().equals(Output.TAG_GOAL_IDS))
			return GOAL;

		if (fieldSchema.getTag().equals(Output.TAG_OBJECTIVE_IDS))
			return OBJECTIVE;

		if (fieldSchema.getTag().equals(Output.TAG_INDICATOR_IDS))
			return INDICATOR;

		if (fieldSchema.getTag().equals(AbstractAnalyticalQuestion.TAG_INDICATOR_IDS))
			return INDICATOR;

		if (fieldSchema.getTag().equals(AbstractAnalyticalQuestion.TAG_DIAGRAM_FACTOR_IDS))
			return DIAGRAM_FACTOR;

		if (fieldSchema.getTag().equals(RELEVANT_STRATEGY_IDS))
			return StrategySchema.OBJECT_NAME;
		
		if (fieldSchema.getTag().equals(RELEVANT_ACTIVITY_IDS))
			return ACTIVITY;
		
		throw new RuntimeException("FieldSchema is not a relevancy field: " + fieldSchema.getTag());
	}
	
	private void writeCodelistSchemaElements()
	{
		for (String codelistSchemaElement : codelistSchemaElements)
		{
			getSchemaWriter().printIndented(codelistSchemaElement);
			getSchemaWriter().println();
		}
	}
	
	public String createDimensionSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createSchemaElement(baseObjectSchema, fieldSchema, DIAGRAM_SIZE_ELEMENT_NAME);
	}
	
	public String createDiagramPointSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createSchemaElement(baseObjectSchema, fieldSchema, DIAGRAM_POINT_ELEMENT_NAME);
	}
	
	public String createPointListElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createSchemaElement(baseObjectSchema, fieldSchema, DIAGRAM_POINT_ELEMENT_NAME);
	}
	
	public String writeCalculatedCostSchemaElement(BaseObjectSchema baseObjectSchema)
	{
		return getSchemaWriter().createOptionalSchemaElement(baseObjectSchema.getXmpz2ElementName() + TIME_PERIOD_COSTS, createElementName(TIME_PERIOD_COSTS));
	}
	
	public String writeCalculatedTimeframeSchemaElement(BaseObjectSchema baseObjectSchema)
	{
		return getSchemaWriter().createOptionalSchemaElement(baseObjectSchema.getXmpz2ElementName() + TIMEFRAME, createElementName(CALCULATED_TIMEFRAME));
	}

	public String createThresholdsSchemaElement(BaseObjectSchema baseObjectSchema)
	{
		return getSchemaWriter().createOptionalSchemaElement(baseObjectSchema.getXmpz2ElementName() + THRESHOLDS, createElementName(THRESHOLD) +  "*");
	}
	
	public String createStringRefMapSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createSchemaElement(baseObjectSchema, fieldSchema, EXTERNAL_PROJECT_ID_ELEMENT_NAME);
	}
	
	public String createElementName(String elementName)
	{
		return getSchemaWriter().createElementName(elementName);
	}
	
	public String createStringSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createElementSchema(baseObjectSchema, fieldSchema, TEXT_ELEMENT_TYPE);
	}

	public String createUUIDSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createRequiredElementSchema(baseObjectSchema, fieldSchema, UUID);
	}

	public String createUserTextSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createElementSchema(baseObjectSchema, fieldSchema, FORMATTED_TEXT_TYPE);
	}
	
	public String createBooleanSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createElementSchema(baseObjectSchema, fieldSchema, getSchemaWriter().createBooleanType());
	}

	public String createNumberSchemaElement(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema)
	{
		return createElementSchema(baseObjectSchema, fieldSchema, getSchemaWriter().createDecimalType());
	}
	
	public String createIntegerSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createElementSchema(baseObjectSchema, fieldSchema, getSchemaWriter().createIntegerType());
	}
	
	public String createDateSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createElementSchema(baseObjectSchema, fieldSchema, VOCABULARY_DATE);
	}
	
	public String createOptionalRefSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		String fieldName = getIdElementName(baseObjectSchema, fieldSchema);
		return createOptionalSchemaElement(baseObjectSchema, fieldSchema, fieldName);
	}
	
	public String createRequiredRefSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		String fieldName = getIdElementName(baseObjectSchema, fieldSchema);
		return createRequiredSchemaElement(baseObjectSchema, fieldSchema, fieldName);
	}

	public String createRequiredBaseIdSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, int objectType)
	{
		String objectName = getIdElementName(baseObjectSchema, fieldSchema, objectType);
		return createRequiredSchemaElement(baseObjectSchema, fieldSchema, createIdName(objectName));
	}
	
	public String createOptionalBaseIdSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, int objectType)
	{
		String objectName = getIdElementName(baseObjectSchema, fieldSchema, objectType);
		return createOptionalSchemaElement(baseObjectSchema, fieldSchema, createIdName(objectName));
	}

	public String createDateUnitEffortListSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return createSchemaElement(baseObjectSchema, fieldSchema, DATE_UNIT + getDateUnitTypeName(baseObjectSchema.getType()));
	}
	
	public String createChoiceSchemaElement(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, ChoiceQuestion choiceQuestion)
	{
		String vocabularyName = getChoiceQuestionToSchemaElementNameMap().findVocabulary(choiceQuestion);
		if (fieldSchema.isRequired())
			return createRequiredElementSchema(baseObjectSchema, fieldSchema, vocabularyName);
		
		return createElementSchema(baseObjectSchema, fieldSchema, vocabularyName);
	}
	
	public String createIdListSchemaElement(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema)
	{
		return createRefListSchemaElement(baseObjectSchema, fieldSchema);
	}

	public String createRefListSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		String elementName = getTagToElementNameMap().findElementName(baseObjectSchema.getXmpz2ElementName(), fieldSchema.getTag());
		String reflistElementName = baseObjectSchema.getXmpz2ElementName() + elementName;
		final String idElementName = createIdElementName(baseObjectSchema, fieldSchema, elementName);
		return getSchemaWriter().createOptionalParentAndZeroOrMoreElementDefinition(reflistElementName, idElementName);
	}

	public String createCodelistSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, ChoiceQuestion question)
	{
		String elementName = getTagToElementNameMap().findElementName(baseObjectSchema.getXmpz2ElementName(), fieldSchema.getTag());
		String codelistElementName = baseObjectSchema.getXmpz2ElementName() + elementName;
		
		codelistSchemaElements.add(createCodelistSchemaElement(codelistElementName, question));
		
		return createContainerElement(codelistElementName);
	}

	private String createContainerElement(String codelistElementName)
	{
		return createContainerName(codelistElementName) + ".element ?";
	}
	
	private String createCodelistSchemaElement(String codelistElementName, ChoiceQuestion question)
	{
		final String vocabularyName = getVocabularyName(question);
		final String createContainerName = createContainerName(codelistElementName);
		String containerElement = getSchemaWriter().createElementDefinition(createContainerName) + StringUtilities.NEW_LINE;
		containerElement += "{" + StringUtilities.NEW_LINE;
		containerElement += getSchemaWriter().createSchemaElement("code", vocabularyName) + " *" + StringUtilities.NEW_LINE;
		containerElement += "}" + StringUtilities.NEW_LINE;
		
		return containerElement;
	}

	private String getVocabularyName(ChoiceQuestion question)
	{
		return getChoiceQuestionToSchemaElementNameMap().findVocabulary(question);
	}
	
	private String createContainerName(String elementName)
	{
		return Xmpz2XmlWriter.createContainerElementName(elementName);
	}

	public String createRelevantSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		final String relevantTypeName = createIdName(getRelevantTypeName(fieldSchema));
		final String elementType = getSchemaWriter().createZeroOrMoreDotElement(relevantTypeName);
		return createElementSchema(baseObjectSchema, fieldSchema, elementType);
	}
	
	private void writeVocabularyDefinitions()
	{
		Set<ChoiceQuestion> choiceQuestions = choiceQuestionToSchemaElementNameMap.keySet();
		Vector<ChoiceQuestion> sortedQuestions = new Vector<ChoiceQuestion>(choiceQuestions);
		Collections.sort(sortedQuestions);
		for(ChoiceQuestion question : sortedQuestions)
		{
			String vocabularyName = choiceQuestionToSchemaElementNameMap.get(question);
			if (shouldOmitQuestion(question.getClass()))
				continue;
				
			if (isCustomWrittenVocabulary(vocabularyName))
				continue;
			
			defineVocabulary(question, vocabularyName);
		}
		
		getSchemaWriter().println("vocabulary_full_project_timespan = xsd:NMTOKEN { pattern = 'Total' }");
		getSchemaWriter().println("vocabulary_year = xsd:NMTOKEN { pattern = '[0-9]{4}' }");
		getSchemaWriter().println("vocabulary_month = xsd:integer { minInclusive='1' maxInclusive='12' }");
		getSchemaWriter().println("vocabulary_date = xsd:NMTOKEN { pattern = '[0-9]{4}-[0-9]{2}-[0-9]{2}' }");
		writeLanguageVocabulary();
		writeTncTerrestrialEcoregionVocabulary();
        getSchemaWriter().println("vocabulary_diagram_factor_header_height = xsd:integer { minInclusive='0' maxInclusive='9' }");
	}

	private boolean shouldOmitQuestion(Class<? extends ChoiceQuestion> questionClass)
	{
		// evidence questions share vocabulary with base class impls so only omit once for each type
		if (questionClass.getSuperclass().equals(EvidenceConfidenceTypeExternalQuestion.class))
			return !questionClass.equals(DesireEvidenceConfidenceQuestion.class);

		if (questionClass.getSuperclass().equals(EvidenceConfidenceTypeProjectQuestion.class))
			return !questionClass.equals(MeasurementEvidenceConfidenceQuestion.class);

		if (questionClass.equals(InternalQuestionWithoutValues.class))
			return true;

		return false;
	}

	private boolean isCustomWrittenVocabulary(String vocabularyName)
	{
		if (vocabularyName.equals(VOCABULARY_TNC_TERRESTRIAL_ECO_REGION))
			return true;

		return vocabularyName.equals(VOCABULARY_LANGUAGE_CODE);
	}
	
	private void writeLanguageVocabulary()
	{
		getSchemaWriter().println("vocabulary_language_code = xsd:NMTOKEN { pattern = '([a-z][a-z][a-z])|"+ getOredElements(new TwoLetterLanguagesQuestion().getAllCodes())+ "' }");
	}
	
	private void writeTncTerrestrialEcoregionVocabulary()
	{
		TncTerrestrialEcoRegionQuestion question = new TncTerrestrialEcoRegionQuestion();
		CodeList allCodes = question.getAllCodes();
		Vector<String> codes = allCodes.toVector();
		int almostHalf = codes.size() / 2;
		Vector<String> subList1 = new Vector<String>(codes.subList(0, almostHalf));
		Vector<String> subList2 = new Vector<String>(codes);
		subList2.removeAll(subList1);

		getSchemaWriter().println(VOCABULARY_TNC_TERRESTRIAL_ECO_REGION  + " = " + VOCABULARY_TNC_TERRESTRIAL_ECO_REGION_SUBSET_1 + "|" + VOCABULARY_TNC_TERRESTRIAL_ECO_REGION_SUBSET_2);
		getSchemaWriter().println(VOCABULARY_TNC_TERRESTRIAL_ECO_REGION_SUBSET_1 + " = " + StringUtilities.joinWithOr(subList1, "'", "'"));
		getSchemaWriter().println(VOCABULARY_TNC_TERRESTRIAL_ECO_REGION_SUBSET_2 + " = " + StringUtilities.joinWithOr(subList2, "'", "'"));
	}

	private String getOredElements(CodeList allCodes)
	{
		return StringUtilities.joinWithOr(allCodes.toArray());
	}

	private void defineVocabulary(final String elementName, final CodeList allCodesFromDynamicQuestion)
	{
		defineVocabulary(allCodesFromDynamicQuestion, new Vector<String>(), elementName);
	}
	
	private void defineVocabulary(CodeList allCodesFromDynamicQuestion, Vector<String> elements, final String elementName)
	{
		getSchemaWriter().print(elementName + " = ");
		for(int index = 0; index < allCodesFromDynamicQuestion.size(); ++index)
		{
			String code = allCodesFromDynamicQuestion.get(index);
			elements.add("'" + code + "'");
		}
		
		getSchemaWriter().writeOredElements(elements);
	}

	private void writeObjectTypeIdElements()
	{
		final Vector<String> objectTypeNames = Xmpz2GroupedConstants.getObjectTypeNamesToCreateIdSchemaElements();
		for (String objectTypeName : objectTypeNames)
		{
			writeIdElement(objectTypeName);
		}
	}

	private String createIdElementName(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String elementName)
	{
		if (isFieldForType(baseObjectSchema, fieldSchema, OutputSchema.getObjectType(), Output.TAG_GOAL_IDS))
			return createIdName(GOAL);

		if (isFieldForType(baseObjectSchema, fieldSchema, OutputSchema.getObjectType(), Output.TAG_OBJECTIVE_IDS))
			return createIdName(OBJECTIVE);

		if (isFieldForType(baseObjectSchema, fieldSchema, OutputSchema.getObjectType(), Output.TAG_INDICATOR_IDS))
			return createIdName(INDICATOR);

		if (isFieldForType(baseObjectSchema, fieldSchema, AnalyticalQuestionSchema.getObjectType(), AnalyticalQuestion.TAG_ASSUMPTION_IDS))
			return createIdName(ASSUMPTION);

		Vector<Integer> analyticalQuestionSchemas = new Vector<Integer>();
		analyticalQuestionSchemas.add(AnalyticalQuestionSchema.getObjectType());
		analyticalQuestionSchemas.add(AssumptionSchema.getObjectType());

		if (isFieldForType(baseObjectSchema, fieldSchema, analyticalQuestionSchemas, AbstractAnalyticalQuestion.TAG_DIAGRAM_FACTOR_IDS))
			return createIdName(DIAGRAM_FACTOR);

		if (isFieldForType(baseObjectSchema, fieldSchema, analyticalQuestionSchemas, AbstractAnalyticalQuestion.TAG_INDICATOR_IDS))
			return createIdName(INDICATOR);

		if (isFieldForType(baseObjectSchema, fieldSchema, TaskSchema.getObjectType(), Task.TAG_SUBTASK_IDS))
			return createIdName(SUB_TASK);

		if (isFieldForType(baseObjectSchema, fieldSchema, IndicatorSchema.getObjectType(), Indicator.TAG_METHOD_IDS))
			return createIdName(METHOD );
		
		if (isFieldForType(baseObjectSchema, fieldSchema, StrategySchema.getObjectType(), Strategy.TAG_ACTIVITY_IDS))
			return createIdName(ACTIVITY );
		
		if (isFieldForType(baseObjectSchema, fieldSchema, DiagramLinkSchema.getObjectType(), DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS))
			return createIdName(DIAGRAM_LINK);
		
		if (isFieldForType(baseObjectSchema, fieldSchema, DiagramFactorSchema.getObjectType(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS))
			return createIdName(DIAGRAM_FACTOR);
		
		if (DiagramObject.isDiagramObject(baseObjectSchema.getType()) && fieldSchema.getTag().equals(DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS))
			return createIdName(TAGGED_OBJECT_SET_ELEMENT_NAME);

		if (isFieldForType(baseObjectSchema, fieldSchema, DiagramFactorSchema.getObjectType(), DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS))
			return createIdName(TAGGED_OBJECT_SET_ELEMENT_NAME);

		return StringUtilities.removeLastChar(elementName);
	}
	
	private boolean isFieldForType(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, Vector<Integer> objectTypes, String tag)
	{
		for(Integer objectType: objectTypes)
		{
			if (isFieldForType(baseObjectSchema, fieldSchema, (int)objectType, tag))
				return true;
		}

		return false;
	}

	private boolean isFieldForType(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, int objectType, String tag)
	{
		if (objectType == baseObjectSchema.getType() && fieldSchema.getTag().equals(tag))
			return true;

		return false;
	}

	private String getIdElementName(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		if (DiagramFactor.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(DiagramFactor.TAG_WRAPPED_REF))
			return WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME;
		
		if (ThreatReductionResult.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF))
			return THREAT_ID;
		
		return getTagToElementNameMap().findElementName(baseObjectSchema, fieldSchema);
	}
	
	private String getIdElementName(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, int objectType)
	{
		if (ResourceAssignment.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(ResourceAssignment.TAG_RESOURCE_ID))
			return RESOURCE;
		
		if (DiagramLink.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID))
			return LINKABLE_FACTOR;
		
		if (DiagramLink.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID))
			return LINKABLE_FACTOR;
	
		return getProject().getObjectManager().getInternalObjectTypeName(objectType);
	}
	
	private void writeIdElement(String baseName)
	{
		getSchemaWriter().writeElement(createIdName(baseName));
	}

	private void writeBaseObjectSchemaHeader(BaseObjectSchemaWriter baseObjectSchemaWriter)
	{
		String baseObjectName = baseObjectSchemaWriter.getXmpz2ElementName();
		String baseObjectPoolName = baseObjectSchemaWriter.getPoolName();
		
		String result = getSchemaWriter().createZeroOrMoreSchemaElement(baseObjectPoolName, createElementName(baseObjectName));
		getSchemaWriter().defineAlias(createElementName(baseObjectPoolName), result);
		getSchemaWriter().defineAlias(createElementName(baseObjectName), ELEMENT_NAME + PREFIX + baseObjectName);
	}

	private void writeLinkableFactorIds()
	{
		String[] factorNames = Xmpz2GroupedConstants.getLinkableFactorNames();
		
		writeOredSchemaElements(LINKABLE_FACTOR_ID, factorNames);
	}

	private void writeDiagramIds()
	{
		String[] diagramNames = Xmpz2GroupedConstants.getDiagramNames();

		writeOredSchemaElements(DIAGRAM_ID, diagramNames);
	}

	private void writeWrappedByDiagramFactorSchemaElement()
	{
		String[] factorNames = Xmpz2GroupedConstants.getWrappableFactorNames();

		writeOredSchemaElements(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME, factorNames);
	}

	private void defineVocabulary(ChoiceQuestion question, String vocabularyName)
	{
		CodeList codes = question.getCodesAsReadableCodes();
		defineVocabulary(vocabularyName, codes);
	}
	
	private void writeOredSchemaElements(final String parentElementName, final String[] elementNamesAsArray)
	{
		Vector<String> elementNames = Utility.convertToVector(elementNamesAsArray);
		getSchemaWriter().startElementDefinition(parentElementName);
		Vector<String> elements = new Vector<String>();
		for (String elementName : elementNames)
		{
			elements.add(createElementName(createIdName(elementName)));
		}
		
		getSchemaWriter().writeOredElements(elements);
		getSchemaWriter().endBlock();
	}

	private void writeHtmlTagSchemaElements()
	{
		String[] tagNames = Xmpz2GroupedConstants.getHtmlTagsAsElementNames();
		getSchemaWriter().write(FORMATTED_TEXT_TYPE + " = ( ");
		
		Vector<String> elements = new Vector<String>();
		elements.add(TEXT_ELEMENT_TYPE);
		for (int index = 0; index < tagNames.length; ++index)
		{
			elements.add("element." + tagNames[index]);
		}
		
		getSchemaWriter().writeOredElements(elements);
		getSchemaWriter().println(")*");
		
		getSchemaWriter().writeSchemaElement("br", "empty");
		getSchemaWriter().writeSchemaElement("b", FORMATTED_TEXT_TYPE);
		getSchemaWriter().writeSchemaElement("i", FORMATTED_TEXT_TYPE);
		getSchemaWriter().writeSchemaElement("div", FORMATTED_TEXT_TYPE);
		getSchemaWriter().writeSchemaElement("u", FORMATTED_TEXT_TYPE);
		getSchemaWriter().writeSchemaElement("strike", FORMATTED_TEXT_TYPE);
		getSchemaWriter().writeSchemaElement("ul", "element.li*");
		getSchemaWriter().writeSchemaElement("ol", "element.li*");
		getSchemaWriter().writeSchemaElement("li", FORMATTED_TEXT_TYPE);
		
		getSchemaWriter().printlnIndented(getSchemaWriter().createSelfNamedElement(HtmlUtilities.ANCHOR_ELEMENT_NAME));
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented(getSchemaWriter().createTextAttributeElement(HtmlUtilities.HREF_ATTRIBUTE_NAME) + " &");
		getSchemaWriter().printlnIndented(getSchemaWriter().createTextAttributeElement(HtmlUtilities.NAME_ATTRIBUTE_NAME) + "? &");
		getSchemaWriter().printlnIndented(getSchemaWriter().createTextAttributeElement(HtmlUtilities.TITLE_ATTRIBUTE_NAME) + "? &");
		getSchemaWriter().printlnIndented(getSchemaWriter().createTextAttributeElement(HtmlUtilities.TARGET_ATTRIBUTE_NAME) + "? &");			  
		getSchemaWriter().printlnIndented(FORMATTED_TEXT_TYPE);
		getSchemaWriter().endBlock();
	}
	
	private void writeUriRestrictedSchemaElement()
	{
		writeRegexRestrictedElement(URI_RESTRICTED_TEXT, "'([a-zA-Z0-9]|=|\\?|\\.|-|#|@|:|\\+|/|&|_|~|%)*'");
	}
	
	private void writeNonEmptyStringSchemaElement()
	{
		writeRegexRestrictedElement(NON_EMPTY_STRING, "'\\S+'");
	}

	private void writeUUIDSchemaElement()
	{
		writeRegexRestrictedElement(UUID, "'[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}'");
	}

	private void writeRegexRestrictedElement(final String elementName, final String regex)
	{
		getSchemaWriter().println(elementName + " = xsd:string { pattern = " + regex + " }");
	}

	private void writeGeospacialLocationElement()
	{
		String[] elementNames = createVector(LATITUDE, LONGITUDE);
		defineElementWithSameType(GEOSPATIAL_LOCATION, elementNames, "decimal");
	}
	
	private void writeDiagramPointElement()
	{
		String[] elementNames = createVector(X_ELEMENT_NAME, Y_ELEMENT_NAME);
		defineElementWithSameType(DIAGRAM_POINT_ELEMENT_NAME, elementNames, "integer");
	}
	
	private void writeDiagramSizeElement()
	{
		String[] elementNames = createVector(WIDTH_ELEMENT_NAME, HEIGHT_ELEMENT_NAME);
		defineElementWithSameType(DIAGRAM_SIZE_ELEMENT_NAME, elementNames, "integer");
	}
	
	private Xmpz2CustomSchemaDefinitionCreator createDashboardUserChoiceMapSchemaCreator()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), DASHBOARD_STATUS_ENTRY);
		creator.addTextAttributeElement(KEY_ATTRIBUTE_NAME);
		creator.addOptionalChildElement(DASHBOARD_PROGRESS, VOCABULARY_DASHBOARD_ROW_PROGRESS);
		creator.addChildElement(Xmpz2XmlWriter.createContainerElementName(DASHBOARD + DASHBOARD_FLAGS) + ".element? ");
		creator.addOptionalChildElement(DASHBOARD_COMMENTS, FORMATTED_TEXT_TYPE);
		
		return creator;
	}

	private void writeDashboardFlagsContainer()
	{
		String flagsContainerSchema = createCodelistSchemaElement("DashboardFlags", StaticQuestionManager.getQuestion(DashboardFlagsQuestion.class));
		getSchemaWriter().println(flagsContainerSchema);
	}
	
	private void writeDateUnitSchemaElements() 
	{
		defineDateUnitEfforts();
		defineFullProjectTimeSpanElement(WORK_UNITS_FULL_PROJECT_TIMESPAN);
		defineYearElement(WORK_UNITS_YEAR);
		defineQuarterElement(WORK_UNITS_QUARTER);
		defineMonthElement(WORK_UNITS_MONTH);
		defineDayElement(WORK_UNITS_DAY);
		
		defineDateUnitExpense();
		defineFullProjectTimeSpanElement(EXPENSES_FULL_PROJECT_TIMESPAN);
		defineYearElement(EXPENSES_YEAR);
		defineQuarterElement(EXPENSES_QUARTER);
		defineMonthElement(EXPENSES_MONTH);
		defineDayElement(EXPENSES_DAY);

		defineDateUnitTimeframe();
		defineFullProjectTimeSpanElement(TIMEFRAMES_FULL_PROJECT_TIMESPAN);
		defineYearElement(TIMEFRAMES_YEAR);
		defineQuarterElement(TIMEFRAMES_QUARTER);
		defineMonthElement(TIMEFRAMES_MONTH);
		defineDayElement(TIMEFRAMES_DAY);
	}
	
	private void defineDateUnitEfforts()
	{ 		
		final String[] elementTypes = new String[]{
				createElementName(WORK_UNITS_DAY),
				createElementName(WORK_UNITS_MONTH),
				createElementName(WORK_UNITS_QUARTER),
				createElementName(WORK_UNITS_YEAR),
				createElementName(WORK_UNITS_FULL_PROJECT_TIMESPAN),
				};
		
		getSchemaWriter().defineBudgetElementsWithQuantity(DATE_UNIT_WORK_UNITS, WORK_UNITS_DATE_UNIT, WORK_UNITS, elementTypes);
	}

	private void defineDateUnitExpense()
	{
		final String[] elementTypes = new String[]{
				createElementName(EXPENSES_DAY),
				createElementName(EXPENSES_MONTH),
				createElementName(EXPENSES_QUARTER),
				createElementName(EXPENSES_YEAR),
				createElementName(EXPENSES_FULL_PROJECT_TIMESPAN),
		};
		
		getSchemaWriter().defineBudgetElementsWithQuantity(DATE_UNITS_EXPENSE, EXPENSES_DATE_UNIT, EXPENSE, elementTypes);
	}

	private void defineDateUnitTimeframe()
	{
		final String[] elementTypes = new String[]{
				createElementName(TIMEFRAMES_DAY),
				createElementName(TIMEFRAMES_MONTH),
				createElementName(TIMEFRAMES_QUARTER),
				createElementName(TIMEFRAMES_YEAR),
				createElementName(TIMEFRAMES_FULL_PROJECT_TIMESPAN),
		};

		getSchemaWriter().defineBudgetElementsWithoutQuantity(DATE_UNITS_TIMEFRAME, TIMEFRAMES_DATE_UNIT, elementTypes);
	}

	private void defineFullProjectTimeSpanElement(String fullProjectTimeSpanElementName)
	{
		final String[] subElements = new String[]{
				getSchemaWriter().createAttributeElement(FULL_PROJECT_TIMESPAN, VOCABULART_FULL_PROJECT_TIMESPAN),
		};
		defineElement(fullProjectTimeSpanElementName, subElements);
	}
	
	private void defineYearElement(String yearElementName)
	{
		defineYearMonthElement(yearElementName, START_YEAR, START_MONTH);
	}

	private void defineQuarterElement(String quarterElementName)
	{
		defineYearMonthElement(quarterElementName, YEAR, START_MONTH);
	}
	
	private void defineMonthElement(String monthElementName)
	{
		defineYearMonthElement(monthElementName, YEAR, MONTH);
	}

	private void defineYearMonthElement(String elementName, final String yearElementName, final String monthElementName)
	{
		String[] subElements = createVector(createAttribute(yearElementName, VOCABULARY_YEAR), createAttribute(monthElementName, VOCABULARY_MONTH));
		defineElement(elementName, subElements);
	}

	private String createAttribute(final String attributeName, final String attributeType)
	{
		return getSchemaWriter().createAttributeElement(attributeName, attributeType);
	}
	
	private void defineDayElement(String dayElementName)
	{
		String[] subElements = new String[]{
				getSchemaWriter().createAttributeElement(DATE, VOCABULARY_DATE),
		};
		defineElement(dayElementName, subElements);
	}
	
	private void defineElement(String elementName, String[] subElements)
	{
		getSchemaWriter().defineAlias(createElementName(elementName), ELEMENT_NAME + PREFIX + elementName);
		getSchemaWriter().startBlock();
		getSchemaWriter().defineElements(Utility.convertToVector(subElements));
		getSchemaWriter().endBlock();
	}
	
	private Xmpz2CustomSchemaDefinitionCreator createExporterDetailsElementCreator()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), EXPORT_DETAILS);
		creator.addChildElement(EXPORTER_NAME, TEXT_ELEMENT_TYPE);
		creator.addChildElement(EXPORTER_VERSION, TEXT_ELEMENT_TYPE);
		creator.addChildElement(EXPORT_TIME, TEXT_ELEMENT_TYPE);
		
		return creator;
	}
	
	private Xmpz2CustomSchemaDefinitionCreator creatorThresholdsElementSchemaCreator()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), THRESHOLD);
		creator.addOptionalChildElement(STATUS_CODE, VOCABULARY_MEASUREMENT_STATUS);
		creator.addOptionalChildElement(THRESHOLD_VALUE, TEXT_ELEMENT_TYPE);
		creator.addOptionalChildElement(THRESHOLD_DETAILS, TEXT_ELEMENT_TYPE);
		
		return creator;
	}

	private Xmpz2CustomSchemaDefinitionCreator createTimePeriodCostsElementSchemaCreator()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), TIME_PERIOD_COSTS);
		creator.addChildElement(CALCULATED_START_DATE, VOCABULARY_DATE);
		creator.addChildElement(CALCULATED_END_DATE, VOCABULARY_DATE);
		creator.addOptionalDecimalElement(CALCULATED_EXPENSE_TOTAL);
		creator.addOptionalDecimalElement(CALCULATED_WORK_UNITS_TOTAL);
		creator.addOptionalDecimalElement(CALCULATED_TOTAL_BUDGET_COST);
		creator.addOptionalDecimalElement(CALCULATED_WORK_COST_TOTAL);
		creator.addOptionalChildElement(CALCULATED_WHO, getSchemaWriter().createZeroOrMoreDotElement(RESOURCE_ID));
		creator.addOptionalChildElement(CALCULATED_EXPENSE_ENTRIES, getSchemaWriter().createZeroOrMoreDotElement(EXPENSE_ENTRY));
		creator.addChildElement(getSchemaWriter().createOptionalSchemaElement(CALCULATED_WORK_UNITS_ENTRIES, getSchemaWriter().createZeroOrMoreDotElement(WORK_UNITS_ENTRY)));
		
		return creator;
	}

	private Xmpz2CustomSchemaDefinitionCreator createCalculatedTimeframeElementSchemaCreator()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), Xmpz2XmlConstants.CALCULATED_TIMEFRAME);
		creator.addChildElement(CALCULATED_START_DATE, VOCABULARY_DATE);
		creator.addChildElement(CALCULATED_END_DATE, VOCABULARY_DATE);

		return creator;
	}

	private Xmpz2CustomSchemaDefinitionCreator createExpenseEntryElementSchemaCreator()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), EXPENSE_ENTRY);
		creator.addOptionalSchemaElement(FUNDING_SOURCE_ID);
		creator.addOptionalSchemaElement(ACCOUNTING_CODE_ID);
		creator.addOptionalSchemaElement(BUDGET_CATEGORY_ONE_ID);
		creator.addOptionalSchemaElement(BUDGET_CATEGORY_TWO_ID);
		creator.addChildElement(getSchemaWriter().createSchemaElement(EXPENSE_ENTRY + DETAILS, getSchemaWriter().createZeroOrMoreDotElement(DATE_UNITS_EXPENSE)));
		
		return creator;
	}

	private Xmpz2CustomSchemaDefinitionCreator createWorkUnitsEntryElementSchemaCreator()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), WORK_UNITS_ENTRY);
		creator.addOptionalSchemaElement(RESOURCE_ID);
		creator.addOptionalSchemaElement(FUNDING_SOURCE_ID);
		creator.addOptionalSchemaElement(ACCOUNTING_CODE_ID);
		creator.addOptionalSchemaElement(BUDGET_CATEGORY_ONE_ID);
		creator.addOptionalSchemaElement(BUDGET_CATEGORY_TWO_ID);
		creator.addChildElement(getSchemaWriter().createSchemaElement(WORK_UNITS_ENTRY + DETAILS, getSchemaWriter().createZeroOrMoreDotElement(DATE_UNIT_WORK_UNITS)));
		
		return creator;
	}

	private Xmpz2CustomSchemaDefinitionCreator createExternalIdSchemaElementSchemaCreator()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), EXTERNAL_PROJECT_ID_ELEMENT_NAME);
		creator.addChildElement(EXTERNAL_APP_ELEMENT_NAME, NON_EMPTY_STRING);
		creator.addChildElement(PROJECT_ID, NON_EMPTY_STRING);
		
		return creator;
	}

	private Xmpz2CustomSchemaDefinitionCreator createDiagramFactorUiSettingsSchemaCreator()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), STYLE);
		creator.addOptionalChildElement(DIAGRAM_FACTOR + DIAGRAM_FACTOR_FONT_SIZE_ELEMENT_NAME, VOCABULARY_DIAGRAM_FACTOR_FONT_SIZE);
		creator.addOptionalChildElement(DIAGRAM_FACTOR + DIAGRAM_FACTOR_FONT_STYLE_ELEMENT_NAME, VOCABULARY_DIAGRAM_FACTOR_FONT_STYLE);
		creator.addOptionalChildElement(DIAGRAM_FACTOR + DIAGRAM_FACTOR_FOREGROUND_COLOR_ELEMENT_NAME, VOCABULARY_DIAGRAM_FACTOR_FOREGROUND_COLOR);
		creator.addOptionalChildElement(DIAGRAM_FACTOR + DIAGRAM_FACTOR_BACKGROUND_COLOR_ELEMENT_NAME, VOCABULARY_DIAGRAM_FACTOR_BACKGROUND_COLOR);
        creator.addOptionalChildElement(DIAGRAM_FACTOR + DIAGRAM_FACTOR_HEADER_SIZE_ELEMENT_NAME, VOCABULARY_DIAGRAM_FACTOR_HEADER_HEIGHT);

		return creator;
	}
	
	
	private Xmpz2CustomSchemaDefinitionCreator createExtraDataElement()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), EXTRA_DATA);
		creator.addZeroOrMoreDotElement(EXTRA_DATA_SECTION);
		
		return creator;
	}
	
	private Xmpz2CustomSchemaDefinitionCreator writeExtraDataSectionElement()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), EXTRA_DATA_SECTION);
		creator.addTextAttributeElement(EXTRA_DATA_SECTION_OWNER_ATTRIBUTE);
		creator.addZeroOrMoreDotElement(EXTRA_DATA_ITEM);
		
		return creator;
	}
	
	private Xmpz2CustomSchemaDefinitionCreator defineAccountingClassificationAssociationsElement()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), ACCOUNTING_CLASSIFICATION_ASSOCIATION);
		creator.addUriRestrictedAttributeElement(ACCOUNTING_CLASSIFICATION_ASSOCIATION_CODE);
		creator.addChildElement(ACCOUNTING_CLASSIFICATION_ASSOCIATION_SELECTION_TYPE, VOCABULARY_TAXONOMY_CLASSIFICATION_SELECTION_MODE);
		creator.addTextSchemaElement(ACCOUNTING_CLASSIFICATION_ASSOCIATION_LABEL);
		creator.addOptionalTextSchemaElement(ACCOUNTING_CLASSIFICATION_ASSOCIATION_DESCRIPTION);
		creator.addIntegerElement(ACCOUNTING_CLASSIFICATION_ASSOCIATION_SEQUENCE_NO);
		creator.addUriTextSchemaElement(ACCOUNTING_CLASSIFICATION_ASSOCIATION_TAXONOMY_CODE);

		return creator;
	}
	
	private Xmpz2CustomSchemaDefinitionCreator defineTaxonomyAssociationsElement()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), TAXONOMY_ASSOCIATION);
		creator.addUriRestrictedAttributeElement(TAXONOMY_ASSOCIATION + CODE);
		creator.addChildElement(TAXONOMY_ASSOCIATION_MULTI_SELECT, VOCABULARY_TAXONOMY_CLASSIFICATION_MULTISELECT_MODE);
		creator.addChildElement(TAXONOMY_ASSOCIATION_SELECTION_TYPE, VOCABULARY_TAXONOMY_CLASSIFICATION_SELECTION_MODE);
		creator.addTextSchemaElement(TAXONOMY_ASSOCIATION_LABEL);
		creator.addOptionalTextSchemaElement(TAXONOMY_ASSOCIATION_DESCRIPTION);
		creator.addUriTextSchemaElement(TAXONOMY_ASSOCIATION_TAXONOMY_CODE);

		return creator;
	}

	private Xmpz2CustomSchemaDefinitionCreator defineTaxonomyElement()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), TAXONOMY_ELEMENT);
		creator.addUriRestrictedAttributeElement(TAXONOMY_ELEMENT_CODE);
		creator.addChildElement(createContainerElement(TAXONOMY_ELEMENT_CHILD_CODE));
		creator.addTextSchemaElement(TAXONOMY_ELEMENT_LABEL);
		creator.addTextSchemaElement(TAXONOMY_ELEMENT_DESCRIPTION);
		creator.addTextSchemaElement(TAXONOMY_ELEMENT_USER_CODE);
		
		return creator;
	}

	private Xmpz2CustomSchemaDefinitionCreator defineTaxonomyClassificationContainerElement()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), TAXONOMY_CLASSIFICATION);
		creator.addChildElement(TAXONOMY_CLASSIFICATION_TAXONOMY_CODE, URI_RESTRICTED_TEXT);
		creator.addChildElement(createContainerElement(TAXONOMY_CLASSIFICATION_TAXONOMY_ELEMENT_CODE));

		return creator;
	}

	private void writeTaxonomyClassificationTaxonomyElementCodeContainer()
	{
		String containerDefinition = createCodelistSchemaElement(TAXONOMY_CLASSIFICATION_TAXONOMY_ELEMENT_CODE, new InternalQuestionWithoutValues());
		getSchemaWriter().println(containerDefinition);
	}

	private Xmpz2CustomSchemaDefinitionCreator defineAccountingClassificationContainerElement()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), ACCOUNTING_CLASSIFICATION);
		creator.addChildElement(ACCOUNTING_CLASSIFICATION_TAXONOMY_CODE, URI_RESTRICTED_TEXT);
		creator.addChildElement(createContainerElement(ACCOUNTING_CLASSIFICATION_TAXONOMY_ELEMENT_CODE));

		return creator;
	}

	private void writeAccountingClassificationTaxonomyElementCodeContainer()
	{
		String containerDefinition = createCodelistSchemaElement(ACCOUNTING_CLASSIFICATION_TAXONOMY_ELEMENT_CODE, new InternalQuestionWithoutValues());
		getSchemaWriter().println(containerDefinition);
	}

	private void writeTaxonomyElementChildElementCodeContainer()
	{
		String containerDefinition = createCodelistSchemaElement(TAXONOMY_ELEMENT_CHILD_CODE, new InternalQuestionWithoutValues());
		getSchemaWriter().println(containerDefinition);
	}

	private void writeTaxonomyElementCode()
	{
		getSchemaWriter().writeTextElement(TAXONOMY_ELEMENT_CODE);
		getSchemaWriter().println();
	}
	
	private Xmpz2CustomSchemaDefinitionCreator defineExtraDataItemElement()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), EXTRA_DATA_ITEM);
		creator.addTextAttributeElement(EXTRA_DATA_ITEM_NAME);
		creator.addOptionalTextSchemaElement(EXTRA_DATA_ITEM_VALUE);
		
		return creator;
	}
	
	private Xmpz2CustomSchemaDefinitionCreator defineThreatStressRatingElement()
	{
		Xmpz2CustomSchemaDefinitionCreator creator = new Xmpz2CustomSchemaDefinitionCreator(getSchemaWriter(), THREAT_STRESS_RATING);
		creator.addChildElement(THREAT_STRESS_RATING + THREAT_ID, THREAT_ID + DOT_ELEMENT);
		creator.addChildElement(THREAT_STRESS_RATING + STRESS_ID, STRESS_ID + DOT_ELEMENT);
		creator.addOptionalChildElement(THREAT_STRESS_RATING + CONTRIBUTION, VOCABULARY_THREAT_STRESS_RATING_CONTRIBUTION_CODE);
		creator.addOptionalChildElement(THREAT_STRESS_RATING + IRREVERSIBILITY, VOCABULARY_IRREVERSIBILITY_CODE);
		creator.addOptionalChildElement(THREAT_STRESS_RATING + IS_ACTIVE, getSchemaWriter().createBooleanType());
		creator.addOptionalChildElement(THREAT_STRESS_RATING + CALCULATED_THREAT_STRESS_RATING, VOCABULARY_THREAT_RATING);

		return creator;
	}
	
	public String createOptionalSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, final String elementType)
	{
		return createElementSchema(baseObjectSchema, fieldSchema, getSchemaWriter().createDotElement(elementType));
	}
	
	private String createSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, final String elementType)
	{
		return createElementSchema(baseObjectSchema, fieldSchema, getSchemaWriter().createZeroOrMoreDotElement(elementType));
	}
	
	private String createRequiredSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, final String elementType)
	{
		return createRequiredElementSchema(baseObjectSchema, fieldSchema, getSchemaWriter().createDotElement(elementType));
	}

	private String createElementSchema(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String elementType)
	{
		String poolName = baseObjectSchema.getXmpz2ElementName();
		String elementName = getTagToElementNameMap().findElementName(poolName, fieldSchema.getTag());
		return getSchemaWriter().createOptionalSchemaElement(poolName + elementName, elementType);
	}
	
	private String createRequiredElementSchema(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String elementType)
	{
		String poolName = baseObjectSchema.getXmpz2ElementName();
		String elementName = getTagToElementNameMap().findElementName(poolName, fieldSchema.getTag());
		return getSchemaWriter().createSchemaElement(poolName + elementName, elementType);
	}
	
	private void defineElementWithSameType(String parentName, String[] names, String elementType)
	{
		getSchemaWriter().startElementDefinition(parentName);
		Vector<String> elementNames = new Vector<String>();
		for (int index = 0; index < names.length; ++index)
		{
			final String elementName = getSchemaWriter().createXsdSchemaElement(names[index], elementType);
			elementNames.add(elementName);	
		}
		
		getSchemaWriter().defineElements(elementNames);
		getSchemaWriter().endBlock();
	}

	private Vector<BaseObjectSchemaWriter> getTopLevelBaseObjectSchemas()
	{
		Vector<BaseObjectSchemaWriter> schemaWriters = new Vector<BaseObjectSchemaWriter>();
		for(int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			if (!isPoolDirectlyInXmpz2(objectType))
				continue;

			BaseObjectPool pool = getProject().getPool(objectType);
			if(pool == null)
				continue;
			
			BaseObjectSchema baseObjectSchema = pool.createBaseObjectSchema(getProject());
			schemaWriters.add(createSchemaWriter(baseObjectSchema));
		}
		
		schemaWriters.add(new SimpleThreatRatingSchemaWriter(this));
		schemaWriters.add(new StressBasedThreatRatingSchemaWriter(this));
		
		return schemaWriters;
	}

	private BaseObjectSchemaWriter createSchemaWriter(BaseObjectSchema baseObjectSchema)
	{
		if (Indicator.is(baseObjectSchema.getType()))
			return new IndicatorSchemaWriter(this, baseObjectSchema);
		
		if (Method.is(baseObjectSchema.getType()))
			return new MethodSchemaWriter(this, baseObjectSchema);

		if (Measurement.is(baseObjectSchema.getType()))
			return new MeasurementSchemaWriter(this, baseObjectSchema);

		if (Desire.isDesire(baseObjectSchema.getType()))
			return new DesireSchemaWriter(this, baseObjectSchema);
		
		if (Task.is(baseObjectSchema.getType()))
			return new TaskSchemaWriter(this, baseObjectSchema);
		
		if (Strategy.is(baseObjectSchema.getType()))
			return new StrategySchemaWriter(this, baseObjectSchema);
		
		if (DiagramLink.is(baseObjectSchema.getType()))
			return new DiagramLinkSchemaWriter(this, baseObjectSchema);
		
		if (DiagramFactor.is(baseObjectSchema.getType()))
			return new DiagramFactorSchemaWriter(this, baseObjectSchema);
		
		if (ObjectTreeTableConfiguration.is(baseObjectSchema.getType()))
			return new ObjectTreeTableConfigurationSchemaWriter(this, baseObjectSchema);
		
		if (Target.is(baseObjectSchema.getType()))
			return new TargetSchemaWriter(this, baseObjectSchema);
		
		if (HumanWelfareTarget.is(baseObjectSchema.getType()))
			return new HumanWelfareTargetSchemaWriter(this, baseObjectSchema);
		
		if (BiophysicalFactor.is(baseObjectSchema.getType()))
			return new BiophysicalFactorSchemaWriter(this, baseObjectSchema);

		if (BiophysicalResult.is(baseObjectSchema.getType()))
			return new BiophysicalResultSchemaWriter(this, baseObjectSchema);

		if (Cause.is(baseObjectSchema.getType()))
			return new CauseSchemaWriter(this, baseObjectSchema);
		
		if (Dashboard.is(baseObjectSchema.getType()))
			return new DashboardSchemaWriter(this, baseObjectSchema);
		
		if (Stress.is(baseObjectSchema.getType()))
			return new StressSchemaWriter(this, baseObjectSchema);
		
		if (KeyEcologicalAttribute.is(baseObjectSchema.getType()))
			return new KeyEcologicalAttributeSchemaWriter(this, baseObjectSchema);
		
		if (ResultsChainDiagram.is(baseObjectSchema.getType()))
			return new ResultsChainSchemaWriter(this, baseObjectSchema);
		
		if (ConceptualModelDiagram.is(baseObjectSchema.getType()))
			return new ConceptualModelSchemaWriter(this, baseObjectSchema);

		if (ThreatReductionResult.is(baseObjectSchema.getType()))
			return new ThreatReductionResultSchemaWriter(this, baseObjectSchema);
		
		if (IntermediateResult.is(baseObjectSchema.getType()))
			return new IntermediateResultSchemaWriter(this, baseObjectSchema);

		if (MiradiShareTaxonomy.is(baseObjectSchema.getType()))
			return new MiradiShareTaxonomySchemaWriter(this, baseObjectSchema);
		
		if (ProjectResource.is(baseObjectSchema.getType()))
			return new ResourceSchemaWriter(this, baseObjectSchema);

		if (TaggedObjectSet.is(baseObjectSchema.getType()))
			return new TaggedObjectSetSchemaWriter(this, baseObjectSchema);

		return new BaseObjectSchemaWriter(this, baseObjectSchema);
	}
	
	private boolean isPoolDirectlyInXmpz2(int objectType)
	{
		if (FactorLink.is(objectType))
			return false;
		
		if (ViewData.is(objectType))
			return false;
		
		if (ProjectMetadata.is(objectType))
			return false;

		if (CostAllocationRuleSchema.getObjectType() == objectType)
			return false;

		if (ThreatStressRating.is(objectType))
			return false;

		if (WcpaProjectDataSchema.getObjectType() == objectType)
			return false;

		if (WwfProjectDataSchema.getObjectType() == objectType)
			return false;

		if (RareProjectDataSchema.getObjectType() == objectType)
			return false;

		if (WcsProjectDataSchema.getObjectType() == objectType)
			return false;

		if (TncProjectDataSchema.getObjectType() == objectType)
			return false;

		if (FosProjectDataSchema.getObjectType() == objectType)
			return false;

		if (WwfProjectDataSchema.getObjectType() == objectType)
			return false;

		if (Xenodata.is(objectType))
			return false;

		if (TableSettings.is(objectType))
			return false;

		if (ThreatStressRatingData.is(objectType))
			return false;

		if (ThreatSimpleRatingData.is(objectType))
			return false;

		if (RatingCriterion.is(objectType))
			return false;

		if (ValueOptionSchema.getObjectType() == objectType)
			return false;

		if (ReportTemplate.is(objectType))
			return false;

		if (XslTemplate.is(objectType))
			return false;
		
		if (MiradiShareProjectData.is(objectType))
			return false;

		if (TaxonomyAssociation.is(objectType))
			return false;
		
		if (AccountingClassificationAssociation.is(objectType))
			return false;

		return true;
	}
	
	private String getDateUnitTypeName(int objectType)
	{
		if (ExpenseAssignment.is(objectType))
			return EXPENSE;
		
		if (Timeframe.is(objectType))
			return TIMEFRAME;
		
		if (ResourceAssignment.is(objectType))
			return "WorkUnits";

		throw new RuntimeException("Object type " + objectType + " cannot have a dateunitEffortsList field");
	}
	
	private String[] createVector(String item1, String item2)
	{
		final String[] items = new String[]{
				item1,
				item2,
		};

		return items;
	}

	private String createIdName(String objectName)
	{
		return objectName + ID;
	}

	public ChoiceQuestionToSchemaElementNameMap getChoiceQuestionToSchemaElementNameMap()
	{
		return choiceQuestionToSchemaElementNameMap;
	}
		
	private Xmpz2TagToElementNameMap getTagToElementNameMap()
	{
		return tagToElementNameMap;
	}
	
	public Xmpz2SchemaWriter getSchemaWriter()
	{
		return writer;
	}

	public Project getProject()
	{
		return project;
	}
	
	private Xmpz2TagToElementNameMap tagToElementNameMap;
	private ChoiceQuestionToSchemaElementNameMap choiceQuestionToSchemaElementNameMap;
	private Project project;
	private Xmpz2SchemaWriter writer;
	private Vector<BaseObjectSchemaWriter> baseObjectSchemaWriters;
	private Vector<String> codelistSchemaElements;
}
