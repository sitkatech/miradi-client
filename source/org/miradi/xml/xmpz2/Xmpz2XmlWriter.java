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

package org.miradi.xml.xmpz2;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.Set;

import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objecthelpers.TaxonomyClassificationMap;
import org.miradi.objecthelpers.TaxonomyElement;
import org.miradi.objecthelpers.TaxonomyElementList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.InternalQuestionWithoutValues;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.FieldSchemaIdList;
import org.miradi.schemas.FieldSchemaReflist;
import org.miradi.utils.CodeList;
import org.miradi.utils.PointList;
import org.miradi.utils.XmlUtilities2;

public class Xmpz2XmlWriter implements Xmpz2XmlConstants
{
	public Xmpz2XmlWriter(Project projectToUse, UnicodeWriter writerToUse) throws Exception
	{
		project = projectToUse;
		writer = writerToUse;
		tagToElementNameMap = new Xmpz2TagToElementNameMap();
	}
	
	public void writeStartPoolElement(String startElementName) throws Exception
	{
		writeStartElement(createPoolElementName(startElementName));
	}

	public void writeEndPoolElement(String endElementName) throws Exception
	{
		writeEndElement(createPoolElementName(endElementName));
	}
	
	public static String createPoolElementName(String startElementName)
	{
		return startElementName + POOL_ELEMENT_TAG;
	}
	
	public void writeCodeElement(final String parentElementName, String elementName, ChoiceItem rating) throws Exception
	{
		final String code = rating.getCode();
		writeCodeElement(parentElementName, elementName, code);
	}

	public void writeOptionalCodeElement(final String parentElementName, String elementName, final String code) throws Exception
	{
		if (code.length() == 0)
			return;
		
		writeCodeElement(parentElementName, elementName, code);
	}
	
	public void writeCodeElement(final String parentElementName, String elementName, final String code) throws Exception
	{
		writeStartElement(parentElementName + elementName);
		writeXmlText(code);
		writeEndElement(parentElementName + elementName);
	}
	
	public void writeChoiceData(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema, final ChoiceData choiceData) throws Exception
	{
		String readableCode = getReadableCode(choiceData);
		
		writeField(baseObjectSchema, fieldSchema, readableCode);
	}
	
	public void writeChoiceData(final String elementName, final BaseObject baseObject, final String tag) throws Exception
	{
		ObjectData choiceData = baseObject.getField(tag);
		String readableCode = getReadableCode(choiceData);
		
		writeElement(elementName, readableCode);
	}

	private String getReadableCode(ObjectData choiceData) throws Exception
	{
		if (!choiceData.isChoiceItemData())
			throw new Exception("Expecting a ChoiceData object!");
		
		ChoiceQuestion question = choiceData.getChoiceQuestion();
		String code = choiceData.get();
		
		return question.convertToReadableCode(code);
	}

