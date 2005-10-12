/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;
import java.util.HashMap;

public class NodeData 
{
	public NodeData(DiagramNode node) throws Exception
	{
		data = new HashMap();
		data.put(ID, new Integer(node.getId()));
		data.put(TYPE, new Integer(node.getNodeType()));
		data.put(TEXT, node.getText());
		data.put(LOCATION, node.getLocation());
	}
	
	public int getId()
	{
		return ((Integer)data.get(ID)).intValue();
	}
	
	public int getType()
	{
		return ((Integer)data.get(TYPE)).intValue();
	}
	
	public String getText()
	{
		return (String)data.get(TEXT);
	}
	
	public Point getLocation()
	{
		return (Point)data.get(LOCATION);
	}
	
	public static String ID = "id";
	public static String LOCATION = "location";
	public static String TEXT = "text";
	public static String TYPE = "type";

	private HashMap data;
	
}
