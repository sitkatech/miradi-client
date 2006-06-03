/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.EAMObjectPool;

public class ViewPool extends EAMObjectPool
{
	public void put(ViewData viewData)
	{
		put(viewData.getId(), viewData);
	}
	
	public ViewData find(int id)
	{
		return (ViewData)getRawObject(id);
	}

	public ViewData findByLabel(String label)
	{
		int[] ids = getIds();
		for(int i = 0; i < ids.length; ++i)
		{
			ViewData viewData = find(ids[i]);
			if(viewData.getLabel().equals(label))
				return viewData;
		}
		return null;
	}
}
