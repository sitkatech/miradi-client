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
import java.io.OutputStream;
import java.util.Set;

import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.FieldSchemaIdList;
import org.miradi.schemas.FieldSchemaReflist;
import org.miradi.utils.CodeList;
import org.miradi.utils.PointList;
import org.miradi.xml.generic.XmlSchemaCreator;
import org.miradi.xml.wcs.TagToElementNameMap;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class Xmpz2XmlWriter extends UnicodeWriter implements XmpzXmlConstants
{
	public Xmpz2XmlWriter(Project projectToUse, OutputStream bytes) throws Exception
	{
		super(bytes);
		
		project = projectToUse;
	}
	
	public void writeStartPoolElement(String startElementName) throws Exception
	{
		writeStartElement(createPoolElementName(startElementName));
	}

	public void writeEndPoolElement(String endElementName) throws Exception
	{
		writeEndElement(createPoolElementName(endElementName));
	}
	
	public String createPoolElementName(String startElementName)
	{
		return startElementName + POOL_ELEMENT_TAG;
	}
	
	public void writeChoiceData(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema, final ChoiceData choiceData) throws Exception
	{
		final ChoiceQuestion choiceQuestion = choiceData.getChoiceQuestion();
		final String code = choiceData.get();
		String readableCode = choiceQuestion.convertToReadableCode(code);
		writeField(baseObjectSchema, fieldSchema, readableCode);
	}

	public void writeStringData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, string);
	}

	public void writeCodeListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String codeListAsString) throws Exception
	{
		CodeList codes = new CodeList(codeListAsString);
		if (codes.isEmpty())
			return;
		
		final String elementName = appendChildNameToParentName(baseObjectSchema, fieldSchema);
		writeNonOptionalCodeListElement(elementName, codes);
	}

	public void writeNonOptionalCodeListElement(final String elementName, final CodeList codes)	throws Exception
	{
		final String elementContainerName = createContainerElementName(elementName);
		writeStartElement(elementContainerName);
		for(String code : codes)
		{
			writeElement(XmlSchemaCreator.CODE_ELEMENT_NAME, code);
		}
		
		writeEndElement(elementContainerName);
	}

	public void writeBaseIdData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, ORef ref) throws Exception
	{
		writeValidRefData(baseObjectSchema, fieldSchema, ref);
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

	public void writeNumberData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String number) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, number);
	}

	public void writePercentageData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String number) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, number);
	}

	public void writeValidRefData(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema, final ORef ref) throws Exception
	{
		if (ref.isValid())
		{
			final String parentElementName = appendChildNameToParentName(baseObjectSchema, fieldSchema);
			final BaseObject baseObject = BaseObject.find(getProject(), ref);
			final String idElementName = baseObject.getTypeName() + ID_ELEMENT_NAME;
			
			writeRef(ref, parentElementName, idElementName);
		}
	}

	public void writeValidRef(final String parentElementName, final String idElementName, final ORef ref) throws Exception, IOException
	{
		if (ref.isValid())
		{
			writeRef(ref, parentElementName + idElementName, idElementName);
		}
	}
	
	private void writeRef(final ORef ref, final String parentElementName, final String idElementName) throws Exception, IOException
	{
		writeStartElement(parentElementName);
		writeStartElement(idElementName);
		write(ref.getObjectId().toString());
		writeEndElement(idElementName);
		writeEndElement(parentElementName);
	}

	public void writePointData(BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema, final Point point) throws Exception
	{
		final String elementName = appendChildNameToParentName(baseObjectSchema, fieldSchema);
		writePoint(elementName, point);
	}

	public void writeBendPointListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, PointList points) throws Exception
	{
		writeBendPointList(baseObjectSchema.getObjectName(), points);
	}

	public void writeBendPointList(final String objectTypeName, PointList points) throws Exception
	{
		final String elemnentName = objectTypeName + BEND_POINTS_ELEMENT_NAME;
		writeStartElement(elemnentName);
		for (int index = 0; index < points.size(); ++index)
		{
			writePoint(DIAGRAM_POINT_ELEMENT_NAME, points.get(index));
		}
		
		writeEndElement(elemnentName);
	}
	
	public void writePoint(final String pointElementName, Point point) throws Exception
	{
		writeStartElement(pointElementName);
		writeStartElement(DIAGRAM_POINT_ELEMENT_NAME);
		writeElement(X_ELEMENT_NAME, point.x);
		writeElement(Y_ELEMENT_NAME, point.y);
		writeEndElement(DIAGRAM_POINT_ELEMENT_NAME);
		writeEndElement(pointElementName);
	}

	public void writePseudoQuestionData(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, String string)
	{
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
		return elementContainerName.replaceAll("Refs", "Ids");
	}

	public void writeUserTextData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, string);
	}

	public void writeRefMapData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String stringRefMapAsString) throws Exception
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
			writeElement(PROJECT_ID, projectId);
			writeEndElement(EXTERNAL_PROJECT_ID_ELEMENT_NAME);
		}
		
		writeEndElement(appendChildNameToParentName(PROJECT_SUMMARY, Xenodata.TAG_PROJECT_ID));
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
	
	private String createContainerElementName(String startElementName)
	{
		return startElementName + CONTAINER_ELEMENT_TAG;
	}
	
	public void writeXmlHeader() throws IOException
	{
		writeln("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
	}
	
	public void writeMainElementStart() throws Exception
	{
		writeStartElementWithAttribute(CONSERVATION_PROJECT, XMLNS, NAME_SPACE);
	}
	
	public void writeStartElementWithAttribute(String startElementName, String attributeName, String attributeValue) throws IOException
	{
		write("<" + startElementName + " " + attributeName + "=\"" + attributeValue + "\">");
	}
	
	public void writeStartElementWithTwoAttributes(String startElementName, String attributeName1, int attributeValue1, String attributeName2, int attributeValue2) throws IOException
	{
		write("<" + startElementName + " " + attributeName1 + "=\"" + Integer.toString(attributeValue1) + "\" " + attributeName2 + "=\"" + Integer.toString(attributeValue2) + "\">");
	}
	
	public void writeMainElementEnd() throws Exception
	{
		writeEndElement(CONSERVATION_PROJECT);
	}
	
	public void writeObjectElementStart(final BaseObject baseObject) throws Exception
	{
		BaseObjectSchema baseObjectSchema = baseObject.getSchema();
		writeStartElementWithAttribute(baseObjectSchema.getXmpz2ElementName(), ID, baseObject.getId().toString());
	}

	public void writeObjectElementEnd(final BaseObjectSchema baseObjectSchema) throws Exception
	{
		writeEndElement(baseObjectSchema.getXmpz2ElementName());
	}
	
	public void writeFieldElement(final BaseObject baseObject, final AbstractFieldSchema fieldSchema) throws Exception
	{
		ObjectData field = baseObject.getField(fieldSchema.getTag());
		if (field.isPseudoField() && !isExportedPseudo(baseObject, fieldSchema.getTag()))
			return;
				
		field.writeAsXmpz2XmlData(this, baseObject.getSchema(), fieldSchema);
	}
	
	private boolean isExportedPseudo(BaseObject baseObject, String tag)
	{
		return false;
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
		
		if (doesCodeExist(question, code))
			writeXmlText(question.convertToReadableCode(code));
		
		writeEndElement(parentElementName + convertedElementName);
	}
	
	private boolean doesCodeExist(ChoiceQuestion question, String code)
	{
		ChoiceItem choiceItem = question.findChoiceByCode(code);
		if (choiceItem == null)
			return false;
		
		return true;
	}
	
	private void writeElement(final String elementName, final int data) throws Exception
	{
		writeElement(elementName, Integer.toString(data));
	}
	
	public void writeElement(String elementName, String data) throws Exception
	{
		if (data == null || data.length() == 0)
			return;
		
		writeStartElement(elementName);
		writeXmlText(data);
		writeEndElement(elementName);
	}
	
	public void writeXmlText(String xmlText) throws IOException
	{
		write(xmlText);
	}
	
	public void writeStartElement(final String elemnentName)	throws Exception
	{
		write("<" + elemnentName + ">");
	}
	
	public void writeEndElement(final String elemnentName)	throws Exception
	{
		write("</" + elemnentName + ">");
		writeln();
	}
	
	private String getConvertedElementName(String parentElementName,String elementName)
	{
		TagToElementNameMap map = new TagToElementNameMap();
		return map.findElementName(parentElementName, elementName);
	}
	
	public Project getProject()
	{
		return project;
	}
	
	private Project project;
}