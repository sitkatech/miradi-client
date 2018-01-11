/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.utils;

import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.project.Project;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;

public class SchemaConsoleWriter
{
	public static void main(String[] args)
	{
		try
		{
			outputBaseObjectSchemas();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void outputBaseObjectSchemas() throws Exception
	{
		Project project = new Project();
		project.clear();
		Translation.initialize();
		System.out.println("HashMap<Integer, HashMap<String, String>> typeToFields = new HashMap<Integer, HashMap<String, String>>();");
		for (int type = ObjectType.FIRST_OBJECT_TYPE; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			BaseObjectPool pool = project.getPool(type);
			if (pool == null)
				continue;
			
			BaseObjectSchema schema = pool.createBaseObjectSchema(project);
			System.out.println("HashMap<String, String> fieldsForType" + type + " = new HashMap<String, String>();");
			for(AbstractFieldSchema fieldSchema : schema)
			{
				if (fieldSchema.isPseudoField())
					continue;
				
				ObjectData field = fieldSchema.createField(null);
				final String tag = fieldSchema.getTag();
				System.out.println("fieldsForType" + type + ".put(\"" + tag + "\", \"" + field.getClass().getSimpleName() + "\");");
			}
			
			System.out.println("typeToFields.put(" + type + ", fieldsForType" + type + ");");
			System.out.println();
		}
	}	
}
