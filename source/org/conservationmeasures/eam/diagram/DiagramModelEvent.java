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

	public DiagramModelEvent(Object source, EAMGraphCell nodeToUse, int indexToUse) 
	{
		super(source);
		node = nodeToUse;
		index = indexToUse;
	}
	
	public EAMGraphCell getNode()
	{
		return node;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	private EAMGraphCell node;
	private int index;
}
