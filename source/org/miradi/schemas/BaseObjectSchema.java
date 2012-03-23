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

package org.miradi.schemas;

import java.util.HashMap;
import java.util.Vector;

import org.miradi.objectdata.ObjectData;

public class BaseObjectSchema
{
	public BaseObjectSchema()
	{
		fieldSchemas = new Vector<AbstractFieldSchema>();
	}
	
	public void addFieldSchema(final AbstractFieldSchema fieldSchema)
	{
		fieldSchemas.add(fieldSchema);
	}
	
	public Vector<AbstractFieldSchema> getFieldSchemas()
	{
		return fieldSchemas;
	}
	
	public HashMap<String, ObjectData> createTagFieldMap()
	{
		HashMap<String, ObjectData> fields = new HashMap<String, ObjectData>();
		for(AbstractFieldSchema fieldSchema : getFieldSchemas())
		{
			ObjectData field = fieldSchema.createField();
			fields.put(fieldSchema.getTag(), field);
		}
		
		return fields;
	}
	
	private Vector<AbstractFieldSchema> fieldSchemas;
}
