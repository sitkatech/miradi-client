/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ObjectType;



public class Objective extends EAMObject
{
	public Objective(int id)
	{
		super(id);
	}
	
	public int getType()
	{
		return ObjectType.OBJECTIVE;
	}
	
	public String getShortLabel()
	{
		return "xxx";
	}
}
