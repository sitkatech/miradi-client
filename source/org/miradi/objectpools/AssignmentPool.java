/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.project.ObjectManager;

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
	
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new Assignment(objectManager ,actualId);
	}
}
