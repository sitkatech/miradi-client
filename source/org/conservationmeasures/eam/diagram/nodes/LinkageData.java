/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

public class LinkageData 
{
	public LinkageData(Linkage linkage) throws Exception
	{
		id = linkage.getId();
		fromNodeId = linkage.getFromNode().getId();
		toNodeId = linkage.getToNode().getId();
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
