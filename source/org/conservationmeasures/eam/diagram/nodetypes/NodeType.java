/*
 * Copyright 2005, The Conservation Measures Partnership & Beneficent Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodetypes;



public abstract class NodeType
{
	public boolean isIntervention()
	{
		return false;
	}
	
	public boolean isFactor()
	{
		return isIndirectFactor() || isDirectThreat();
	}
	
	public boolean isTarget()
	{
		return false;
	}
	
	public boolean isIndirectFactor()
	{
		return false;
	}
	
	public boolean isDirectThreat()
	{
		return false;
	}
	
	public boolean isCluster()
	{
		return false;
	}

	public boolean equals(Object other)
	{
		return getClass().getName().equals(other.getClass().getName());
	}

	public int hashCode()
	{
		return getClass().getName().hashCode();
	}

	public abstract String toString();
}
