/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.main;

import java.io.File;
import java.io.PrintStream;
import java.util.Vector;

import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.project.ProjectLoader;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.Translation;

public class ListProjectContents
{
	public static void main(String[] args) throws Exception
	{
		Translation.initialize();
		
		// TODO: Find a better way to avoid printing all the
		// exceptions from fields that don't have labels
		EAM.setLogToString();
		
		String projectPath = args[0];
		File projectFile = new File(projectPath);
		Project project = new Project();
		ProjectLoader.loadProject(projectFile, project);
		
		listProjectContents(System.out, project);
	}

	private static void listProjectContents(PrintStream out, Project project)
	{
		for(int type = 0; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			BaseObjectPool pool = project.getPool(type);
			if(pool == null)
				continue;
			if(pool.size() == 0)
				continue;
			
			if(type < 3 || type == ObjectType.TABLE_SETTINGS || type == ObjectType.VIEW_DATA)
				continue;
			
			out.println();
			out.println("------");
			BaseObjectSchema schema = pool.createBaseObjectSchema(project);
			out.println("Objects of type " + type + " (" + schema.getObjectName()  + ")");
			listPoolContents(out, pool.getAllObjects());
		}
	}

	private static void listPoolContents(PrintStream out, Vector<BaseObject> objects)
	{
		for(BaseObject baseObject : objects)
		{
			listObjectContents(out, baseObject);
		}
	}

	private static void listObjectContents(PrintStream out, BaseObject baseObject)
	{
		out.println(baseObject.getSchema().getObjectName() + ": " + baseObject.getId());
		Vector<String> tags = baseObject.getStoredFieldTags();
		for(String tag : tags)
		{
			ObjectData field = baseObject.getField(tag);
			String value = field.get();
			if(value.length() == 0)
				continue;
			
			String label = EAM.fieldLabel(baseObject.getType(), tag);
			out.println("  " + label + ": " + value);
		}
		out.println();
	}

}
