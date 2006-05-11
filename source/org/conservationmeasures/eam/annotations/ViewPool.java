/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.ObjectPool;

public class ViewPool extends ObjectPool
{
	public void put(ViewData viewData)
	{
		put(viewData.getId(), viewData);
	}
	
	public ViewData find(int id)
	{
		return (ViewData)getRawObject(id);
	}

}
