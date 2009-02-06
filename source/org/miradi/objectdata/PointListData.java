/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
