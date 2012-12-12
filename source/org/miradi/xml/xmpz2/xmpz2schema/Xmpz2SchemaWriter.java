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
import java.util.Calendar;

import org.martus.util.UnicodeWriter;
import org.miradi.utils.Utility;
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
	
	public void writeHeaderComments()
	{
		println("# $LastChangedBy: Miradi $");
		println("# $LastChangedDate: " + Calendar.getInstance().getTime() + " $");
		println("# $LastChangedRevision: " + NAME_SPACE_VERSION + " $");
	}
	
	public void writeNamespace(String uri)
	{
		println("namespace " + RAW_PREFIX + " = " + "'" + uri + "'");
	}
	
	public void writeAlias(final String elementName)
	{
		defineAlias(createDotElement(elementName), ELEMENT_NAME + PREFIX + elementName);
	}

	@Override	
	public void startElementDefinition(String name)
	{
		println(createElementDefinition(name));
		startBlock();
	}

	public String createElementDefinition(String elementName)
	{
		return createDotElement(elementName) + " = element " +  PREFIX + elementName;
	}
	
	public void writeElement(final String elementName)
	{
		println(createDotElement(elementName) + " = element " + PREFIX + elementName + " { xsd:integer }");
	}
	
	public void writeElement(final String elementName, String elementType)
	{
		printIndented(createXsdSchemaElement(elementName, elementType));
	}
	
	public String createAttributeSchemaElement(final String attributeName)
	{
		return createSchemaElementEndingWithAnd(createTextAttributeElement(attributeName));
	}

	public String createTextAttributeElement(final String attributeName)
	{
		return createAttributeElement(attributeName, "text");
	}

	public String createAttributeElement(final String attributeName, final String attributeType)
	{
		return "attribute " + attributeName + " { " + attributeType + " }";
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
		return createOptionalSchemaElement(createSchemaElement(elementName, elementType));
	}
	
	public String createZeroOrMoreElementDefinition(String parentElementName, final String elementName)
	{
		return createOptionalSchemaElement(parentElementName, createZeroOrMoreDotElement(elementName));
	}

	public String createXsdSchemaElement(final String elementName, String elementType)
	{
		return createSchemaElement(elementName, createXsdElement(elementType));
	}

	public String createZeroOrMoreSchemaElement(String parentElementName, final String elementName)
	{
		return createSchemaElement(parentElementName, createZeroOrMoreElementName(elementName));
	}
	
	public String createSchemaElement(final String elementName, final String elementType)
	{
		return ELEMENT_NAME + PREFIX + elementName + " { " + elementType + " }";
	}
	
	public String createDotElement(final String elementName)
	{
		return elementName + ".element";
	}
	
	public String createZeroOrMoreDotElement(final String elementName)
	{
		return createZeroOrMoreElementName(createDotElement(elementName));
	}

	public String createZeroOrMoreElementName(final String createDotElement)
	{
		return createDotElement + "*";
	}
	
	public String createSchemaElementEndingWithAnd(final String element)
	{
		return element + " &";
	}

	public String createOptionalSchemaElement(final String createSchemaElement)
	{
		return createSchemaElement + "?";
	}
	
	public static String createXsdElement(String elementType)
	{
		return "xsd:" + elementType;
	}

	public static String createBooleanType()
	{
		return createXsdElement("boolean");
	}

	public String createDecimalType()
	{
		return createXsdElement("decimal");
	}
	
	public String createIntegerType()
	{
		return createXsdElement("integer");
	}
	public void defineBudgetElements(final String parentName, final String containerName, final String elementName, String[] elementTypes)
	{
		writeAlias(parentName);
		startBlock();
		printIndented(ELEMENT_NAME + PREFIX + containerName + "{");
		writeOredElements(Utility.convertToVector(elementTypes));
		printlnIndented(" }? &");
		printlnIndented(createOptionalSchemaElement(createSchemaElement(elementName, createDecimalType())));
		endBlock();
	}

	public String createElementName(String elementName)
	{
		return elementName + DOT_ELEMENT;
	}
}
