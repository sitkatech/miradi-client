/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Objective extends Desire
{
	public Objective(BaseId id)
	{
		super(id);
	}
	
	public Objective(int idAsInt, EnhancedJsonObject json)
	{
		super(new BaseId(idAsInt), json);
	}
	
	public int getType()
	{
		return ObjectType.OBJECTIVE;
	}
	
}
