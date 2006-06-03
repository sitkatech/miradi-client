/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.objects.EAMObject;

public class EAMObjectPool extends ObjectPool
{
	public EAMObject findObject(int id)
	{
		return (EAMObject)getRawObject(id);
	}
}
