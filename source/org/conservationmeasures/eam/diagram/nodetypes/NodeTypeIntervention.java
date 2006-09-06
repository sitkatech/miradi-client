/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodetypes;


public class NodeTypeIntervention extends NodeType
{
	public boolean isIntervention()
	{
		return true;
	}
	
	public String toString()
	{
		return INTERVENTION_TYPE; 
	}

	public static final String INTERVENTION_TYPE = "Intervention";

}
