/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodetypes;



public class NodeTypeFactor extends NodeType
{
	public boolean isFactor()
	{
		return true;
	}
	
	public String toString()
	{
		return FACTOR_TYPE;
	}

	public static final String FACTOR_TYPE = "Factor";

}
