/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.EAMObject;

public class AssignmentPool extends EAMNormalObjectPool
{
	public AssignmentPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.ASSIGNMENT);
	}
	
	public void put(Assignment assignment)
	{
		put(assignment.getId(), assignment);
	}
	
	public Assignment find(BaseId id)
	{
		return (Assignment)getRawObject(id);
	}
	
	EAMObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new Assignment(actualId, (CreateAssignmentParameter)extraInfo);
	}
}
