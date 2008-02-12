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
import org.miradi.objects.BaseObject;
import org.miradi.objects.Goal;
import org.miradi.project.ObjectManager;

public class GoalPool extends DesirePool
{
	public GoalPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.GOAL);
	}
	
	public Goal find(BaseId id)
	{
		return (Goal)findDesire(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new Goal(objectManager, actualId);
	}

}
