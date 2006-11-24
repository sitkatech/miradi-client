/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodetypes;



public class NodeTypeCause extends NodeType
{
	public boolean isCause()
	{
		return true;
	}
	
	public String toString()
	{
		return CAUSE_TYPE;
	}

	public static final String CAUSE_TYPE = "Factor";

}
