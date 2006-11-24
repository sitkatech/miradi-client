/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.factortypes;


public class FactorTypeTarget extends FactorType
{
	public boolean isTarget()
	{
		return true;
	}
	
	public String toString()
	{
		return TARGET_TYPE;
	}

	public static final String TARGET_TYPE = "Target";

}
