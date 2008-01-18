/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.io.File;
import java.io.IOException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.CreateThreatStressRatingParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Translation;
import org.martus.util.DirectoryUtils;

public class ListAllFields
{
	public static void main(String[] args) throws Exception
	{
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
		Translation.loadFieldLabels();
		for(int type = 0; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			if(project.getPool(type) == null)
				continue;
			BaseObject object = createObject(project, type);
			showObjectName(object);
			String[] fieldTags = object.getFieldTags();
			for(int field = 0; field < fieldTags.length; ++field)
			{
				String tag = fieldTags[field];
				if(object.isPseudoField(tag))
					continue;
				showField(object, tag);
			}
		}
	}

	private static BaseObject createObject(Project project, int type) throws Exception
	{
		CreateObjectParameter extraInfo = createExtraInfo(type);
		ORef ref = new ORef(type, project.createObject(type, extraInfo));
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
