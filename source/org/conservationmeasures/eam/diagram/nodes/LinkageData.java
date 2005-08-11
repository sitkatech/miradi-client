/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

public class LinkageData 
{
	public LinkageData(EAMGraphCell cell) throws Exception
	{
		if(!cell.isLinkage())
			throw new Exception("EAMGraphCell not a Linkage");
		id = cell.getId();
		fromNodeId = ((Linkage)cell).getFromNode().getId();
		toNodeId = ((Linkage)cell).getToNode().getId();
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getFromNodeId()
	{
		return fromNodeId;
	}
	
	public int getToNodeId()
	{
		return toNodeId;
	}
	
	private int id;
	private int fromNodeId;
	private int toNodeId;
}
