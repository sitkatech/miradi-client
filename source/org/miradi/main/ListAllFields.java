/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.main;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.martus.util.DirectoryUtils;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;
import org.miradi.utils.Translation;

public class ListAllFields
{
	public static void main(String[] args) throws Exception
	{
		Miradi.addThirdPartyJarsToClasspath();
		Translation.initialize();
		
		File tempDirectory = File.createTempFile("$$$Miradi-ListAllFields", null);
		tempDirectory.delete();
		tempDirectory.mkdirs();
		Project project = new Project();
		project.createOrOpen(tempDirectory);
		listFieldsToConsole(project);
		project.close();
		DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
	}

	private static void listFieldsToConsole(Project project) throws IOException, Exception
	{
		for(int type = 0; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			if(project.getPool(type) == null)
				continue;
			BaseObject object = createObject(project, type);
			showObjectName(object);
			Vector<String> fieldTags = object.getStoredFieldTags();
			for(int field = 0; field < fieldTags.size(); ++field)
			{
				String tag = fieldTags.get(field);
				showField(object, tag);
			}
		}
	}

	private static BaseObject createObject(Project project, int type) throws Exception
	{
		CreateObjectParameter extraInfo = createExtraInfo(type);
		ORef ref = new ORef(type, project.createObjectAndReturnId(type, extraInfo));
		BaseObject object = project.findObject(ref);
		return object;
	}
	
	

	private static CreateObjectParameter createExtraInfo(int type)
	{
		if(type == DiagramFactor.getObjectType())
			return new CreateDiagramFactorParameter(ORef.INVALID); 
		
		if(type == FactorLink.getObjectType())
			return new CreateFactorLinkParameter(ORef.INVALID, ORef.INVALID);
		
		if(type == DiagramLink.getObjectType())
		{
			FactorLinkId factorLinkId = new FactorLinkId(BaseId.INVALID.asInt());
			DiagramFactorId fromId = new DiagramFactorId(BaseId.INVALID.asInt());
			DiagramFactorId toId = new DiagramFactorId(BaseId.INVALID.asInt());
			return new CreateDiagramFactorLinkParameter(factorLinkId, fromId, toId);
		}
		
		if(type == ThreatStressRating.getObjectType())
		{
			return new CreateThreatStressRatingParameter(ORef.INVALID);
		}
		
		return null;
	}

	private static void showField(BaseObject object, String tag)
	{
		String fieldLabel = EAM.fieldLabel(object.getType(), tag);
		if(fieldLabel.equals(tag))
			System.out.println("  " + fieldLabel);
		else
			System.out.println("  " + fieldLabel + " (" + tag + ")");
	}

	private static void showObjectName(BaseObject object)
	{
		String typeName = object.getTypeName();
		if(object.getType() == Task.getObjectType())
			typeName = "Activity/Method/Task";
		System.out.println(typeName);
	}
}
