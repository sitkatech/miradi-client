/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;

public class NodeData 
{
	public NodeData(Node node) throws Exception
	{
		id = node.getId();
		type = node.getNodeType();
		text = node.getText();
		location = node.getLocation();
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getType()
	{
		return type;
	}
	
	public String getText()
	{
		return text;
	}
	
	public Point getLocation()
	{
		return location;
	}
	
	public int getX()
	{
		return location.x;
	}
	
	public int getY()
	{
		return location.y;
	}
	
	private String text;
	private Point location;
	private int id;
	private int type;
}
