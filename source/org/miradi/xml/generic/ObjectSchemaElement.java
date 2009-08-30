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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;


class ObjectSchemaElement extends SchemaElement
{
	public ObjectSchemaElement(String objectTypeNameToUse)
	{
		objectTypeName = objectTypeNameToUse;
		fields = new Vector<FieldSchemaElement>();
	}
	
	public void createTextField(String fieldNameToUse)
	{
		FieldSchemaElement field = new TextFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}

	public String getObjectTypeName()
	{
		return objectTypeName;
	}

	@Override
	public void output(PrintWriter writer) throws IOException
	{
		writer.println(getDotElement(getObjectTypeName()) + " = ");
		super.output(writer);
		writer.print("miradi:" + getObjectTypeName());
		writer.println();
		
		writer.println("{");
		for(FieldSchemaElement fieldElement : fields)
		{
			fieldElement.output(writer);
			writer.println("&");
		}
		writer.println("}");
	}
	
	private String objectTypeName;
	private Vector<FieldSchemaElement> fields;
}