	public void writeStringData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, string);
	}

	public void writeCodeListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, ChoiceQuestion questionToUse, String codeListAsString) throws Exception
	{
		CodeList codes = new CodeList(codeListAsString);
		final String elementName = appendChildNameToParentName(baseObjectSchema, fieldSchema);
		writeCodeList(elementName, questionToUse, codes);
	}

	private void writeCodeList(final String elementName, CodeList codes)	throws Exception
	{
		writeCodeList(elementName, new InternalQuestionWithoutValues(), codes);
	}
	
	public void writeCodeList(final String elementName, ChoiceQuestion questionToUse, CodeList codes)	throws Exception
	{
		if (codes.hasData())
			writeNonOptionalCodeListElement(elementName, questionToUse, codes);
	}

	public void writeNonOptionalCodeListElement(final String elementName, ChoiceQuestion questionToUse, final CodeList codes)	throws Exception
	{
		CodeList readableCodes = convertToReadableCodes(questionToUse, codes);
		writeNonOptionalCodeListElement(elementName, readableCodes);
	}

	public void writeNonOptionalCodeListElement(final String elementName, final CodeList codes) throws Exception
	{
		final String elementContainerName = createContainerElementName(elementName);
		writeStartElement(elementContainerName);
		for(String code : codes)
		{
			writeElement(CODE_ELEMENT_NAME, code);
		}
		
		writeEndElement(elementContainerName);
	}
	
	private CodeList convertToReadableCodes(ChoiceQuestion questionToUse, CodeList internalCodes)
	{
		CodeList readableCodes = new CodeList();
		for (String internalCode : internalCodes)
		{
			String readableCode = questionToUse.convertToReadableCode(internalCode);
			readableCodes.add(readableCode);
		}
		
		return readableCodes;
	}

	public void writeDateData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String isoDate) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, isoDate);
	}

	public void writeDimensionData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, Dimension dimension) throws Exception
	{
		final String sizeElementName = appendChildNameToParentName(baseObjectSchema.getObjectName(), SIZE);
		writeStartElement(sizeElementName);
		writeStartElement(DIAGRAM_SIZE_ELEMENT_NAME);
		writeElement(WIDTH_ELEMENT_NAME, dimension.width);		
		writeElement(HEIGHT_ELEMENT_NAME, dimension.height);
		writeEndElement(DIAGRAM_SIZE_ELEMENT_NAME);
		writeEndElement(sizeElementName);
	}

	public void writeFloatData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String number) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, number);
	}

	public void writeIntegerData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String number) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, number);
	}

	public void writeBooleanData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String booleanAsString) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, booleanAsString);
	}
	
	public void writeBooleanElement(final String elementName, final boolean isTrue) throws Exception
	{
		String booleanAsString = "0";
		if (isTrue)
			booleanAsString = BooleanData.BOOLEAN_TRUE;
		
		writeElement(elementName, booleanAsString);
	}

	public void writeNumberData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String number) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, number);
	}

	public void writePercentageData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String number) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, number);
	}

	public void writeRefDataIfValid(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema, final ORef ref) throws Exception
	{
		final String parentElementName = appendChildNameToParentName(baseObjectSchema, fieldSchema);
		final String idName = getConvertedElementName(baseObjectSchema.getObjectName(), fieldSchema.getTag());

		writeRef(ref, parentElementName, idName);
	}

	public void writeRefIfValid(final String parentElementName, final String idElementName, final ORef ref) throws Exception, IOException
	{
		writeRef(ref, parentElementName + idElementName, idElementName);
	}
	
	public void writeRef(final ORef ref, final String parentElementName, final String idElementName) throws Exception, IOException
	{
		if (ref.isValid())
		{
			writeStartElement(parentElementName);
			writeStartElement(idElementName);
			write(ref.getObjectId().toString());
			writeEndElement(idElementName);
			writeEndElement(parentElementName);
		}
	}

	public void writeDiagramPointData(BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema, final Point point) throws Exception
	{
		final String elementName = appendChildNameToParentName(baseObjectSchema, fieldSchema);
		writeStartElement(elementName);
		writeDiagramPoint(elementName, point);
		writeEndElement(elementName);
	}

	public void writeBendPointListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, PointList points) throws Exception
	{
		if (points.size() > 0)
			writeBendPointList(baseObjectSchema.getObjectName(), points);
	}

	private void writeBendPointList(final String objectTypeName, PointList points) throws Exception
	{
		final String elemnentName = objectTypeName + BEND_POINTS_ELEMENT_NAME;
		writeStartElement(elemnentName);
		for (int index = 0; index < points.size(); ++index)
		{
			writeDiagramPoint(DIAGRAM_POINT_ELEMENT_NAME, points.get(index));
		}
		
		writeEndElement(elemnentName);
	}
	
	public void writeDiagramPoint(final String pointElementName, Point point) throws Exception
	{
		writeStartElement(DIAGRAM_POINT_ELEMENT_NAME);
		writeElement(X_ELEMENT_NAME, point.x);
		writeElement(Y_ELEMENT_NAME, point.y);
		writeEndElement(DIAGRAM_POINT_ELEMENT_NAME);
	}

	public void writePseudoQuestionData(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, String string)
	{
		//NOTE: Don't export pseudodata
	}

	public void writePseudoRefListData(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema, String string) throws Exception, Exception
	{
		writeRefList(baseObjectSchema, fieldSchema, new ORefList(string));
	}

	public void writeIdListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, ORefList refList) throws Exception
	{
		FieldSchemaIdList fieldSchemaIdList = (FieldSchemaIdList) fieldSchema;
		final String elementTypeName = project.getObjectManager().getInternalObjectTypeName(fieldSchemaIdList.getIdListType());
		final String elementContainerName = appendChildNameToParentName(baseObjectSchema, fieldSchema);
		writeReflist(elementContainerName, elementTypeName, refList);
	}
	
	public void writeRefList(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, ORefList refListToUse) throws Exception
	{
		final String idElementName = ((FieldSchemaReflist) fieldSchema).getTypeName();
		final String elementName = appendChildNameToParentName(baseObjectSchema, fieldSchema);
		writeReflist(elementName, idElementName, refListToUse);
	}
	
	public void writeReflist(final String parentName, final String elementContainerName, final String elementTypeName, ORefList refListToUse) throws Exception
	{
		final String elementName = appendChildNameToParentName(parentName, elementContainerName);
		writeReflist(elementName, elementTypeName, refListToUse);
	}
	
	public void writeReflist(final String elementContainerName, final String elementTypeName, ORefList refListToUse) throws Exception
	{
		if (refListToUse.isEmpty())
			return;
		
		writeNonOptionalReflist(elementContainerName, elementTypeName, refListToUse);
	}

	public void writeNonOptionalReflist(final String elementContainerName, final String elementTypeName, ORefList refListToUse) throws Exception
	{
		final String elementName = convertRefsToIdsSuffix(elementContainerName);
		writeStartElement(elementName);
		for(ORef ref : refListToUse)
		{
			writeElement(elementTypeName + ID_ELEMENT_NAME, ref.getObjectId().toString());
		}
		
		writeEndElement(elementName);
	}
	
	private String convertRefsToIdsSuffix(final String elementContainerName)
	{
		return elementContainerName.replaceAll(REFS, IDS);
	}

	public void writeXmlFormattedText(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String xmlFormattedText) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, xmlFormattedText);
	}

	public void writeRefMapXenoData(String stringRefMapAsString) throws Exception
	{
		writeStartElement(appendChildNameToParentName(PROJECT_SUMMARY, Xenodata.TAG_PROJECT_ID));

		StringRefMap stringRefMap = new StringRefMap(stringRefMapAsString);
		Set<String> keys = stringRefMap.getKeys();
		for(String key: keys)
		{
			ORef xenodataRef = stringRefMap.getValue(key);
			if (xenodataRef.isInvalid())
			{
				EAM.logWarning("Invalid Xenodata ref found for key: " + key + " while exporting.");
				continue;
			}

			Xenodata xenodata = Xenodata.find(getProject(), xenodataRef);
			String projectId = xenodata.getData(Xenodata.TAG_PROJECT_ID);
			writeStartElement(EXTERNAL_PROJECT_ID_ELEMENT_NAME);
			writeElement(EXTERNAL_APP_ELEMENT_NAME, key);
			writeRequiredElement(PROJECT_ID, projectId);
			writeEndElement(EXTERNAL_PROJECT_ID_ELEMENT_NAME);
		}
		
		writeEndElement(appendChildNameToParentName(PROJECT_SUMMARY, Xenodata.TAG_PROJECT_ID));
	}
	
	public void writeTaxonomyElementList(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, TaxonomyElementList taxonomyElementList) throws Exception
	{
		writeStartElement(TAXONOMY_ELEMENTS);
		for(TaxonomyElement taxonomyElement : taxonomyElementList)
		{
			writeTaxonomyElement(taxonomyElement);
		}
		writeEndElement(TAXONOMY_ELEMENTS);
	}
	
	private void writeTaxonomyElement(TaxonomyElement taxonomyElement) throws Exception
	{
		writeStartElementWithAttribute(TAXONOMY_ELEMENT, TAXONOMY_ELEMENT_CODE, taxonomyElement.getCode());
		writeRequiredElement(TAXONOMY_ELEMENT_USER_CODE, taxonomyElement.getUserCode());
		writeRequiredElement(TAXONOMY_ELEMENT_LABEL, taxonomyElement.getLabel());
		writeRequiredElement(TAXONOMY_ELEMENT_DESCRIPTION, taxonomyElement.getDescription());
		writeCodeList(TAXONOMY_ELEMENT_CHILD_CODE, taxonomyElement.getChildCodes());
		
		writeEndElement(TAXONOMY_ELEMENT);
	}
	
	public void writeTaxonomyClassifications(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, TaxonomyClassificationMap taxonomyClassificationData) throws Exception
	{
		writeStartElement(TAXONOMY_CLASSIFICATION_CONTAINER);
		Set<String> taxonomyCodes = taxonomyClassificationData.toHashMap().keySet();
		for(String taxonomyCode : taxonomyCodes)
		{
			CodeList taxonomyElementCodes = taxonomyClassificationData.getCodeList(taxonomyCode);
			writeTaxonomyClassification(taxonomyCode, taxonomyElementCodes);
		}
		
		writeEndElement(TAXONOMY_CLASSIFICATION_CONTAINER);
	}

	private void writeTaxonomyClassification(String taxonomyCode, CodeList taxonomyElementCodes) throws Exception
	{
		writeStartElement(TAXONOMY_CLASSIFICATION);
		writeElement(TAXONOMY_CLASSIFICATION_TAXONOMY_CODE, taxonomyCode);
		writeCodeList(TAXONOMY_CLASSIFICATION_TAXONOMY_ELEMENT_CODE, taxonomyElementCodes);
		writeEndElement(TAXONOMY_CLASSIFICATION);
	}

	private void writeField(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String data) throws Exception
	{
		final String elementName = appendChildNameToParentName(baseObjectSchema, fieldSchema);
		writeElement(elementName, data);
	}
	
	private String appendChildNameToParentName(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema)
	{
		final String xmpz2ElementName = baseObjectSchema.getXmpz2ElementName();
		final String tag = fieldSchema.getTag();
		return appendChildNameToParentName(xmpz2ElementName, tag);
	}

	public String appendChildNameToParentName(final String parentElementName,	final String childElementName)
	{
		return parentElementName + getConvertedElementName(parentElementName, childElementName);
	}
	
	public static String createContainerElementName(String elementName)
	{
		return elementName + CONTAINER_ELEMENT_TAG;
	}
	
	public void writeXmlHeader() throws Exception
	{
		writeln(XML_HEADER);
	}
	
	public void writeMainElementStart() throws Exception
	{
		writeStartElementWithAttribute(CONSERVATION_PROJECT, XMLNS + COLON + RAW_PREFIX, NAME_SPACE);
		writeln();
	}
	
	public void writeDay(DateUnit dateUnit, final String dayElementName) throws Exception
	{
		writeDatalessElementWithAttribute(dayElementName, DATE, dateUnit.toString());
	}

	public void writeMonth(DateUnit dateUnit, final String monthElementName) throws Exception
	{
		writeDatalessElementWithTwoAttributes(monthElementName, YEAR, dateUnit.getYear(), MONTH, dateUnit.getMonth());
	}

	public void writeQuarter(DateUnit dateUnit, final String quarterElementName) throws Exception
	{
		MultiCalendar start = dateUnit.getQuarterDateRange().getStartDate();
		writeDatalessElementWithTwoAttributes(quarterElementName, YEAR, start.getGregorianYear(), START_MONTH, start.getGregorianMonth());
	}

	public void writeYear(DateUnit dateUnit, final String yearElementName) throws Exception
	{
		final int yearStartMonth = dateUnit.getYearStartMonth();
		final int startYear = Integer.parseInt(dateUnit.getYearYearString());
		writeDatalessElementWithTwoAttributes(yearElementName, START_YEAR, startYear, START_MONTH, yearStartMonth);
	}

	public void writeProjectTotal(DateUnit dateUnit, final String fullProjectTimespanElementName) throws Exception
	{		
		writeDatalessElementWithAttribute(fullProjectTimespanElementName, FULL_PROJECT_TIMESPAN, TOTAL);
	}
	
	public void writeDatalessElementWithAttribute(final String elementName, final String attributeName, final String attributeValue) throws Exception
	{
		writeStartElementWithAttribute(elementName, attributeName, attributeValue);
		writeEndElement(elementName);
	}
	
	public void writeDatalessElementWithTwoAttributes(final String elementName, final String attributeName1, final int attributeValue1, final String attributeName2, final int attributeValue2) throws Exception
	{
		writeStartElementWithTwoAttributes(elementName, attributeName1, attributeValue1, attributeName2, attributeValue2);
		writeEndElement(elementName);
	}
	
	public void writeStartElementWithAttribute(String startElementName, String attributeName, String attributeValue) throws Exception
	{
		attributeValue = XmlUtilities2.getXmlEncoded(attributeValue);
		write("<" + PREFIX + startElementName + " " + attributeName + "=\"" + attributeValue + "\">");
	}
	
	public void writeStartElementWithTwoAttributes(String startElementName, String attributeName1, int attributeValue1, String attributeName2, int attributeValue2) throws Exception
	{
		write("<" + PREFIX + startElementName + " " + attributeName1 + "=\"" + Integer.toString(attributeValue1) + "\" " + attributeName2 + "=\"" + Integer.toString(attributeValue2) + "\">");
	}
	
	public void writeMainElementEnd() throws Exception
	{
		writeEndElement(CONSERVATION_PROJECT);
	}
	
	public void writeObjectElementStart(final BaseObject baseObject) throws Exception
	{
		writeObjectStartElementWithAttribute(baseObject, ID, baseObject.getId().toString());
	}

	public void writeObjectStartElementWithAttribute(final BaseObject baseObject, final String attributeName, final String attributeValue) throws Exception
	{
		BaseObjectSchema baseObjectSchema = baseObject.getSchema();
		writeStartElementWithAttribute(baseObjectSchema.getXmpz2ElementName(), attributeName, attributeValue);
	}
	
	public void writeObjectElementStartWithoutAttribute(final BaseObject baseObject) throws Exception
	{
		BaseObjectSchema baseObjectSchema = baseObject.getSchema();
		writeStartElement(baseObjectSchema.getXmpz2ElementName());
	}

	public void writeObjectElementEnd(final BaseObjectSchema baseObjectSchema) throws Exception
	{
		writeEndElement(baseObjectSchema.getXmpz2ElementName());
	}
	
	public void writeFieldElement(final BaseObject baseObject, final AbstractFieldSchema fieldSchema) throws Exception
	{
		if (fieldSchema.isPseudoField())
			return;

		ObjectData field = baseObject.getField(fieldSchema.getTag());
		field.writeAsXmpz2XmlData(this, baseObject.getSchema(), fieldSchema);
	}
	
	public void writeElement(String parentElementName, BaseObject object, String tag) throws Exception
	{
		writeElement(parentElementName, tag, object.getData(tag));
	}
	
	public void writeElement(String parentElementName, String elementName, String data) throws Exception
	{
		String convertedElementName = getConvertedElementName(parentElementName, elementName);
		writeElement(parentElementName + convertedElementName, data);
	}
	
	public void writeNonOptionalCodeElement(String parentElementName, String elementName, ChoiceQuestion question, String code) throws Exception
	{
		String convertedElementName = getConvertedElementName(parentElementName, elementName);
		writeStartElement(parentElementName + convertedElementName);
		
		if (question.doesCodeExist(code))
			writeXmlText(question.convertToReadableCode(code));
		
		writeEndElement(parentElementName + convertedElementName);
	}
	
	private void writeElement(final String elementName, final int data) throws Exception
	{
		writeElement(elementName, Integer.toString(data));
	}
	
	public void writeElement(String elementName, String data) throws Exception
	{
		if (data.length() == 0)
			return;
		
		writeRequiredElement(elementName, data);
	}
	
	private void writeRequiredElement(String elementName, String data) throws Exception
	{
		if (data == null)
			return;
		
		writeRawElement(elementName, data);
	}

	private void writeRawElement(String elementName, String data) throws Exception
	{
		writeStartElement(elementName);
		writeXmlText(data);
		writeEndElement(elementName);
	}
	
	public void writeXmlText(String xmlText) throws Exception
	{
		write(xmlText);
	}
	
	public void writeStartElement(final String elemnentName)	throws Exception
	{
		write("<" + PREFIX + elemnentName + ">");
	}
	
	public void writeEndElement(final String elemnentName)	throws Exception
	{
		write("</" + PREFIX + elemnentName + ">");
		writeln();
	}
	
	private String getConvertedElementName(String parentElementName,String elementName)
	{
		return getTagToElementNameMap().findElementName(parentElementName, elementName);
	}

	private Xmpz2TagToElementNameMap getTagToElementNameMap()
	{
		return tagToElementNameMap;
	}
	
	public void write(final String value) throws Exception
	{
		getWriter().write(value);
	}
	
	private void writeln(final String value) throws Exception
	{
		getWriter().writeln(value);
	}
	
	private void writeln() throws Exception
	{
		getWriter().writeln();
	}
	
	public Project getProject()
	{
		return project;
	}
	
	private UnicodeWriter getWriter()
	{
		return writer;
	}
	
	private Project project;
	private UnicodeWriter writer;
	private Xmpz2TagToElementNameMap tagToElementNameMap;
}
