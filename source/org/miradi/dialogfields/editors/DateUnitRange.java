/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields.editors;

import java.util.Collections;
import java.util.Vector;

import org.miradi.objecthelpers.DateUnit;

public class DateUnitRange
{
	public DateUnitRange(Vector<DateUnit> dateUnitsToUse) throws Exception
	{
		dateUnits = dateUnitsToUse;
		
		Collections.sort(dateUnits);
	}
	
	public DateUnit getStartDateUnit()
	{
		return dateUnits.get(0);
	}

	public DateUnit getEndDateUnit()
	{
		return dateUnits.get(1);
	}
	
	private Vector<DateUnit> dateUnits;
}
