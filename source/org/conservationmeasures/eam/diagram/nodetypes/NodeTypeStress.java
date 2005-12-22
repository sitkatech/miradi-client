/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodetypes;

import org.conservationmeasures.eam.main.EAM;


public class NodeTypeStress extends NodeTypeFactor
{
	public boolean isStress() 
	{
		return true;
	}
	
	public String toString()
	{
		return EAM.text("Type|Stress");
	}
}
