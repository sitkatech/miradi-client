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
	
	public int getInt(String tag)
	{
		return ((Integer)data.get(tag)).intValue();
	}
	
	public String getString(String tag)
	{
		return (String)data.get(tag);
	}
	
	public Point getPoint(String tag)
	{
		return (Point)data.get(tag);
	}
	
	final public static String ID = "id";
	final public static String LOCATION = "location";
	final public static String TEXT = "text";
	final public static String TYPE = "type";

	private HashMap data;
}
