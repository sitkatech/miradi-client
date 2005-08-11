/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;

public class NodeData 
{
	public NodeData(EAMGraphCell cell) throws Exception
	{
		if(!cell.isNode())
			throw new Exception("EAMGraphCell not a Node");
		id = cell.getId();
		text = cell.getText();
		location = cell.getLocation();
		type = ((Node)cell).getNodeType();
	}
	
	public String getText()
	{
		return text;
	}
	
	public Point getLocation()
	{
		return location;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getNodeType()
	{
		return type;
	}
	
	private String text;
	private Point location;
	private int id;
	private int type;
}
