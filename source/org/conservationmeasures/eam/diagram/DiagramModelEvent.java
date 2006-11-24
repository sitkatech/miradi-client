/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import java.util.EventObject;

import org.conservationmeasures.eam.diagram.nodes.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;


public class DiagramModelEvent extends EventObject 
{
	public DiagramModelEvent(Object source, EAMGraphCell cellToUse) 
	{
		super(source);
		cell = cellToUse;
	}
	
	public DiagramNode getNode()
	{
		return (DiagramNode)cell;
	}
	
	public DiagramFactorLink getLinkage()
	{
		return (DiagramFactorLink)cell;
	}
	
	private EAMGraphCell cell;
}
