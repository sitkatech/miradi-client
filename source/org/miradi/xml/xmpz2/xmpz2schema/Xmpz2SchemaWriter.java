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
import java.util.Vector;

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
	
	public void writeSeparatedElements(final Vector<String> elements, final String separator)
	{
		for (int index = 0; index < elements.size(); ++index)
		{
			write(elements.get(index));
			if (index < elements.size() - 1)
				print(separator);
		}
		println();
	}

	public void defineElements(Vector<String> elements)
	{
		for (int index = 0; index < elements.size(); ++index)
		{
			if (index > 0)
				println(" &");
			
			printIndented(elements.get(index));
		}
	}
}
