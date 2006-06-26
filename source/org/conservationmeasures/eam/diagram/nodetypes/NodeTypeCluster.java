/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodetypes;

import org.conservationmeasures.eam.main.EAM;

public class NodeTypeCluster extends NodeType
{
	public boolean isCluster()
	{
		return true;
	}

	public String toString()
	{
		return EAM.text("Type|Cluster");
	}
}
