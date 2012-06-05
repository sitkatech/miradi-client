/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

import java.util.Set;
import java.util.Vector;

import org.miradi.main.Miradi;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.objects.Desire;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.TableSettings;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.ViewData;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.OpenStandardsProgressStatusQuestion;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.CostAllocationRuleSchema;
import org.miradi.schemas.FosProjectDataSchema;
import org.miradi.schemas.ProjectSummarySchema;
import org.miradi.schemas.RareProjectDataSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.schemas.WcsProjectDataSchema;
import org.miradi.schemas.WwfProjectDataSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.StringUtilities;
import org.miradi.utils.Translation;
import org.miradi.xml.xmpz2.Xmpz2TagToElementNameMap;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

public class Xmpz2XmlSchemaCreator implements Xmpz2XmlConstants
{
	public static void main(String[] args) throws Exception
	{
		Miradi.addThirdPartyJarsToClasspath();
		Translation.initialize();

		Xmpz2SchemaWriter writer = new Xmpz2SchemaWriter(System.out);
		Xmpz2XmlSchemaCreator creator = new Xmpz2XmlSchemaCreator(writer);
		
		creator.writeRncSchema();
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
		writeHeader();
		writeConservationProjectElement();
		writeSingletonElements();
		writeBaseObjectSchemaElements();
		writeCodelistSchemaElements();
		writeVocabularyDefinitions();
		writeObjectTypeIdElements();
		writeWrappedByDiagramFactorSchemaElement();
		writeHtmlTagSchemaElements();
		writeGeospacialLocationElement();
		writeDiagramPointElement();
		writeDiagramSizeElement();
		writeDateUnitSchemaElements();
		defineThresholdsElement();
		defineTimePeriodCostsElement();
	}

	private void writeHeader()
	{
		getSchemaWriter().writeNamespace(NAME_SPACE);
		getSchemaWriter().defineAlias("start", CONSERVATION_PROJECT + ".element");
	}

	private void writeConservationProjectElement()
	{
		getSchemaWriter().startElementDefinition(CONSERVATION_PROJECT);

		Vector<String> elementNames = new Vector<String>();
		elementNames.add(createElementName(PROJECT_SUMMARY));
		elementNames.add(createElementName(PROJECT_SUMMARY_SCOPE));
		elementNames.add(createElementName(PROJECT_SUMMARY_LOCATION));
		elementNames.add(createElementName(PROJECT_SUMMARY_PLANNING));
		for(BaseObjectSchemaWriter baseObjectSchemaWriter : baseObjectSchemaWriters)
		{
			String poolName = baseObjectSchemaWriter.getPoolName();
			elementNames.add(createElementName(poolName) + " ?");
		}
		
		getSchemaWriter().writeContentsList(elementNames);
		
		getSchemaWriter().endElementDefinition(CONSERVATION_PROJECT);
		getSchemaWriter().flush();
	}
	
	private void writeSingletonElements() throws Exception
	{
		writeSingletonObjectSchema(new ProjectSummarySchema());
		writeSingletonObjectSchema(new ProjectSummaryScopeSchema());
		writeSingletonObjectSchema(new ProjectSummaryLocationSchema());
		writeSingletonObjectSchema(new ProjectSummaryPlanningSchema());
		writeSingletonObjectSchema(new ProjectSummaryTncProjectDataSchema());
		writeSingletonObjectSchema(new WwfProjectDataSchema());
		writeSingletonObjectSchema(new WcsProjectDataSchema());
		writeSingletonObjectSchema(new FosProjectDataSchema());
		writeSingletonObjectSchema(new RareProjectDataSchema());
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
		baseObjectSchemaWriter.writeFields(getSchemaWriter());
		getSchemaWriter().println();
	}
	
