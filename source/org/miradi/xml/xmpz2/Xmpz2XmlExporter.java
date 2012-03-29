/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FosProjectData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.wcs.TagToElementNameMap;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class Xmpz2XmlExporter extends XmlExporter implements XmpzXmlConstants
{
	public Xmpz2XmlExporter(Project projectToExport, final Xmpz2XmlUnicodeWriter outToUse)
	{
		super(projectToExport);
		
		out = outToUse;
	}

	@Override
	public void exportProject(UnicodeWriter outToUse) throws Exception
	{
		getWriter().writeXmlHeader();
		getWriter().writeMainElementStart();
		exportPools();
		getWriter().writeMainElementEnd();
	}

	private void exportPools() throws Exception
	{
		writeFosProjectDataSchemaElement();
	}
	
	private void writeFosProjectDataSchemaElement() throws Exception
	{
		FosProjectData fosBaseObject = getFosProjectData();
		BaseObjectSchema schema = fosBaseObject.getSchema();
		getWriter().writeStartElement(schema);
		for(AbstractFieldSchema fieldSchema : schema)
		{
			getWriter().writeElement(fosBaseObject, fieldSchema);
		}
		
		getWriter().writeEndElement(schema);
	}
	
	public void writeOptionalElementWithSameTag(String parentElementName, BaseObject object, String tag) throws Exception
	{
		writeOptionalElement(parentElementName, tag, object, tag);
	}
	
	public void writeOptionalElement(String parentElementName, String elementName, BaseObject object, String tag) throws Exception
	{
		String convertedElementName = getConvertedElementName(parentElementName, elementName);
		writeOptionalElement(getWriter(), parentElementName + convertedElementName, object, tag);
	}
	
	public void writeCodeElement(String parentElementName, String elementName, ChoiceQuestion question, String code) throws Exception
	{
		String convertedElementName = getConvertedElementName(parentElementName, elementName);
		writeStartElement(getWriter(), parentElementName + convertedElementName);
		
		if (doesCodeExist(question, code))
			writeXmlEncodedData(getWriter(), question.convertToReadableCode(code));
		else
			logMissingCode(question, code);
		
		writeEndElement(getWriter(), parentElementName + convertedElementName);
	}
	
	private String getConvertedElementName(String parentElementName,String elementName)
	{
		TagToElementNameMap map = new TagToElementNameMap();
		return map.findElementName(parentElementName, elementName);
	}
	
	private boolean doesCodeExist(ChoiceQuestion question, String code)
	{
		ChoiceItem choiceItem = question.findChoiceByCode(code);
		if (choiceItem == null)
			return false;
		
		return true;
	}
	
	private void logMissingCode(ChoiceQuestion question, String code)
	{
		EAM.logWarning(code + " is a code that does not exist in the question:" + question.getClass().getSimpleName());
	}
	
	private Xmpz2XmlUnicodeWriter getWriter()
	{
		return out;
	}

	private Xmpz2XmlUnicodeWriter out;
}
