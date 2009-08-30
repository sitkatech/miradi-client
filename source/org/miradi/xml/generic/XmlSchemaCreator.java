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

import org.miradi.main.Miradi;
import org.miradi.utils.Translation;

public class XmlSchemaCreator
{
	public static void main(String[] args) throws Exception
	{
		new XmlSchemaCreator().printXmlRncSchema(new SchemaWriter(System.out));
	}

	public XmlSchemaCreator() throws Exception
	{
		Miradi.addThirdPartyJarsToClasspath();
		Translation.initialize();
	}

	private void printXmlRncSchema(SchemaWriter writer) throws Exception
	{
		ProjectSchemaElement rootElement = new ProjectSchemaElement();
		writer.defineAlias("start", rootElement.getProjectElementName() + ".element");
		rootElement.output(writer);
		
		writer.println("vocabulary_date = xsd:NMTOKEN { pattern = '[0-9]{4}-[0-9]{2}-[0-9]{2}' }");

		writer.defineAlias("GeospatialLocation.element.element", "element cp:GeospatialLocation");
		writer.startBlock();
        writer.printlnIndented("element cp:latitude { xsd:decimal } &");
        writer.printlnIndented("element cp:longitude { xsd:decimal } &");
        writer.endBlock();

        writer.flush();
    }
}
