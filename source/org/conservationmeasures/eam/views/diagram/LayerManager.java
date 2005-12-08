/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.util.HashSet;
import java.util.Set;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;

public class LayerManager
{
	public LayerManager()
	{
		hiddenNodeTypes = new HashSet();
	}
	
	public boolean isVisible(DiagramNode node)
	{
		return isTypeVisible(node.getClass());
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
	
	public boolean areAllLayersVisible()
	{
		return hiddenNodeTypes.isEmpty();
	}

	Set hiddenNodeTypes;
}
