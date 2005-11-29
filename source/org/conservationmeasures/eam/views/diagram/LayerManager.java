/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.util.HashSet;
import java.util.Set;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.types.NodeType;

public class LayerManager
{
	public LayerManager()
	{
		hiddenNodeTypes = new HashSet();
	}
	
	public boolean isVisible(DiagramNode node)
	{
		return isTypeVisible(node.getType());
	}

	public boolean isTypeVisible(NodeType nodeType)
	{
		return !hiddenNodeTypes.contains(nodeType);
	}
	
	public void setVisibility(NodeType nodeType, boolean newVisibility)
	{
		if(newVisibility)
			hiddenNodeTypes.remove(nodeType);
		else
			hiddenNodeTypes.add(nodeType);
	}
	
	public boolean areAllLayersVisible()
	{
		return hiddenNodeTypes.isEmpty();
	}

	Set hiddenNodeTypes;
}
