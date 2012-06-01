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
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.FactorLink;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.TableSettings;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.ViewData;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.CostAllocationRuleSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.Translation;
import org.miradi.xml.wcs.XmpzXmlConstants;
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
		writeBaseObjectSchemaElements();
		writeCodelistSchemaElements();
		writeVocabularyDefinitions();
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
			elementNames.add(createElementName(poolName) + " ? ");
		}
		
		getSchemaWriter().writeContentsList(elementNames);
		
		getSchemaWriter().endElementDefinition(CONSERVATION_PROJECT);
		getSchemaWriter().flush();
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
	
	private void writeCodelistSchemaElements()
	{
		for (String codelistSchemaElement : codelistSchemaElements)
		{
			getSchemaWriter().printIndented(codelistSchemaElement);
			getSchemaWriter().println();
		}
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
	}
	
	public void writeIdAttribute()
	{
		getSchemaWriter().printIndented("attribute " + ID + " "+ "{xsd:integer} &");
	}

	private void writeBaseObjectSchemaHeader(BaseObjectSchemaWriter baseObjectSchemaWriter)
	{
		String baseObjectName = baseObjectSchemaWriter.getXmpz2ElementName();
		String baseObjectPoolName = baseObjectSchemaWriter.getPoolName();
		
		String result = XmpzXmlConstants.ELEMENT_NAME + XmpzXmlConstants.PREFIX + baseObjectPoolName + " { " + createElementName(baseObjectName) + "* }";
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
		String objectName = getProject().getObjectManager().getInternalObjectTypeName(objectType);
		writeSchemaElement(baseObjectSchema, fieldSchema, objectName + ID);
	}

	public void writeDateUnitEffortListSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		writeSchemaElement(baseObjectSchema, fieldSchema, "DateUnit" + getDateUniteTypeName(baseObjectSchema.getType()));
	}
	
	public void writeChoiceSchemaElement(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, ChoiceQuestion choiceQuestion)
	{
		String vocabularyName = getChoiceQuestionToSchemaElementNameMap().get(choiceQuestion.getClass());
		writeElementSchema(baseObjectSchema, fieldSchema, vocabularyName);
	}

	//FIXME urgent - method is not complete
	public void writeRefListSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		String elementName = getTagToElementNameMap().findElementName(baseObjectSchema.getObjectName(), fieldSchema.getTag());
		String reflistElementName = baseObjectSchema.getXmpz2ElementName() + elementName;
		//element miradi:ConceptualModelSelectedTaggedObjectSetIds { TaggedObjectSetId.element* }
		getSchemaWriter().printIndented("element " + PREFIX + reflistElementName);
	}
	
	public void writeCodelistSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, ChoiceQuestion question)
	{
		String elementName = getTagToElementNameMap().findElementName(baseObjectSchema.getObjectName(), fieldSchema.getTag());
		String codelistElementName = baseObjectSchema.getXmpz2ElementName() + elementName;
		getSchemaWriter().printIndented(codelistElementName + "Container.element ?");
		
		codelistSchemaElements.add(createCodelistSchemaElement(codelistElementName, question));
	}
	
	private String createCodelistSchemaElement(String codelistElementName, ChoiceQuestion question)
	{
		String vocabularyName = getChoiceQuestionToSchemaElementNameMap().get(question.getClass());
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
	
	public void writeSchemaElement(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, final String elementType)
	{
		writeElementSchema(baseObjectSchema, fieldSchema, elementType + ".element*");
	}

	private void writeElementSchema(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String elementType)
	{
		String poolName = baseObjectSchema.getXmpz2ElementName();
		String elementName = getTagToElementNameMap().findElementName(baseObjectSchema.getObjectName(), fieldSchema.getTag());
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
			schemaWriters.add(new BaseObjectSchemaWriter(this, baseObjectSchema));
		}
		
		return schemaWriters;
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
			return WORK_UNITS;
		
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
