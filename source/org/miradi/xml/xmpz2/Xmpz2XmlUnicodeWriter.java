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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.CodeList;
import org.miradi.xml.generic.XmlSchemaCreator;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class Xmpz2XmlUnicodeWriter extends UnicodeWriter implements XmpzXmlConstants
{
	public Xmpz2XmlUnicodeWriter(Project projectToUse, OutputStream bytes) throws Exception
	{
		super(bytes);
		
		project = projectToUse;
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

	public void writeCodeToCodeMapData(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeToUserStringMapData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String codeListAsString) throws Exception
	{
		CodeList codes = new CodeList(codeListAsString);
		if (codes.isEmpty())
			return;
		
		final String elementName = appendParentNameToChildName(baseObjectSchema, fieldSchema);
		final String elementContainerName = createContainerElementName(elementName);
		writeStartElement(elementContainerName);
		for(String code : codes)
		{
			writeElement(XmlSchemaCreator.CODE_ELEMENT_NAME, code);
		}
		
		writeEndElement(elementContainerName);
	}

	public void writeDateUnitListData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeRefListListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeTagListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeBaseIdData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeToChoiceMapData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeToCodeListMapData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeDateData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String isoData) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, isoData);
	}

	public void writeDateRangeData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string) 
	{
	}

	public void writeDateUnitEffortListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeDimensionData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeFloatData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String number) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, number);
	}

	public void writeIdListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
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

	public void writeORefData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writePointData(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writePointListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writePseudoQuetionData(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writePseudoRefListData(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeRefListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeRelevancyOverrideSetData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeUserTextData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string) throws Exception
	{
		writeField(baseObjectSchema, fieldSchema, string);
	}

	public void writeRefMapData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String stringRefMapAsString) throws Exception
	{
		writeStartElement(appendParentNameToChildName(PROJECT_SUMMARY, Xenodata.TAG_PROJECT_ID));

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
		
		writeEndElement(appendParentNameToChildName(PROJECT_SUMMARY, Xenodata.TAG_PROJECT_ID));

	}
	
	private void writeField(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String data) throws Exception
	{
		final String elementName = appendParentNameToChildName(baseObjectSchema, fieldSchema);
		writeElement(elementName, data);
	}
	
	private String appendParentNameToChildName(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema)
	{
		final String xmpz2ElementName = baseObjectSchema.getXmpz2ElementName();
		final String tag = fieldSchema.getTag();
		return appendParentNameToChildName(xmpz2ElementName, tag);
	}

	private String appendParentNameToChildName(final String parentElementName,	final String childElementName)
	{
		return parentElementName + childElementName;
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
		writeln("<" + CONSERVATION_PROJECT + " " + XMLNS + "=\"" + NAME_SPACE + "\">");
	}
	
	public void writeMainElementEnd() throws Exception
	{
		writeEndElement(CONSERVATION_PROJECT);
	}
	
	public void writeObjectElementStart(final BaseObjectSchema baseObjectSchema) throws Exception
	{
		writeStartElement(baseObjectSchema.getXmpz2ElementName());
	}

	public void writeObjectElementEnd(final BaseObjectSchema baseObjectSchema) throws Exception
	{
		writeEndElement(baseObjectSchema.getXmpz2ElementName());
	}
	
	public void writeFieldElement(final BaseObject baseObject, final AbstractFieldSchema fieldSchema) throws Exception
	{
		ObjectData field = baseObject.getField(fieldSchema.getTag());
		field.writeAsXmpz2XmlData(this, baseObject.getSchema(), fieldSchema);
	}
	
	private void writeElement(String elementName, String data) throws Exception
	{
		if (data == null || data.length() == 0)
			return;
		
		writeStartElement(elementName);
		writeXmlText(data);
		writeEndElement(elementName);
		writeln();
	}
	
	private void writeXmlText(String xmlText) throws IOException
	{
		write(xmlText);
	}
	
	private void writeStartElement(final String elemnentName)	throws Exception
	{
		write("<" + elemnentName + ">");
	}
	
	private void writeEndElement(final String elemnentName)	throws Exception
	{
		writeln("</" + elemnentName + ">");
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
