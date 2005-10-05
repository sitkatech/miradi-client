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
		text = node.getText();
		location = node.getLocation();
		type = node.getNodeType();
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
	
	public int getType()
	{
		return type;
	}
	
	private String text;
	private Point location;
	private int id;
	private int type;
}
