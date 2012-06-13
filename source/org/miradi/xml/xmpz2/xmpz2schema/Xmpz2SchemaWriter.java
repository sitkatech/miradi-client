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

	@Override	
	public void startElementDefinition(String name)
	{
		println(name + ".element = element " +  PREFIX + name);
		startBlock();
	}
	
	public void writeElement(final String idName)
	{
		println(idName + ".element = element " + PREFIX + idName + " { xsd:integer }");
	}
	
	public String createAttributeSchemaElement(final String attributeName)
	{
		return "attribute " + attributeName + " { text } &";
	}

	public String createSchemaElement4(final String elementName, final String elementType)
	{
		return ELEMENT_NAME + PREFIX + elementName + " { " + elementType + " } &";
	}
	
	public String createSchemaElement10(final String elementName, final String elementType)
	{
		return ELEMENT_NAME + PREFIX + elementName + " { " + elementType + " } &";
	}

	public String createSchemaElement9(final String elementName, final String elementType)
	{
		return ELEMENT_NAME + PREFIX + elementName + " { " + elementType + " }? &";
	}
	
	public String createOptionalSchemaElement(final String elementName, final String elementType)
	{
		return createSchemaElement9(elementName, elementType);
	}
	
	public String createSchemaElement(String poolName, String elementName, String elementType)
	{
		return ELEMENT_NAME + PREFIX + poolName + elementName + " { " + elementType + " }?";
	}
	
	public String createSchemaElement8(final String elementName, final String elementType)
	{
		return ELEMENT_NAME + PREFIX + elementName + " { " + elementType + " }?";
	}
	
	public String createSchemaElement(final String extraDataItemValue)
	{
		return ELEMENT_NAME + PREFIX + extraDataItemValue + " { text }?";
	}
	
	public String createElementName(String parentElementName, final String elementName)
	{
		return ELEMENT_NAME + PREFIX + parentElementName + " { " + elementName + ".element* }?";
	}

	public String createSchemaElement5(final String elementName, final String elementType)
	{
		return ELEMENT_NAME + PREFIX + elementName + " { " + elementType + " }";
	}
	
	public void writeElement(final String elementName, String elementType)
	{
		printIndented(ELEMENT_NAME + PREFIX + elementName + " { xsd:" + elementType + " }");
	}
		
	public String createSchemaElement(String parentElementName, final String elementName)
	{
		return ELEMENT_NAME + PREFIX + parentElementName + " { " + elementName + "* }";
	}
}
