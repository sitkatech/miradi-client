/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.generic;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Vector;

import org.martus.util.UnicodeWriter;

public class SchemaWriter extends PrintWriter
{
	public SchemaWriter(UnicodeWriter writer)
	{
		super(writer);
	}
	
	public SchemaWriter(PrintStream out)
	{
		super(out);
	}

	public void defineAlias(String alias, String elementName)
	{
		println(createAlias(alias, elementName));
	}

	public String createAlias(String alias, String elementName)
	{
		return alias + " = " + elementName;
	}

	public void startBlock()
	{
		printlnIndented("{");
		++indentLevel;
	}

	public void endBlock()
	{
		--indentLevel;
		printlnIndented("}");
		println();
	}
	
	public void printIndented(String text)
	{
		for(int i = 0; i < indentLevel; ++i)
			print(INDENTATION);
		print(text);
	}
	
	public void printlnIndented(String text)
	{
		printIndented(text);
		println();
	}

	public void startElementDefinition(String name)
	{
		println(name + ".element = element " + name);
		startBlock();
	}

	public void writeSchemaElement(String elementName, String elementType)
	{
		printlnIndented(createSelfNamedElement(elementName) + " { " + elementType + " }");
	}

	public String createSelfNamedElement(String elementName) 
	{
		return "element." + elementName + " = element " + elementName;
	}

	public void endElementDefinition(String name)
	{
		endBlock();
	}
	
	public void writeOredElements(Vector<String> elements)
	{
		writeSeparatedElements(elements, "|");
	}
	
	public void defineElements(Vector<String> elements)
	{
		writeSeparatedElements(elements, " &\n");
	}
	
	private void writeSeparatedElements(final Vector<String> elements, final String separator)
	{
		for (int index = 0; index < elements.size(); ++index)
		{
			if (index > 0)
				print(separator);
			
			printIndented(elements.get(index));
		}
		
		println();
	}

	private int indentLevel;
	public static final String INDENTATION = "  ";
}
