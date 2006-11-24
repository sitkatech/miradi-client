/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.factortypes;


public class FactorTypeCluster extends FactorType
{
	public boolean isFactorCluster()
	{
		return true;
	}

	public String toString()
	{
		return CLUSTER_TYPE;
	}

	public static final String CLUSTER_TYPE = "Cluster";

}
