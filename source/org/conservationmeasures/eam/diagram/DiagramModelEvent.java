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

	public DiagramModelEvent(EAMGraphCell nodeToUse, int indexToUse) 
	{
		super(nodeToUse);
		index = indexToUse;
	}
	
	public EAMGraphCell getNode()
	{
		return (EAMGraphCell)getSource();
	}
	
	public int getIndex()
	{
		return index;
	}
	
	private int index;

}
