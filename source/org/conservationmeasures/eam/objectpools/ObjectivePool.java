/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Objective;

public class ObjectivePool extends DesirePool
{
	public ObjectivePool()
	{
		super(ObjectType.OBJECTIVE);
	}
	
	public Objective find(BaseId id)
	{
		return (Objective)findDesire(id);
	}

}
