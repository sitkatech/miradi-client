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

import org.martus.util.UnicodeWriter;
import org.miradi.objectdata.ObjectData;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.HtmlUtilities;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class Xmpz2XmlUnicodeWriter extends UnicodeWriter implements XmpzXmlConstants
{
	public Xmpz2XmlUnicodeWriter(OutputStream bytes) throws Exception
	{
		super(bytes);
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
		xmlText = HtmlUtilities.replaceHtmlBrsWithNewlines(xmlText);
		write(xmlText);
	}
	
	private void writeStartElement(final String elemnentName)	throws Exception
	{
		writeln("<" + elemnentName + ">");
	}
	
	private void writeEndElement(final String elemnentName)	throws Exception
	{
		writeln("</" + elemnentName + ">");
	}
	
	public void writeChoiceData(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema, final ChoiceQuestion choiceQuestion, final String code) throws Exception
	{
		String readableCode = choiceQuestion.convertToReadableCode(code);
		final String elementName = appendParentNameToChildName(baseObjectSchema, fieldSchema);
		writeElement(elementName, readableCode);
	}

	private String appendParentNameToChildName(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema)
	{
		return baseObjectSchema.getXmpz2ElementName() + fieldSchema.getTag();
	}

	public void writeStringData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeToCodeMapData(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeToUserStringMapData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
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

	public void writetCodeToCodeListMapData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writetDateData(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, String date) throws Exception
	{
		final String elementName = appendParentNameToChildName(baseObjectSchema, fieldSchema);
		writeElement(elementName, date);
	}

	public void writetDateRangeData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string) 
	{
	}

	public void writeDateUnitEffortListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeDimensionData(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeFloatData(BaseObjectSchema baseObjectSchema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeIdListData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeIntegerData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeBooleanData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeNumberData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writePercentageData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
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

	public void writeUserTextData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeRefMapData(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema, String string)
	{
	}
}
