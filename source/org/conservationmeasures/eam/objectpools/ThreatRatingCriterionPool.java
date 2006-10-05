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
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;

public class ThreatRatingCriterionPool extends EAMNormalObjectPool
{
	public ThreatRatingCriterionPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.THREAT_RATING_CRITERION);
	}

	EAMObject createRawObject(BaseId actualId)
	{
		return new ThreatRatingCriterion(actualId);
	}

}
