/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;

public class CreateModelNodeParameter extends CreateObjectParameter
{
	public CreateModelNodeParameter(NodeType type)
	{
		nodeType = type;
	}

	public NodeType getNodeType()
	{
		return nodeType;
	}
	
	NodeType nodeType;
}
