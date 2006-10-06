/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ProjectMetadata;

public class ProjectMetadataPool extends EAMNormalObjectPool
{
	public ProjectMetadataPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.PROJECT_METADATA);
	}
	
	public ProjectMetadata find(BaseId id)
	{
		return (ProjectMetadata)findObject(id);
	}

	EAMObject createRawObject(BaseId actualId)
	{
		return new ProjectMetadata(actualId);
	}

}
