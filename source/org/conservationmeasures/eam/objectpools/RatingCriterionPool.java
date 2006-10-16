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
import org.conservationmeasures.eam.objects.RatingCriterion;

public class RatingCriterionPool extends EAMNormalObjectPool
{
	public RatingCriterionPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.RATING_CRITERION);
	}

	EAMObject createRawObject(BaseId actualId)
	{
		return new RatingCriterion(actualId);
	}

}
