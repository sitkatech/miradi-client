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
import org.miradi.objects.RatingCriterion;
import org.miradi.project.ObjectManager;

public class RatingCriterionPool extends EAMNormalObjectPool
{
	public RatingCriterionPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.RATING_CRITERION);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new RatingCriterion(objectManager, actualId);
	}

}
