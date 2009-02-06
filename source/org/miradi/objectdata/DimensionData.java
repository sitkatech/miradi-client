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

import java.awt.Dimension;

import org.miradi.utils.EnhancedJsonObject;

public class DimensionData extends ObjectData
{

	public DimensionData(String tagToUse)
	{
		super(tagToUse);
		dimension = new Dimension(0, 0);
	}

	public Dimension getDimension()
	{
		return dimension;
	}
	
	public void setDimension(Dimension dimensionToUse)
	{
		dimension = dimensionToUse;
	}

	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			dimension = new Dimension(0, 0);
			return;
		}
		
		dimension = EnhancedJsonObject.convertToDimension(newValue);		
	}

	public String get()
	{
		if(dimension == null)
			return "";
		
		return EnhancedJsonObject.convertFromDimension(dimension);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof DimensionData))
			return false;
		
		DimensionData other = (DimensionData)rawOther;
		return dimension.equals(other.dimension);
	}

	public int hashCode()
	{
		return dimension.hashCode();
	}
	
	Dimension dimension;
}
