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

public class StartEndDateUnitProvider
{
	public StartEndDateUnitProvider(Vector<DateUnit> dateUnitsToUse) throws Exception
	{
		dateUnits = dateUnitsToUse;
		ensureSameTypeDateUnits();
		
		Collections.sort(dateUnits);
	}
	
	private void ensureSameTypeDateUnits()
	{
		if (!areBothTheSame(getStartDateUnit(), getEndDateUnit()))
			throw new RuntimeException("Start and End dateUnit are not the same type");
	}

	private boolean areBothTheSame(DateUnit startDateUnit, DateUnit endDateUnit)
	{
		if (startDateUnit == null)
			return endDateUnit == null;
		
		if (startDateUnit.isDay())
			return endDateUnit.isDay();

		if (startDateUnit.isMonth())
			return endDateUnit.isMonth();
		
		if (startDateUnit.isQuarter())
			return endDateUnit.isQuarter();
		
		if (startDateUnit.isYear())
			return endDateUnit.isYear();
		
		if (startDateUnit.isProjectTotal())
			return endDateUnit.isProjectTotal();
		
		return false;
	}

	public DateUnit getStartDateUnit()
	{
		if (dateUnits.isEmpty())
			return null;
		
		return dateUnits.firstElement();
	}

	public DateUnit getEndDateUnit()
	{
		if (dateUnits.isEmpty())
			return null;
		
		return dateUnits.lastElement();
	}
	
	private Vector<DateUnit> dateUnits;
}
