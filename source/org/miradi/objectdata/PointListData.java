/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectdata;

import java.awt.Point;
import java.util.List;

import org.miradi.utils.PointList;

public class PointListData extends ObjectData
{
	public PointListData(String tagToUse)
	{
		super(tagToUse);
		points = new PointList();
	}
	
	public void set(String newValue) throws Exception
	{
		set(new PointList(newValue));
	}
	
	public String get()
	{
		return points.toString();
	}
	
	public void set(PointList newPoints)
	{
		points = newPoints;
	}
	
	public PointList getPointList()
	{
		return points;
	}
	
	public int size()
	{
		return points.size();
	}
	
	public Point get(int index)
	{
		return points.get(index);
	}
	
	public void add(Point point)
	{
		points.add(point);
	}
	
	public void addAll(List listToAdd)
	{
		points.addAll(listToAdd);
	}
	
	public void removePoint(Point point)
	{
		points.removePoint(point);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof PointListData))
			return false;
		
		PointListData other = (PointListData)rawOther;
		return points.equals(other.points);
	}

	public int hashCode()
	{
		return points.hashCode();
	}
	
	PointList points;
}
