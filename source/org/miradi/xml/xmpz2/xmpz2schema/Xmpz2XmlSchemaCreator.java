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

import java.util.Collections;
import java.util.Set;
import java.util.Vector;

import org.miradi.main.Miradi;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.objects.Dashboard;
import org.miradi.objects.Desire;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.FactorLink;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.RatingCriterion;
import org.miradi.objects.ReportTemplate;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;
import org.miradi.objects.TableSettings;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.ViewData;
import org.miradi.objects.Xenodata;
import org.miradi.objects.XslTemplate;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DashboardFlagsQuestion;
import org.miradi.questions.OpenStandardsProgressStatusQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.CostAllocationRuleSchema;
import org.miradi.schemas.FosProjectDataSchema;
import org.miradi.schemas.RareProjectDataSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.schemas.ValueOptionSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.schemas.WcsProjectDataSchema;
import org.miradi.schemas.WwfProjectDataSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.StringUtilities;
import org.miradi.utils.Translation;
import org.miradi.xml.xmpz2.Xmpz2TagToElementNameMap;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;

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
		writeHeader();
		writeConservationProjectElement();
		writeSingletonElements();
		writeBaseObjectSchemaElements();
		writeCodelistSchemaElements();
		writeVocabularyDefinitions();
		writeObjectTypeIdElements();
		writeWrappedByDiagramFactorSchemaElement();
		writeLinkableFactorIds();
		writeHtmlTagSchemaElements();
		writeGeospacialLocationElement();
		writeDiagramPointElement();
		writeDiagramSizeElement();
		writeDateUnitSchemaElements();
		writeThresholdsElement();
		writeTimePeriodCostsElement();
		writeExpenseEntryElement();
		writeWorkUnitsEntryElement();
		writeExternaIdSchemaElement();
		writeSimpleThreatRatingElement();
		writeStressBasedThreatRatingElement();
		writeDiagramFactorUiSettings();
		writeDashboardUserChoiceMap();
		writeDashboardFlagsContainer();
		writeExtraDataSectionElement();
	}

	private void writeHeader()
	{
		getSchemaWriter().writeNamespace(NAME_SPACE);
		getSchemaWriter().defineAlias("start", CONSERVATION_PROJECT + DOT_ELEMENT);
	}

	private void writeConservationProjectElement()
	{
		getSchemaWriter().startElementDefinition(CONSERVATION_PROJECT);

		Vector<String> elementNames = new Vector<String>();
		elementNames.add(createElementName(PROJECT_SUMMARY));
		elementNames.add(createElementName(PROJECT_SUMMARY_SCOPE));
		elementNames.add(createElementName(PROJECT_SUMMARY_LOCATION));
		elementNames.add(createElementName(PROJECT_SUMMARY_PLANNING));
		elementNames.add(createElementName(TNC_PROJECT_DATA));
		elementNames.add(createElementName(WWF_PROJECT_DATA));
		elementNames.add(createElementName(WCS_PROJECT_DATA));
		elementNames.add(createElementName(RARE_PROJECT_DATA));
		elementNames.add(createElementName(FOS_PROJECT_DATA));
		elementNames.add(createElementName(EXTRA_DATA));
		for(BaseObjectSchemaWriter baseObjectSchemaWriter : baseObjectSchemaWriters)
		{
			String poolName = baseObjectSchemaWriter.getPoolName();
			elementNames.add(createElementName(poolName) + " ?");
		}
		
		elementNames.add(ELEMENT_NAME + PREFIX + DELETED_ORPHANS_ELEMENT_NAME +  "{ text }?");
		getSchemaWriter().writeContentsList(elementNames);
		
		getSchemaWriter().endElementDefinition(CONSERVATION_PROJECT);
		getSchemaWriter().flush();
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
		for(int index = 0; index < schemaFieldElements.size(); ++index)
		{
			getSchemaWriter().printIndented(schemaFieldElements.get(index));
			if (index < schemaFieldElements.size() - 1)
				getSchemaWriter().print(" &");
			
			getSchemaWriter().println();
		}
	}
	
	public String writeRelevantSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeElementSchema(baseObjectSchema, fieldSchema, getRelevantTypeName(fieldSchema));
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
	
	public String writeDimensionSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeSchemaElement(baseObjectSchema, fieldSchema, DIAGRAM_SIZE_ELEMENT_NAME);
	}
	
	public String writeDiagramPointSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeSchemaElement(baseObjectSchema, fieldSchema, DIAGRAM_POINT_ELEMENT_NAME);
	}
	
	public String writePointListElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeSchemaElement(baseObjectSchema, fieldSchema, DIAGRAM_POINT_ELEMENT_NAME);
	}
	
	public String writeCalculatedCostSchemaElement(BaseObjectSchema baseObjectSchema)
	{
		return createSchemaElement(baseObjectSchema.getXmpz2ElementName(), TIME_PERIOD_COSTS, TIME_PERIOD_COSTS + DOT_ELEMENT);
	}
	
	public String createThresholdsSchemaElement(BaseObjectSchema baseObjectSchema)
	{
		return createSchemaElement(baseObjectSchema.getXmpz2ElementName(), THRESHOLDS, "IndicatorThreshold.element*");
	}
	
	public String writeStringRefMapSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeSchemaElement(baseObjectSchema, fieldSchema, EXTERNAL_PROJECT_ID_ELEMENT_NAME);
	}
	
	private void writeVocabularyDefinitions()
	{
		Set<ChoiceQuestion> choiceQuestions = choiceQuestionToSchemaElementNameMap.keySet();
		Vector<ChoiceQuestion> sortedQuestions = new Vector<ChoiceQuestion>(choiceQuestions);
		Collections.sort(sortedQuestions);
		for(ChoiceQuestion question : sortedQuestions)
		{
			String vocabularyName = choiceQuestionToSchemaElementNameMap.get(question);
			defineVocabulary(question, vocabularyName);
		}
		
		getSchemaWriter().println("vocabulary_full_project_timespan = xsd:NMTOKEN { pattern = 'Total' } ");
		getSchemaWriter().println("vocabulary_year = xsd:NMTOKEN { pattern = '[0-9]{4}' } ");
		getSchemaWriter().println("vocabulary_month = xsd:integer { minInclusive='1' maxInclusive='12' } ");
		getSchemaWriter().println("vocabulary_date = xsd:NMTOKEN { pattern = '[0-9]{4}-[0-9]{2}-[0-9]{2}' }");
		defineDashboardStatusesVocabulary();
	}
	
	private void writeObjectTypeIdElements()
	{
		writeIdElement(CONCEPTUAL_MODEL);
		writeIdElement(RESULTS_CHAIN);
		writeIdElement(DIAGRAM_FACTOR);
		writeIdElement(DIAGRAM_LINK);
		writeIdElement(BIODIVERSITY_TARGET);
		writeIdElement(HUMAN_WELFARE_TARGET);
		writeIdElement(CAUSE);
		writeIdElement(STRATEGY);
		writeIdElement(THREAT_REDUCTION_RESULTS);
		writeIdElement(INTERMEDIATE_RESULTS);
		writeIdElement(GROUP_BOX);
		writeIdElement(TEXT_BOX);
		writeIdElement(SCOPE_BOX);
		writeIdElement(ACTIVITY);
		writeIdElement(STRESS);
		writeIdElement(GOAL);
		writeIdElement(OBJECTIVE);
		writeIdElement(INDICATOR);
		writeIdElement(KEY_ECOLOGICAL_ATTRIBUTE);
		writeIdElement(TAGGED_OBJECT_SET_ELEMENT_NAME);
		writeIdElement(SUB_TARGET);
		writeIdElement(THREAT);
		writeIdElement(ACCOUNTING_CODE);
		writeIdElement(FUNDING_SOURCE);
		writeIdElement(BUDGET_CATEGORY_ONE);
		writeIdElement(BUDGET_CATEGORY_TWO);
		writeIdElement(PROGRESS_REPORT);
		writeIdElement(PROGRESS_PERCENT);
		writeIdElement(EXPENSE_ASSIGNMENT);
		writeIdElement(RESOURCE_ASSIGNMENT);
		writeIdElement(RESOURCE_ID_ELEMENT_NAME);
		writeIdElement(MEASUREMENT);
		writeIdElement(METHOD);
		writeIdElement(SUB_TASK);
	}
	
	private void writeIdElement(String baseName)
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
		return elementName + DOT_ELEMENT;
	}
	
	public String writeStringSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeElementSchema(baseObjectSchema, fieldSchema, "text");
	}

	public String writeUserTextSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeElementSchema(baseObjectSchema, fieldSchema, "formatted_text");
	}
	
	public String writeBooleanSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeElementSchema(baseObjectSchema, fieldSchema, "xsd:boolean");
	}
	
	public String writeNumberSchemaElement(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema)
	{
		return writeElementSchema(baseObjectSchema, fieldSchema, "xsd:decimal");
	}
	
	public String writeIntegerSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeElementSchema(baseObjectSchema, fieldSchema, "xsd:integer");
	}
	
	public String writeDateSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeElementSchema(baseObjectSchema, fieldSchema, "vocabulary_date");
	}
	
	public String writeRefSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		String fieldName = getIdElementName(baseObjectSchema, fieldSchema);
		return writeSchemaElement(baseObjectSchema, fieldSchema, fieldName);
	}

	private String getIdElementName(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		if (DiagramFactor.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(DiagramFactor.TAG_WRAPPED_REF))
			return WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME;
		
		if (ThreatReductionResult.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF))
			return THREAT_ID;
		
		return getTagToElementNameMap().findElementName(baseObjectSchema, fieldSchema);
	}
	
	public String writeBaseIdSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, int objectType)
	{
		String objectName = getIdElementName(baseObjectSchema, fieldSchema, objectType);
		return writeSchemaElement(baseObjectSchema, fieldSchema, objectName + ID);
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

	public String writeDateUnitEffortListSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return writeSchemaElement(baseObjectSchema, fieldSchema, "DateUnit" + getDateUniteTypeName(baseObjectSchema.getType()));
	}
	
	public String writeChoiceSchemaElement(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, ChoiceQuestion choiceQuestion)
	{
		String vocabularyName = getChoiceQuestionToSchemaElementNameMap().findVocabulary(choiceQuestion);
		return writeElementSchema(baseObjectSchema, fieldSchema, vocabularyName);
	}
	
	public String writeIdListSchemaElement(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema)
	{
		return writeRefListSchemaElement(baseObjectSchema, fieldSchema);
	}

	public String writeRefListSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		String elementName = getTagToElementNameMap().findElementName(baseObjectSchema.getXmpz2ElementName(), fieldSchema.getTag());
		String reflistElementName = baseObjectSchema.getXmpz2ElementName() + elementName;
		final String idElementName = createIdElementName(baseObjectSchema, fieldSchema, elementName);
		return ELEMENT_NAME + PREFIX + reflistElementName + " { " + idElementName + ".element* }?";
	}

	private String createIdElementName(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String elementName)
	{
		if (Task.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(Task.TAG_SUBTASK_IDS))
			return SUB_TASK + ID;
		
		if (Indicator.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(Indicator.TAG_METHOD_IDS))
			return METHOD + ID;
		
		if (Strategy.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(Strategy.TAG_ACTIVITY_IDS))
			return ACTIVITY + ID;
		
		if (DiagramLink.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS))
			return DIAGRAM_LINK + ID;
		
		if (DiagramFactor.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS))
			return DIAGRAM_FACTOR + ID;
		
		if (TaggedObjectSet.is(baseObjectSchema.getType()) && fieldSchema.getTag().equals(TaggedObjectSet.TAG_TAGGED_OBJECT_REFS))
			return WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME;
		
		if (DiagramObject.isDiagramObject(baseObjectSchema.getType()) && fieldSchema.getTag().equals(DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS))
			return TAGGED_OBJECT_SET_ELEMENT_NAME + ID;
				
		return StringUtilities.removeLastChar(elementName);
	}
	
	public String writeCodelistSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, ChoiceQuestion question)
	{
		String elementName = getTagToElementNameMap().findElementName(baseObjectSchema.getXmpz2ElementName(), fieldSchema.getTag());
		String codelistElementName = baseObjectSchema.getXmpz2ElementName() + elementName;
		
		codelistSchemaElements.add(createCodelistSchemaElement(codelistElementName, question));
		
		return codelistElementName + "Container.element ?";
	}
	
	private String createCodelistSchemaElement(String codelistElementName, ChoiceQuestion question)
	{
		String vocabularyName = getChoiceQuestionToSchemaElementNameMap().get(question);
		String containerElement = codelistElementName + "Container.element = element " +  PREFIX + codelistElementName + "Container " + HtmlUtilities.NEW_LINE;
		containerElement += "{" + HtmlUtilities.NEW_LINE;
		containerElement += getSchemaWriter().INDENTATION + ELEMENT_NAME + PREFIX + "code { " + vocabularyName + " } *" + HtmlUtilities.NEW_LINE;
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
	
	private void writeLinkableFactorIds()
	{
		final String[] factorNames = new String[]{
				  BIODIVERSITY_TARGET, 
				  HUMAN_WELFARE_TARGET, 
				  CAUSE, 
				  STRATEGY, 
				  THREAT_REDUCTION_RESULTS, 
				  INTERMEDIATE_RESULTS,
				  GROUP_BOX,
				};
		
		writeOredSchemaElements(LINKABLE_FACTOR_ID, factorNames);
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
		
		writeOredSchemaElements(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME, factorNames);
	}

	private void writeOredSchemaElements(final String elementName, final String[] elementNames)
	{
		getSchemaWriter().startElementDefinition(elementName);
		for (int index = 0; index < elementNames.length; ++index)
		{
			getSchemaWriter().printIndented(elementNames[index] + ID + DOT_ELEMENT);
			if (index < elementNames.length - 1)
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
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + "latitude { xsd:decimal } &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + "longitude { xsd:decimal } ");
		getSchemaWriter().endBlock();
	}
	
	private void writeDiagramPointElement()
	{
		getSchemaWriter().startElementDefinition(DIAGRAM_POINT_ELEMENT_NAME);
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + "x { xsd:integer } & ");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + "y { xsd:integer }"); 
		getSchemaWriter().endBlock();
	}
	
	private void writeDiagramSizeElement()
	{
		getSchemaWriter().startElementDefinition(DIAGRAM_SIZE_ELEMENT_NAME);
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + "width { xsd:integer } & ");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + "height { xsd:integer }"); 
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
	
	private void writeDashboardUserChoiceMap()
	{
		getSchemaWriter().defineAlias(DASHBOARD_STATUS_ENTRY + DOT_ELEMENT, ELEMENT_NAME + PREFIX + DASHBOARD_STATUS_ENTRY);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented("attribute " + KEY_ATTRIBUTE_NAME + " { text } &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + DASHBOARD_PROGRESS + " { " + VOCABULARY_DASHBOARD_ROW_PROGRESS + " }? &");
		getSchemaWriter().printlnIndented(DASHBOARD + DASHBOARD_FLAGS + CONTAINER_ELEMENT_TAG + ".element? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + DASHBOARD_COMMENTS + " { text }?");
		getSchemaWriter().endBlock();
	}
	
	private void writeDashboardFlagsContainer()
	{
		String falgsContainerSchema = createCodelistSchemaElement("DashboardFlags", StaticQuestionManager.getQuestion(DashboardFlagsQuestion.class));
		getSchemaWriter().println(falgsContainerSchema);
	}
	
	private void writeExtraDataSectionElement()
	{
		writer.defineAlias(EXTRA_DATA_SECTION + DOT_ELEMENT, ELEMENT_NAME + PREFIX + EXTRA_DATA_SECTION);
		writer.startBlock();
		writer.printlnIndented("attribute " + EXTRA_DATA_SECTION_OWNER_ATTRIBUTE + " { text } &");
		writer.printlnIndented(EXTRA_DATA_ITEM + ".element *");
		writer.endBlock();
		
		defineExtraDataItemElement();
	}

	private void defineExtraDataItemElement()
	{
		writer.defineAlias(EXTRA_DATA_ITEM + DOT_ELEMENT, ELEMENT_NAME + PREFIX + EXTRA_DATA_ITEM);
		writer.startBlock();
		writer.printlnIndented("attribute " + EXTRA_DATA_ITEM_NAME + " { text } &");
		writer.printlnIndented(ELEMENT_NAME + PREFIX + EXTRA_DATA_ITEM_VALUE + " { text }?");
		writer.endBlock();
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
		writer.defineAlias("DateUnitWorkUnits.element", ELEMENT_NAME + PREFIX + "DateUnitWorkUnits");
		writer.startBlock();
		writer.printlnIndented(ELEMENT_NAME + PREFIX + "WorkUnitsDateUnit{WorkUnitsDay.element | WorkUnitsMonth.element | WorkUnitsQuarter.element | WorkUnitsYear.element | WorkUnitsFullProjectTimespan.element }? &");
		writer.printlnIndented(ELEMENT_NAME + PREFIX + "NumberOfUnits { xsd:decimal }?");
		writer.endBlock();
	}

	private void defineDateUnitExpense()
	{
		writer.defineAlias("DateUnitExpense.element", ELEMENT_NAME + PREFIX + "DateUnitExpense");
		writer.startBlock();
		writer.printlnIndented(ELEMENT_NAME + PREFIX + EXPENSES_DATE_UNIT + "{ExpensesDay.element | ExpensesMonth.element | ExpensesQuarter.element | ExpensesYear.element | ExpensesFullProjectTimespan.element }? &");
		writer.printlnIndented(ELEMENT_NAME + PREFIX + EXPENSE + " { xsd:decimal }?");
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
		getSchemaWriter().defineAlias(elementName + DOT_ELEMENT, "element miradi:" + elementName);
		getSchemaWriter().startBlock();
		for (int index = 0; index < subElements.length; ++index)
		{
			if (index > 0)
				getSchemaWriter().println(" &");
			
			getSchemaWriter().printIndented(subElements[index]);
		}
		
		getSchemaWriter().println();
		getSchemaWriter().endBlock();
	}
	
	private void writeThresholdsElement()
	{
		getSchemaWriter().defineAlias(THRESHOLD + DOT_ELEMENT, ELEMENT_NAME + PREFIX + THRESHOLD);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + STATUS_CODE + "{" + VOCABULARY_MEASUREMENT_STATUS + "}? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + THRESHOLD_VALUE + " { text }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + THRESHOLD_DETAILS + " { text }?");
		getSchemaWriter().endBlock();
	}

	private void writeTimePeriodCostsElement()
	{
		getSchemaWriter().defineAlias(TIME_PERIOD_COSTS + DOT_ELEMENT, ELEMENT_NAME + PREFIX + TIME_PERIOD_COSTS);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + CALCULATED_START_DATE + "{ vocabulary_date } &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + CALCULATED_END_DATE + "{ vocabulary_date } &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + CALCULATED_EXPENSE_TOTAL + "{ xsd:decimal }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + CALCULATED_WORK_UNITS_TOTAL + "{ xsd:decimal }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + CALCULATED_TOTAL_BUDGET_COST + "{ xsd:decimal }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + CALCULATED_WHO + "{ ResourceId.element* }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + CALCULATED_EXPENSE_ENTRIES + "{ " + EXPENSE_ENTRY + ".element* }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + CALCULATED_WORK_UNITS_ENTRIES + "{ " + WORK_UNITS_ENTRY + ".element* }?");
		getSchemaWriter().endBlock();
	}
	
	private void writeExpenseEntryElement()
	{
		getSchemaWriter().defineAlias(EXPENSE_ENTRY + DOT_ELEMENT, ELEMENT_NAME + PREFIX + EXPENSE_ENTRY);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + EXPENSE_ENTRY + FUNDING_SOURCE_ID + "{ " + FUNDING_SOURCE_ID + ".element }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + EXPENSE_ENTRY + ACCOUNTING_CODE_ID + "{ " + ACCOUNTING_CODE_ID + ".element }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + EXPENSE_ENTRY + BUDGET_CATEGORY_ONE_ID + "{ " + BUDGET_CATEGORY_ONE_ID + ".element }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + EXPENSE_ENTRY + BUDGET_CATEGORY_TWO_ID + "{ " + BUDGET_CATEGORY_TWO_ID + ".element }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + EXPENSE_ENTRY + DETAILS + "{ " + DATE_UNITS_EXPENSE + ".element* }?");
		getSchemaWriter().endBlock();
	}
	
	private void writeWorkUnitsEntryElement()
	{
		getSchemaWriter().defineAlias(WORK_UNITS_ENTRY + DOT_ELEMENT, ELEMENT_NAME + PREFIX + WORK_UNITS_ENTRY);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + WORK_UNITS_ENTRY + RESOURCE_ID + "{ " + RESOURCE_ID + ".element }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + WORK_UNITS_ENTRY + FUNDING_SOURCE_ID + "{ " + FUNDING_SOURCE_ID + ".element }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + WORK_UNITS_ENTRY + ACCOUNTING_CODE_ID + "{ " + ACCOUNTING_CODE_ID + ".element }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + WORK_UNITS_ENTRY + BUDGET_CATEGORY_ONE_ID + "{ " + BUDGET_CATEGORY_ONE_ID + ".element }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + WORK_UNITS_ENTRY + BUDGET_CATEGORY_TWO_ID + "{ " + BUDGET_CATEGORY_TWO_ID + ".element }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + WORK_UNITS_ENTRY + DETAILS + "{ " + DATE_UNIT_WORK_UNITS + ".element* }?");
		getSchemaWriter().endBlock();
	}
	
	private void writeExternaIdSchemaElement()
	{
		getSchemaWriter().defineAlias(EXTERNAL_PROJECT_ID_ELEMENT_NAME + DOT_ELEMENT, ELEMENT_NAME + PREFIX + EXTERNAL_PROJECT_ID_ELEMENT_NAME);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + EXTERNAL_APP_ELEMENT_NAME + " { text } &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + PROJECT_ID + " { text } ");
		getSchemaWriter().endBlock();
	}
	
	private void writeSimpleThreatRatingElement()
	{
		getSchemaWriter().defineAlias(SIMPLE_BASED_THREAT_RATING + DOT_ELEMENT	, ELEMENT_NAME + PREFIX + SIMPLE_BASED_THREAT_RATING);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + SIMPLE_BASED_THREAT_RATING + SCOPE + " { " + VOCABULARY_SIMPLE_THREAT_RATING_SCOPE_CODE + " }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + SIMPLE_BASED_THREAT_RATING + SEVERITY + " { " + VOCABULARY_SIMPLE_THREAT_RATING_SEVERITY_CODE +" }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + SIMPLE_BASED_THREAT_RATING + IRREVERSIBILITY + " { " + VOCABULARY_SIMPLE_THREAT_RATING_IRREVERSIBILITY_CODE + " }?");
		getSchemaWriter().endBlock();
    }

	private void writeStressBasedThreatRatingElement()
	{
		getSchemaWriter().defineAlias(STRESS_BASED_THREAT_RATING + DOT_ELEMENT, ELEMENT_NAME + PREFIX + STRESS_BASED_THREAT_RATING);
		getSchemaWriter().startBlock();
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + STRESS_BASED_THREAT_RATING + "StressId{ StressId.element } &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + STRESS_BASED_THREAT_RATING + CONTRIBUTION + " { " + VOCABULARY_THREAT_STRESS_RATING_CONTRIBUTION_CODE + " }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + STRESS_BASED_THREAT_RATING + IRREVERSIBILITY + " { " + VOCABULARY_THREAT_STRESS_RATING_IRREVERSIBILITY_CODE + " }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + STRESS_BASED_THREAT_RATING + IS_ACTIVE + " { xsd:boolean }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + STRESS_BASED_THREAT_RATING + STRESS_RATING + " { " + VOCABULARY_THREAT_RATING + " }? &");
		getSchemaWriter().printlnIndented(ELEMENT_NAME + PREFIX + STRESS_BASED_THREAT_RATING + THREAT_STRESS_RATING + " { " + VOCABULARY_THREAT_RATING + " }? ");
		getSchemaWriter().endBlock();
	}
	
	private void writeDiagramFactorUiSettings()
	{
		writer.defineAlias(STYLING + DOT_ELEMENT	, ELEMENT_NAME + PREFIX + STYLING);
		writer.startBlock();
		writer.printlnIndented(ELEMENT_NAME + PREFIX + DIAGRAM_FACTOR + DIAGRAM_FACTOR_FONT_SIZE_ELEMENT_NAME + " { " + ChoiceQuestionToSchemaElementNameMap.VOCABULARY_DIAGRAM_FACTOR_FONT_SIZE + " }? &");
		writer.printlnIndented(ELEMENT_NAME + PREFIX + DIAGRAM_FACTOR + DIAGRAM_FACTOR_FONT_STYLE_ELEMENT_NAME + " { " + ChoiceQuestionToSchemaElementNameMap.VOCABULARY_DIAGRAM_FACTOR_FONT_STYLE + " }? &");
		writer.printlnIndented(ELEMENT_NAME + PREFIX + DIAGRAM_FACTOR + DIAGRAM_FACTOR_FOREGROUND_COLOR_ELEMENT_NAME + " { " + ChoiceQuestionToSchemaElementNameMap.VOCABULARY_DIAGRAM_FACTOR_FOREGROUND_COLOR + " }? &");
		writer.printlnIndented(ELEMENT_NAME + PREFIX + DIAGRAM_FACTOR + DIAGRAM_FACTOR_BACKGROUND_COLOR_ELEMENT_NAME + " { " + ChoiceQuestionToSchemaElementNameMap.VOCABULARY_DIAGRAM_FACTOR_BACKGROUND_COLOR + " }?");
		writer.endBlock();		
	}

	public String writeSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, final String elementType)
	{
		return writeElementSchema(baseObjectSchema, fieldSchema, elementType + ".element*");
	}

	private String writeElementSchema(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String elementType)
	{
		String poolName = baseObjectSchema.getXmpz2ElementName();
		String elementName = getTagToElementNameMap().findElementName(poolName, fieldSchema.getTag());
		return createSchemaElement(poolName, elementName, elementType);
	}

	public String createSchemaElement(String poolName, String elementName, String elementType)
	{
		return ELEMENT_NAME + PREFIX + poolName + elementName + " { " + elementType + " }?";
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
		
		schemaWriters.add(new ThreatTargetThreatRatingSchemaWriter(this));
		schemaWriters.add(new ExtraDataWriter(this));
		
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
		
		if (Dashboard.is(baseObjectSchema.getType()))
			return new DashboardSchemaWriter(this, baseObjectSchema);
		
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

		if (ThreatRatingCommentsData.is(objectType))
			return false;

		if (RatingCriterion.is(objectType))
			return false;

		if (ValueOptionSchema.getObjectType() == objectType)
			return false;

		if (ReportTemplate.is(objectType))
			return false;

		if (XslTemplate.is(objectType))
			return false;

		return true;
	}
	
	private String getDateUniteTypeName(int objectType)
	{
		if (ExpenseAssignment.is(objectType))
			return EXPENSE;
		
		if (ResourceAssignment.is(objectType))
			return "WorkUnits";
		
		throw new RuntimeException("Object type " + objectType + " cannot have a dateunitEffortsList field");
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
