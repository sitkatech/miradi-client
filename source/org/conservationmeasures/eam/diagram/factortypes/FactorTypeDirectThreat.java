/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.factortypes;

import org.conservationmeasures.eam.main.EAM;



public class FactorTypeDirectThreat extends FactorTypeCause
{
	public boolean isDirectThreat() 
	{
		return true;
	}
	
	public String toString()
	{
		return EAM.text("Type|Direct Threat");
	}
}
