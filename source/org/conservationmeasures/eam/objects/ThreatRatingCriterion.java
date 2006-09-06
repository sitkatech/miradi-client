/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;

public class ThreatRatingCriterion extends EAMBaseObject
{
	public ThreatRatingCriterion(BaseId idToUse)
	{
		super(idToUse);
	}
	
	public ThreatRatingCriterion(JSONObject json)
	{
		super(json);
	}
	
	public int getType()
	{
		return ObjectType.THREAT_RATING_CRITERION;
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();

		return json;
	}
	
}
