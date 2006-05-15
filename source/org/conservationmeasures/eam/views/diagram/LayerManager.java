/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.util.HashSet;
import java.util.Set;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.objects.IdList;

public class LayerManager
{
	public LayerManager()
	{
		hiddenNodeTypes = new HashSet();
		hiddenIds = new IdList();
	}
	
	public boolean isVisible(DiagramNode node)
	{
		if(!isTypeVisible(node.getClass()))
			return false;
		
		if(hiddenIds.contains(node.getId()))
			return false;
		
		return true;
	}

	public boolean isTypeVisible(Class nodeClass)
	{
		return !hiddenNodeTypes.contains(nodeClass);
	}
	
	public void setVisibility(Class nodeClass, boolean newVisibility)
	{
		if(newVisibility)
			hiddenNodeTypes.remove(nodeClass);
		else
			hiddenNodeTypes.add(nodeClass);
	}
	
	public boolean areAllNodesVisible()
	{
		return hiddenNodeTypes.isEmpty() && hiddenIds.isEmpty();
	}
	
	public void setHiddenIds(IdList idsToHide)
	{
		hiddenIds = new IdList(idsToHide);
	}

	Set hiddenNodeTypes;
	IdList hiddenIds;
}
