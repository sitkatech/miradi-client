/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