	public void writeRelevantSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		writeElementSchema(baseObjectSchema, fieldSchema, getRelevantTypeName(fieldSchema));
	}
	
	private String getRelevantTypeName(AbstractFieldSchema fieldSchema)
	{
		if (fieldSchema.getTag().equals(Desire.TAG_RELEVANT_INDICATOR_SET))
			return "IndicatorId.element*";
		
		if (fieldSchema.getTag().equals(RELEVANT_STRATEGY_IDS))
			return "StrategyId.element*";
		
		if (fieldSchema.getTag().equals(RELEVANT_ACTIVITY_IDS))
			return "ActivityId.element*";
		
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
	
	public void writeCalculatedCostSchemaElement(BaseObjectSchema baseObjectSchema)
	{
		writeSchemaElement(baseObjectSchema.getXmpz2ElementName(), TIME_PERIOD_COSTS, TIME_PERIOD_COSTS + ".element");
	}
	
	public void writeThresholdsSchemaElement(BaseObjectSchema baseObjectSchema)
	{
		writeSchemaElement(baseObjectSchema.getXmpz2ElementName(), THRESHOLDS, "IndicatorThreshold.element*");
	}
	
	public void writeStringRefMapSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		writeSchemaElement(baseObjectSchema, fieldSchema, EXTERNAL_PROJECT_ID_ELEMENT_NAME);
	}
	
	private void writeVocabularyDefinitions()
	{
		Set<ChoiceQuestion> choiceQuestions = choiceQuestionToSchemaElementNameMap.keySet();
		for(ChoiceQuestion question : choiceQuestions)
		{
			String vocabularyName = choiceQuestionToSchemaElementNameMap.get(question);
			defineVocabulary(question, vocabularyName);
		}
		
		getSchemaWriter().println("vocabulary_full_project_timespan = xsd:NMTOKEN { pattern = 'Total' } ");
		getSchemaWriter().println("vocabulary_year = xsd:NMTOKEN { pattern = '[0-9]{4}' } ");
		getSchemaWriter().println("vocabulary_month = xsd:integer { minInclusive='1' maxInclusive='12' } ");
		getSchemaWriter().println("vocabulary_date = xsd:NMTOKEN { pattern = '[0-9]{4}-[0-9]{2}-[0-9]{2}' }");
		getSchemaWriter().println(VOCABULARY_TEXT_BOX_Z_ORDER + " = '" + Z_ORDER_BACK_CODE + "' | '" + TextBoxZOrderQuestion.FRONT_CODE + "'");
		defineDashboardStatusesVocabulary();
	}
	
	private void writeObjectTypeIdElements()
	{
		defineIdElement(CONCEPTUAL_MODEL);
		defineIdElement(RESULTS_CHAIN);
		defineIdElement(DIAGRAM_FACTOR);
		defineIdElement(DIAGRAM_LINK);
		defineIdElement(BIODIVERSITY_TARGET);
		defineIdElement(HUMAN_WELFARE_TARGET);
		defineIdElement(CAUSE);
		defineIdElement(STRATEGY);
		defineIdElement(THREAT_REDUCTION_RESULTS);
		defineIdElement(INTERMEDIATE_RESULTS);
		defineIdElement(GROUP_BOX);
		defineIdElement(TEXT_BOX);
		defineIdElement(SCOPE_BOX);
		defineIdElement(ACTIVITY);
		defineIdElement(STRESS);
		defineIdElement(GOAL);
		defineIdElement(OBJECTIVE);
		defineIdElement(INDICATOR);
		defineIdElement(KEY_ECOLOGICAL_ATTRIBUTE);
		defineIdElement(TAGGED_OBJECT_SET_ELEMENT_NAME);
		defineIdElement(SUB_TARGET);
		defineIdElement(THREAT);
		defineIdElement(ACCOUNTING_CODE);
		defineIdElement(FUNDING_SOURCE);
		defineIdElement(BUDGET_CATEGORY_ONE);
		defineIdElement(BUDGET_CATEGORY_TWO);
		defineIdElement(PROGRESS_REPORT);
		defineIdElement(PROGRESS_PERCENT);
		defineIdElement(EXPENSE_ASSIGNMENT);
		defineIdElement(RESOURCE_ASSIGNMENT);
		defineIdElement(RESOURCE_ID_ELEMENT_NAME);
		defineIdElement(ACTIVITY);
		defineIdElement(MEASUREMENT);
		defineIdElement(METHOD);
		defineIdElement(SUB_TASK);
	}
	
	private void defineIdElement(String baseName)
	{
		getSchemaWriter().println(baseName + "Id.element = element " + PREFIX + baseName + "Id { xsd:integer }");
	}
	
	public void writeIdAttribute()
	{
		getSchemaWriter().printIndented("attribute " + ID + " "+ "{xsd:integer} &");
	}

	private void writeBaseObjectSchemaHeader(BaseObjectSchemaWriter baseObjectSchemaWriter)
	{
		String baseObjectName = baseObjectSchemaWriter.getXmpz2ElementName();
		String baseObjectPoolName = baseObjectSchemaWriter.getPoolName();
		
		String result = ELEMENT_NAME + PREFIX + baseObjectPoolName + " { " + createElementName(baseObjectName) + "* }";
		getSchemaWriter().defineAlias(createElementName(baseObjectPoolName), result);
		getSchemaWriter().defineAlias(createElementName(baseObjectName), "element miradi:" + baseObjectName);
	}

	private String createElementName(String elementName)
	{
		return elementName + ".element";
	}
	
	public void writeStringSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		writeElementSchema(baseObjectSchema, fieldSchema, "text");
	}

	public void writeUserTextSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		writeElementSchema(baseObjectSchema, fieldSchema, "formatted_text");
	}
	
	public void writeBooleanSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		writeElementSchema(baseObjectSchema, fieldSchema, "xsd:boolean");
	}
	
	public void writeNumberSchemaElement(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema)
	{
		writeElementSchema(baseObjectSchema, fieldSchema, "xsd:decimal");
	}
	
	public void writeDateSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		writeElementSchema(baseObjectSchema, fieldSchema, "vocabulary_date");
	}
	
	public void writeRefSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		String fieldName = getTagToElementNameMap().findElementName(baseObjectSchema, fieldSchema);
		writeSchemaElement(baseObjectSchema, fieldSchema, fieldName);
	}
	
	public void writeBaseIdSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, int objectType)
	{
		String objectName = getIdElementName(baseObjectSchema, fieldSchema, objectType);
		writeSchemaElement(baseObjectSchema, fieldSchema, objectName + ID);
	}

	private String getIdElementName(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, int objectType)
	{
		if (ResourceAssignment.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(ResourceAssignment.TAG_RESOURCE_ID))
			return RESOURCE_ID;
	
		return getProject().getObjectManager().getInternalObjectTypeName(objectType);
	}

	public void writeDateUnitEffortListSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		writeSchemaElement(baseObjectSchema, fieldSchema, "DateUnit" + getDateUniteTypeName(baseObjectSchema.getType()));
	}
	
	public void writeChoiceSchemaElement(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, ChoiceQuestion choiceQuestion)
	{
		String vocabularyName = getChoiceQuestionToSchemaElementNameMap().get(choiceQuestion);
		writeElementSchema(baseObjectSchema, fieldSchema, vocabularyName);
	}
	
	public void writeIdListSchemaElement(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema)
	{
		writeRefListSchemaElement(baseObjectSchema, fieldSchema);
	}

	public void writeRefListSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		String elementName = getTagToElementNameMap().findElementName(baseObjectSchema.getXmpz2ElementName(), fieldSchema.getTag());
		String reflistElementName = baseObjectSchema.getXmpz2ElementName() + elementName;
		final String idElementName = createIdElementName(baseObjectSchema, fieldSchema, elementName);
		getSchemaWriter().printIndented("element " + PREFIX + reflistElementName + " { " + idElementName + ".element* }?");
	}

	private String createIdElementName(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String elementName)
	{
		if (Task.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(Task.TAG_SUBTASK_IDS))
			return SUB_TASK + ID;
				
		return StringUtilities.removeLastChar(elementName);
	}
	
	public void writeCodelistSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, ChoiceQuestion question)
	{
		String elementName = getTagToElementNameMap().findElementName(baseObjectSchema.getXmpz2ElementName(), fieldSchema.getTag());
		String codelistElementName = baseObjectSchema.getXmpz2ElementName() + elementName;
		getSchemaWriter().printIndented(codelistElementName + "Container.element ?");
		
		codelistSchemaElements.add(createCodelistSchemaElement(codelistElementName, question));
	}
	
	private String createCodelistSchemaElement(String codelistElementName, ChoiceQuestion question)
	{
		String vocabularyName = getChoiceQuestionToSchemaElementNameMap().get(question);
		String containerElement = codelistElementName + "Container.element = element " +  RAW_PREFIX + ":" + codelistElementName + "Container " + HtmlUtilities.NEW_LINE;
		containerElement += "{" + HtmlUtilities.NEW_LINE;
		containerElement += getSchemaWriter().INDENTATION + "element " + PREFIX + "code { " + vocabularyName + " } *" + HtmlUtilities.NEW_LINE;
		containerElement += "}" + HtmlUtilities.NEW_LINE;
		
		return containerElement;
	}
	
	private void defineVocabulary(ChoiceQuestion question, String vocabularyName)
	{
		CodeList codes = question.getAllCodes();
		getSchemaWriter().print(vocabularyName + " = ");
		for(int index = 0; index < codes.size(); ++index)
		{
			String code = codes.get(index);
			code = question.convertToReadableCode(code);
			getSchemaWriter().write("'" + code + "'");
			if (index < codes.size() - 1)
				getSchemaWriter().print("|");
		}
		
		getSchemaWriter().println();
	}
	
	private void writeWrappedByDiagramFactorSchemaElement()
	{
		final String[] factorNames = new String[]{BIODIVERSITY_TARGET, 
												  HUMAN_WELFARE_TARGET, 
												  CAUSE, 
												  STRATEGY, 
												  THREAT_REDUCTION_RESULTS, 
												  INTERMEDIATE_RESULTS,
												  GROUP_BOX,
												  TEXT_BOX,
												  SCOPE_BOX,
												  ACTIVITY,
												  STRESS,
		};
		
		getSchemaWriter().startElementDefinition(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		for (int index = 0; index < factorNames.length; ++index)
		{
			getSchemaWriter().printIndented(factorNames[index] + ID + ".element");
			if (index < factorNames.length - 1)
				getSchemaWriter().println(" |");
		}
		getSchemaWriter().println();
		getSchemaWriter().endBlock();
	}
	
	private void writeHtmlTagSchemaElements()
	{
		String[] tagNames = new String[] {"br", "b", "i", "u", "strike", "a", "ul", "ol",};
		getSchemaWriter().write("formatted_text = ( text |");
		getSchemaWriter().println();
		for (int index = 0; index < tagNames.length; ++index)
		{
			getSchemaWriter().printIndented("element." + tagNames[index]);
			if (index < tagNames.length - 1)
				getSchemaWriter().println(" | ");
		}
		getSchemaWriter().println();
		getSchemaWriter().println(")*");
		
		getSchemaWriter().printlnIndented("element.br = element br { empty }");
		getSchemaWriter().printlnIndented("element.b = element b { formatted_text }");
		getSchemaWriter().printlnIndented("element.i = element i { formatted_text }");
		getSchemaWriter().printlnIndented("element.u = element u { formatted_text }");
		getSchemaWriter().printlnIndented("element.strike = element strike { formatted_text }");
		getSchemaWriter().printlnIndented("element.ul = element ul { element.li* }");
		getSchemaWriter().printlnIndented("element.ol = element ol { element.li* }");
		getSchemaWriter().printlnIndented("element.li = element li { formatted_text }");
		
		getSchemaWriter().printlnIndented("element.a = element a ");
		getSchemaWriter().printlnIndented("{");
		getSchemaWriter().printlnIndented("	attribute href {text} &");
		getSchemaWriter().printlnIndented("	attribute name {text}? &");
		getSchemaWriter().printlnIndented("	attribute title {text}? &");
		getSchemaWriter().printlnIndented("	attribute target {text}? &");			  
		getSchemaWriter().printlnIndented(" formatted_text  ");
		getSchemaWriter().printlnIndented("}");
	}
	
	private void writeGeospacialLocationElement()
	{
		getSchemaWriter().startElementDefinition(GEOSPATIAL_LOCATION);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented("element " + PREFIX + "latitude { xsd:decimal } &");
		getSchemaWriter().printlnIndented("element " + PREFIX + "longitude { xsd:decimal } ");
		getSchemaWriter().endBlock();
	}
	
	private void writeDiagramPointElement()
	{
		getSchemaWriter().startElementDefinition(DIAGRAM_POINT_ELEMENT_NAME);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented("element " + PREFIX + "x { xsd:integer } & ");
		getSchemaWriter().printlnIndented("element " + PREFIX + "y { xsd:integer }"); 
		getSchemaWriter().endBlock();
	}
	
	private void writeDiagramSizeElement()
	{
		getSchemaWriter().startElementDefinition(DIAGRAM_SIZE_ELEMENT_NAME);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented("element " + PREFIX + "width { xsd:integer } & ");
		getSchemaWriter().printlnIndented("element " + PREFIX + "height { xsd:integer }"); 
		getSchemaWriter().endBlock();
	}

	private void defineDashboardStatusesVocabulary()
	{
		final String[] allCodesFromDynamicQuestion = new String[]{
			OpenStandardsProgressStatusQuestion.NOT_SPECIFIED_CODE,
			OpenStandardsProgressStatusQuestion.NOT_STARTED_CODE,
			OpenStandardsProgressStatusQuestion.IN_PROGRESS_CODE,
			OpenStandardsProgressStatusQuestion.COMPLETE_CODE,
			OpenStandardsProgressStatusQuestion.NOT_APPLICABLE_CODE,
		};
		
		getSchemaWriter().print(VOCABULARY_DASHBOARD_ROW_PROGRESS + " = ");
		for(int index = 0; index < allCodesFromDynamicQuestion.length; ++index)
		{
			String code = allCodesFromDynamicQuestion[index];
			getSchemaWriter().write("'" + code + "'");
			if (index < allCodesFromDynamicQuestion.length - 1)
				getSchemaWriter().print("|");
		}
		
		getSchemaWriter().println();
	}
	
	private void writeDateUnitSchemaElements()
	{
		defineDateUnitEfforts();
		defineWorkUnitsFullProjectTimeSpanElement();
		defineWorkUnitsYearElement();
		defineWorkUnitsQuarterElement();
		defineWorkUnitsMonthElement();
		defineWorkUnitsDayElement();
		
		defineDateUnitExpense();
		defineExpenseFullProjectTimeSpanElement();
		defineExpenseYearElement();
		defineExpenseQuarterElement();
		defineExpenseMonthElement();
		defineExpenseDayElement();
	}
	
	private void defineDateUnitEfforts()
	{
		writer.defineAlias("DateUnitWorkUnits.element", "element " + PREFIX + "DateUnitWorkUnits");
		writer.startBlock();
		writer.printlnIndented("element " + PREFIX + "WorkUnitsDateUnit{WorkUnitsDay.element | WorkUnitsMonth.element | WorkUnitsQuarter.element | WorkUnitsYear.element | WorkUnitsFullProjectTimespan.element }? &");
		writer.printlnIndented("element " + PREFIX + "NumberOfUnits { xsd:decimal }?");
		writer.endBlock();
	}

	private void defineDateUnitExpense()
	{
		writer.defineAlias("DateUnitExpense.element", "element " + PREFIX + "DateUnitExpense");
		writer.startBlock();
		writer.printlnIndented("element " + PREFIX + EXPENSES_DATE_UNIT + "{ExpensesDay.element | ExpensesMonth.element | ExpensesQuarter.element | ExpensesYear.element | ExpensesFullProjectTimespan.element }? &");
		writer.printlnIndented("element " + PREFIX + EXPENSE + " { xsd:decimal }?");
		writer.endBlock();
	}
	
	private void defineExpenseFullProjectTimeSpanElement()
	{
		defineFullProjectTimeSpanElement("ExpensesFullProjectTimespan");
	}
	
	private void defineExpenseYearElement()
	{
		defineYearElement("ExpensesYear");
	}

	private void defineExpenseQuarterElement()
	{
		defineQuarterElement("ExpensesQuarter");
	}
	
	private void defineExpenseMonthElement()
	{
		defineMonthElement("ExpensesMonth");
	}
	
	private void defineExpenseDayElement()
	{
		defineDayElement("ExpensesDay");
	}
	
	private void defineWorkUnitsFullProjectTimeSpanElement()
	{
		defineFullProjectTimeSpanElement("WorkUnitsFullProjectTimespan");
	}
		
	private void defineWorkUnitsYearElement()
	{
		defineYearElement("WorkUnitsYear");
	}

	private void defineWorkUnitsQuarterElement()
	{
		defineQuarterElement("WorkUnitsQuarter");
	}

	private void defineWorkUnitsMonthElement()
	{
		defineMonthElement("WorkUnitsMonth");
	}
	
	private void defineWorkUnitsDayElement()
	{
		defineDayElement("WorkUnitsDay");
	}
	
	private void defineFullProjectTimeSpanElement(String fullProjectTimeSpanElementName)
	{
		String[] subElements = new String[]{"attribute " + FULL_PROJECT_TIMESPAN + " { vocabulary_full_project_timespan }"};
		defineElement(fullProjectTimeSpanElementName, subElements);
	}
	
	private void defineYearElement(String yearElementName)
	{
		String[] subElements = new String[]{"attribute StartYear {vocabulary_year}", "attribute StartMonth {vocabulary_month}"};
		defineElement(yearElementName, subElements);
	}
		
	private void defineQuarterElement(String quarterElementName)
	{
		String[] subElements = new String[]{"attribute Year {vocabulary_year}", "attribute StartMonth {vocabulary_month}"};
		defineElement(quarterElementName, subElements);
	}
	
	private void defineMonthElement(String monthElementName)
	{
		String[] subElements = new String[]{"attribute Year {vocabulary_year}", "attribute Month {vocabulary_month}"};
		defineElement(monthElementName, subElements);
	}
	
	private void defineDayElement(String dayElementName)
	{
		String[] subElements = new String[]{"attribute Date {vocabulary_date}"};
		defineElement(dayElementName, subElements);
	}

	private void defineElement(String elementName, String[] subElements)
	{
		writer.defineAlias(elementName + ".element", "element miradi:" + elementName);
		writer.startBlock();
		for (int index = 0; index < subElements.length; ++index)
		{
			if (index > 0)
				writer.println(" &");
			
			writer.printIndented(subElements[index]);
		}
		
		writer.println();
		writer.endBlock();
	}
	
	private void defineThresholdsElement()
	{
		writer.defineAlias(THRESHOLD + ".element", "element " + PREFIX + THRESHOLD);
		writer.startBlock();
		writer.printlnIndented("element " + PREFIX + STATUS_CODE + "{" + VOCABULARY_MEASUREMENT_STATUS + "}? &");
		writer.printlnIndented("element " + PREFIX + THRESHOLD_VALUE + " { text }? &");
		writer.printlnIndented("element " + PREFIX + THRESHOLD_DETAILS + " { text }?");
		writer.endBlock();
	}
	
	private void defineTimePeriodCostsElement()
	{
		writer.defineAlias(TIME_PERIOD_COSTS + ".element", "element " + PREFIX + TIME_PERIOD_COSTS);
		writer.startBlock();
		writer.printlnIndented("element " + PREFIX + CALCULATED_START_DATE + "{ vocabulary_date } &");
		writer.printlnIndented("element " + PREFIX + CALCULATED_END_DATE + "{ vocabulary_date } &");
		writer.printlnIndented("element " + PREFIX + CALCULATED_EXPENSE_TOTAL + "{ xsd:decimal }? &");
		writer.printlnIndented("element " + PREFIX + CALCULATED_WORK_UNITS_TOTAL + "{ xsd:decimal }? &");
		writer.printlnIndented("element " + PREFIX + CALCULATED_TOTAL_BUDGET_COST + "{ xsd:decimal }? &");
		writer.printlnIndented("element " + PREFIX + CALCULATED_WHO + "{ ResourceId.element* }? &");
		writer.printlnIndented("element " + PREFIX + CALCULATED_EXPENSE_ENTRIES + "{ " + EXPENSE_ENTRY + ".element* }? &");
		writer.printlnIndented("element " + PREFIX + CALCULATED_WORK_UNITS_ENTRIES + "{ " + WORK_UNITS_ENTRY + ".element* }?");
		writer.endBlock();
	}
	
	public void writeSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, final String elementType)
	{
		writeElementSchema(baseObjectSchema, fieldSchema, elementType + ".element*");
	}

	private void writeElementSchema(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String elementType)
	{
		String poolName = baseObjectSchema.getXmpz2ElementName();
		String elementName = getTagToElementNameMap().findElementName(poolName, fieldSchema.getTag());
		writeSchemaElement(poolName, elementName, elementType);
	}

	private void writeSchemaElement(String poolName, String elementName, String elementType)
	{
		getSchemaWriter().printIndented("element " + PREFIX + poolName + elementName);
		getSchemaWriter().print(" { " + elementType + " }?");
	}

	private Vector<BaseObjectSchemaWriter> getTopLevelBaseObjectSchemas()
	{
		Vector<BaseObjectSchemaWriter> schemaWriters = new Vector<BaseObjectSchemaWriter>();
		for(int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			if (!isPoolDirectlyInXmpz2(objectType))
				continue;

			BaseObjectPool pool = (BaseObjectPool) getProject().getPool(objectType);
			if(pool == null)
				continue;
			
			BaseObjectSchema baseObjectSchema = pool.createBaseObjectSchema(getProject());
			schemaWriters.add(createSchemaWriter(baseObjectSchema));
		}
		
		return schemaWriters;
	}

	private BaseObjectSchemaWriter createSchemaWriter(BaseObjectSchema baseObjectSchema)
	{
		if (Indicator.is(baseObjectSchema.getType()))
			return new IndicatorSchemaWriter(this, baseObjectSchema);
		
		if (Desire.isDesire(baseObjectSchema.getType()))
			return new DesireSchemaWriter(this, baseObjectSchema);
		
		if (Task.is(baseObjectSchema.getType()))
			return new TaskSchemaWriter(this, baseObjectSchema);
		
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
		  
		  if (Xenodata.is(objectType))
			  return false;
		  
		  if (TableSettings.is(objectType))
			  return false;
		  
		  if (ThreatRatingCommentsData.is(objectType))
			  return false;
		  
		return !Xmpz2XmlImporter.isCustomImport(objectType);
	}
	
	private String getDateUniteTypeName(int objectType)
	{
		if (ExpenseAssignment.is(objectType))
			return EXPENSE;
		
		if (ResourceAssignment.is(objectType))
			return "WorkUnits";
		
		throw new RuntimeException("Object type " + objectType + " cannot have a dateunitEffortsList field");
	}
	
	private ChoiceQuestionToSchemaElementNameMap getChoiceQuestionToSchemaElementNameMap()
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
