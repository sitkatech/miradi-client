/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

import java.util.Vector;

import org.miradi.xml.xmpz2.Xmpz2XmlConstants;

public class Xmpz2CustomSchemaDefinitionCreator implements Xmpz2XmlConstants
{
	public Xmpz2CustomSchemaDefinitionCreator(Xmpz2SchemaWriter xmpz2SchemaWriterToUse, String parentNameToUse)
	{
		xmpz2SchemaWriter = xmpz2SchemaWriterToUse;
		parentName = parentNameToUse;
		childElements = new Vector<String>();
	}
	
	public void addChildElement(String childElement)
	{
		childElements.add(childElement);
	}
	
	public void addChildElement(String elementName, String elementType)
	{
		addChildElement(getSchemaWriter().createSchemaElement(elementName, elementType));
	}
	
	public void addOptionalChildElement(String elementName, String elementType)
	{
		addChildElement(getSchemaWriter().createOptionalSchemaElement(elementName, elementType));
	}
	
	public void addZeroOrMoreChildElement(String elementName, String elementType)
	{
		addChildElement(getSchemaWriter().createZeroOrMoreElementName(getSchemaWriter().createSchemaElement(elementName, elementType)));
	}
	
	public void addTextAttributeElement(String elementName)
	{
		addChildElement(getSchemaWriter().createTextAttributeElement(elementName));
	}
	
	public void addUriRestrictedAttributeElement(String elementName)
	{
		addChildElement(getSchemaWriter().createUriRestrictedAttributeElement(elementName));
	}
	
	public void addOptionalDecimalElement(String elementName)
	{
		addOptionalChildElement(elementName, getSchemaWriter().createDecimalType());
	}
	
	public void addOptionalTextSchemaElement(String elementName)
	{
		addChildElement(getSchemaWriter().createOptionalTextSchemaElement(elementName));
	}
	
	public void addTextSchemaElement(String elementName)
	{
		addChildElement(getSchemaWriter().createTextSchemaElement(elementName));
	}
	
	public void addUriTextSchemaElement(String elementName)
	{
		addChildElement(getSchemaWriter().createUriSchemaElement(elementName));
	}
	
	public void addZeroOrMoreDotElement(String elementName)
	{
		addChildElement(getSchemaWriter().createZeroOrMoreDotElement(elementName));
	}
	
	public void addOptionalSchemaElement(String elementName)
	{
		addChildElement(getSchemaWriter().createOptionalSchemaElement(parentName + elementName, getSchemaWriter().createDotElement(elementName)));
	}
	
	public String createSchemaElement()
	{
		String schemaElement = getSchemaWriter().createAlias(getSchemaWriter().createElementName(parentName), ELEMENT_NAME + PREFIX + parentName);
		schemaElement = addNewLine(schemaElement);
		schemaElement = addStartBlock(schemaElement);
		schemaElement = addNewLine(schemaElement);
		for (int index = 0; index < childElements.size(); ++index)
		{
			if (index > 0)
			{
				schemaElement = appendAnd(schemaElement);
				schemaElement = addNewLine(schemaElement);
			}
			
			schemaElement += getSchemaWriter().INDENTATION + childElements.get(index);
		}
		schemaElement = addNewLine(schemaElement);
		schemaElement = addEndBlock(schemaElement);
		schemaElement = addNewLine(schemaElement);
		schemaElement = addNewLine(schemaElement);
		
		return schemaElement;
	}
	
	private String appendAnd(String schemaElement)
	{
		return append(schemaElement, " &");
	}

	private String addNewLine(String schemaElement)
	{
		return append(schemaElement, "\n");
	}
	
	private String addStartBlock(String schemaElement)
	{
		return append(schemaElement, "{");
	}

	private String addEndBlock(String schemaElement)
	{
		return append(schemaElement, "}");
	}
	
	public String append(String schemaElement, final String string)
	{
		return schemaElement += string;
	}
	
	private Xmpz2SchemaWriter getSchemaWriter()
	{
		return xmpz2SchemaWriter;
	}
	
	private Vector<String> childElements;
	private Xmpz2SchemaWriter xmpz2SchemaWriter;
	private String parentName;
}
