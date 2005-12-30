/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.objects.ConceptualModelLinkage;

public class LinkagePool extends ObjectPool
{
	public void put(ConceptualModelLinkage linkage)
	{
		put(linkage.getId(), linkage);
	}
	
	public ConceptualModelLinkage find(int id)
	{
		return (ConceptualModelLinkage)getRawObject(id);
	}
}
