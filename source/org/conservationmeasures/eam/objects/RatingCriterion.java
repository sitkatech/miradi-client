/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class RatingCriterion extends EAMBaseObject
{
	public RatingCriterion(BaseId idToUse)
	{
		super(idToUse);
	}
	
	public RatingCriterion(int idAsInt, EnhancedJsonObject json)
	{
		super(new BaseId(idAsInt), json);
	}
	
	public int getType()
	{
		return ObjectType.RATING_CRITERION;
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();

		return json;
	}
	
}
