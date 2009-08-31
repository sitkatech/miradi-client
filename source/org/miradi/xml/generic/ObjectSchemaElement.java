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

	public void createNumericField(String fieldNameToUse)
	{
		FieldSchemaElement field = new NumericFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}

	public void createDateField(String fieldNameToUse)
	{
		FieldSchemaElement field = new DateFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}

	protected void createGeospatialLocationField(String fieldNameToUse)
	{
		FieldSchemaElement field = new LocationFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createDiagramPointField(String fieldNameToUse)
	{
		FieldSchemaElement field = new DiagramPointFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createDiagramSizeField(String fieldNameToUse)
	{
		FieldSchemaElement field = new DiagramSizeFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}
	
	protected void createIdListField(String fieldNameToUse, String storedObjectTypeName)
	{
		FieldSchemaElement field = new IdListFieldSchemaElement(getObjectTypeName(), fieldNameToUse, storedObjectTypeName);
		fields.add(field);
	}

	protected void createCountriesField(String fieldNameToUse)
	{
		FieldSchemaElement field = new CountriesFieldSchemaElement(getObjectTypeName(), fieldNameToUse);
		fields.add(field);
	}

	public String getObjectTypeName()
	{
		return objectTypeName;
	}

	@Override
	public void output(SchemaWriter writer) throws IOException
	{
		writer.defineAlias(getDotElement(getObjectTypeName()), "element miradi:" + getObjectTypeName());
		writer.startBlock();
		for(FieldSchemaElement fieldElement : fields)
		{
			fieldElement.output(writer);
			writer.println("&");
		}
		writer.endBlock();
	}
	
	private String objectTypeName;
	private Vector<FieldSchemaElement> fields;
}