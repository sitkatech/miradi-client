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

import java.io.PrintStream;

import org.martus.util.UnicodeWriter;
import org.miradi.xml.generic.SchemaWriter;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;

public class Xmpz2SchemaWriter extends SchemaWriter implements Xmpz2XmlConstants
{
	public Xmpz2SchemaWriter(UnicodeWriter writer)
	{
		super(writer);
	}

	public Xmpz2SchemaWriter(PrintStream out)
	{
		super(out);
	}
	
	public void writeNamespace(String uri)
	{
		println("namespace " + RAW_PREFIX + " = " + "'" + uri + "'");
	}
	
	public void writeAlias(final String elementName)
	{
		defineAlias(elementName + ".element", ELEMENT_NAME + PREFIX + elementName);
	}

	@Override	
	public void startElementDefinition(String name)
	{
		println(name + ".element = element " +  PREFIX + name);
		startBlock();
	}
	
	public void writeElement(final String elementName)
	{
		println(elementName + ".element = element " + PREFIX + elementName + " { xsd:integer }");
	}
	
	public void writeElement(final String elementName, String elementType)
	{
		printIndented(createXsdSchemaElement(elementName, elementType));
	}
	
	public String createAttributeSchemaElement(final String attributeName)
	{
		return createSchemaElementEndingWithAnd("attribute " + attributeName + " { text }");
	}

	public String createTextSchemaElement(final String extraDataItemValue)
	{
		return createOptionalSchemaElement(extraDataItemValue, "text");
	}

	public String createSchemaElementWithAnd(final String elementName, final String elementType)
	{
		return createSchemaElementEndingWithAnd(createSchemaElement(elementName, elementType));
	}
	
	public String createOptionalSchemaElementWithAnd(final String elementName, final String elementType)
	{
		return createSchemaElementEndingWithAnd(createOptionalSchemaElement(elementName, elementType));
	}
	
	public String createOptionalSchemaElement(final String elementName, final String elementType)
	{
		return createSchemaElement(elementName, elementType) + "?";
	}
	
	public String createRequiredElementDefinition(String parentElementName, final String elementName)
	{
		return createOptionalSchemaElement(parentElementName, createRequiredDotElement(elementName));
	}

	public String createXsdSchemaElement(final String elementName, String elementType)
	{
		return createSchemaElement(elementName, createXsdElement(elementType));
	}

	public String createRequiredSchemaElement(String parentElementName, final String elementName)
	{
		return createSchemaElement(parentElementName, createRequiredElementName(elementName));
	}
	
	public String createSchemaElement(final String elementName, final String elementType)
	{
		return ELEMENT_NAME + PREFIX + elementName + " { " + elementType + " }";
	}
	
	public String createDotElement(final String elementName)
	{
		return elementName + ".element";
	}
	
	public String createRequiredDotElement(final String elementName)
	{
		return createRequiredElementName(createDotElement(elementName));
	}

	public String createRequiredElementName(final String createDotElement)
	{
		return createDotElement + "*";
	}
	
	public String createSchemaElementEndingWithAnd(final String element)
	{
		return element + " &";
	}
	
	public String createXsdElement(String elementType)
	{
		return "xsd:" + elementType;
	}

	public String createBooleanType()
	{
		return createXsdElement("boolean");
	}

	public String createDecimalType()
	{
		return createXsdElement("decimal");
	}
}
