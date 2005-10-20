/*
 * Copyright 2005, The Conservation Measures Partnership & Beneficent Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes.types;

import java.awt.Color;


public abstract class NodeType
{
	public abstract Color getColor();

	public boolean isTarget()
	{
		return false;
	}
	
	public boolean isIndirectFactor()
	{
		return false;
	}
	
	public boolean isIntervention()
	{
		return false;
	}
	
	public boolean isDirectThreat()
	{
		return false;
	}
	
	
	public boolean canHavePriority()
	{
		if(isDirectThreat())
			return true;
		if(isStress())
			return true;
		return false;
	}

	public boolean canHaveObjective()
	{
		if(isDirectThreat())
			return true;
		if(isIndirectFactor())
			return true;
		if(isIntervention())
			return true;
		if(isStress())
			return true;
		return false;
	}

	
	public boolean isStress()
	{
		return false;
	}

}
