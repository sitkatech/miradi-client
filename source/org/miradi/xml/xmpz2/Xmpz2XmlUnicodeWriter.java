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
	
	public void writeObjectElementStart(final BaseObjectSchema schema) throws Exception
	{
		writeStartElement(schema.getXmpz2ElementName());
	}

	public void writeObjectElementEnd(final BaseObjectSchema schema) throws Exception
	{
		writeEndElement(schema.getXmpz2ElementName());
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
		writeTextData(data);
		writeEndElement(elementName);
		writeln();
	}
	
	private void writeTextData(String data) throws IOException
	{
		data = HtmlUtilities.replaceHtmlBrsWithNewlines(data);
		data = HtmlUtilities.stripAllHtmlTags(data);
		write(data);
	}
	
	private void writeStartElement(final String elemnentName)	throws Exception
	{
		writeln("<" + elemnentName + ">");
	}
	
	private void writeEndElement(final String elemnentName)	throws Exception
	{
		writeln("</" + elemnentName + ">");
	}
	
	public void writeChoiceData(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema, final String data) throws Exception
	{
		final String elementName = appendParentNameToChildName(baseObjectSchema, fieldSchema);
		writeElement(elementName, data);
	}

	private String appendParentNameToChildName(final BaseObjectSchema baseObjectSchema, final AbstractFieldSchema fieldSchema)
	{
		return baseObjectSchema.getXmpz2ElementName() + fieldSchema.getTag();
	}

	public void writeStringData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeToCodeMapData(BaseObjectSchema schema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeToUserStringMapData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeListData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeDateUnitListData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeRefListListData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeTagListData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeBaseIdData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeCodeToChoiceMapData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writetCodeToCodeListMapData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writetDateData(BaseObjectSchema schema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writetDateRangeData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string) 
	{
	}

	public void writeDateUnitEffortListData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeDimensionData(BaseObjectSchema schema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeFloatData(BaseObjectSchema schema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeIdListData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeIntegerData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeBooleanData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeNumberData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writePercentageData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeORefData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writePointData(BaseObjectSchema schema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writePointListData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writePseudoQuetionData(BaseObjectSchema schema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writePseudoRefListData(BaseObjectSchema schema,	AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeRefListData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeRelevancyOverrideSetData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeUserTextData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}

	public void writeRefMapData(BaseObjectSchema schema, AbstractFieldSchema fieldSchema, String string)
	{
	}
}
