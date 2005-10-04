/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import java.util.EventObject;

import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;

public class DiagramModelEvent extends EventObject 
{

	public DiagramModelEvent(Object source, EAMGraphCell nodeToUse) 
	{
		super(source);
		node = nodeToUse;
	}
	
	public EAMGraphCell getNode()
	{
		return node;
	}
	
	private EAMGraphCell node;
}
