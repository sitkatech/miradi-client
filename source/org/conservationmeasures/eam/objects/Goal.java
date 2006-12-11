/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Goal extends Desire 
{
	public Goal(BaseId idToUse)
	{
		super(idToUse);
	}
	
	public Goal(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
	}
	
	public int getType()
	{
		return ObjectType.GOAL;
	}

	public static final String OBJECT_NAME = "Goal";
}
