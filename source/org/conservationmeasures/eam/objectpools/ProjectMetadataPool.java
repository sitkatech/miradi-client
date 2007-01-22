/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
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

	EAMObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new ProjectMetadata(actualId);
	}

}
