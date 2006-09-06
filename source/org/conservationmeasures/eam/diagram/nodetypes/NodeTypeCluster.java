/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodetypes;


public class NodeTypeCluster extends NodeType
{
	public boolean isCluster()
	{
		return true;
	}

	public String toString()
	{
		return CLUSTER_TYPE;
	}

	public static final String CLUSTER_TYPE = "Cluster";

}
