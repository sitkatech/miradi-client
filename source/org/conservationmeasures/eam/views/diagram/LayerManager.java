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
		return isTypeVisible(node.getNodeType());
	}

	public boolean isTypeVisible(int nodeType)
	{
		return !hiddenNodeTypes.contains(new Integer(nodeType));
	}
	
	public void setVisibility(int nodeType, boolean newVisibility)
	{
		Integer nodeTypeObject = new Integer(nodeType);
		if(newVisibility)
			hiddenNodeTypes.remove(nodeTypeObject);
		else
			hiddenNodeTypes.add(nodeTypeObject);
	}
	
	public boolean areAllLayersVisible()
	{
		return hiddenNodeTypes.isEmpty();
	}

	Set hiddenNodeTypes;
}
