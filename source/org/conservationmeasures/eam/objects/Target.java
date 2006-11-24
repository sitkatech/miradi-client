/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Target extends Factor
{
	public Target(ModelNodeId idToUse)
	{
		super(idToUse, Factor.TYPE_TARGET);
		clear();
	}
	
	public Target(ModelNodeId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, Factor.TYPE_TARGET, json);
	}

	public boolean isTarget()
	{
		return true;
	}
	
	public boolean canHaveGoal()
	{
		return true;
	}
}
