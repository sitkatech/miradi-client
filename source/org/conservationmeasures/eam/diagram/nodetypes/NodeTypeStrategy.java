/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodetypes;


public class NodeTypeStrategy extends NodeType
{
	public boolean isStrategy()
	{
		return true;
	}
	
	public String toString()
	{
		return STRATEGY_TYPE; 
	}

	public static final String STRATEGY_TYPE = "Intervention";

}
