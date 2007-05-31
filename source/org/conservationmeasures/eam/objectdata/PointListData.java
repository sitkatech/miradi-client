/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import java.awt.Point;
import java.util.List;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.BendPointList;

//FIXME sub class this for BendPointList
public class PointListData extends ObjectData
{
	public PointListData()
	{
		points = new BendPointList();
	}
	
	public PointListData(String valueToUse)
	{
		this();
		try
		{
			set(valueToUse);
		}
		catch (Exception e)
		{
			EAM.logDebug("PointListData ignoring invalid: " + valueToUse);
		}
	}

	public void set(String newValue) throws Exception
	{
		set(new BendPointList(newValue));
	}
	
	public String get()
	{
		return points.toString();
	}
	
	public void set(BendPointList newPoints)
	{
		points = newPoints;
	}
	
	public BendPointList getPointList()
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
	
	BendPointList points;
}
