/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.utils;

import org.martus.util.MultiCalendar;
import org.miradi.ids.BaseId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Measurement;

public class TestBaseObjectDateAndIdComparator extends TestCaseWithProject
{
	public TestBaseObjectDateAndIdComparator(String name)
	{
		super(name);
	}
	
	public void testCompare() throws Exception
	{
		Measurement measurement1 = getProject().createMeasurement();
		measurement1.setData(Measurement.TAG_DATE, MultiCalendar.createFromGregorianYearMonthDay(1000, 1, 1).toString());
		
		Measurement measurement2 = getProject().createMeasurement();
		
		ORef measurementRef3 = getProject().createObject(Measurement.getObjectType(), new BaseId(9000));
		Measurement measurement3 = Measurement.find(getProject(), measurementRef3);
		
		assertEquals("wrong compare value for comparing date to blank?", -1, BaseObjectDateAndIdComparator.compare(measurement1, measurement2, Measurement.TAG_DATE));
		assertEquals("wrong compare value for comparing date to blank?", 1, BaseObjectDateAndIdComparator.compare(measurement2, measurement1, Measurement.TAG_DATE));
		assertEquals("wrong compare value for same object?", 0, BaseObjectDateAndIdComparator.compare(measurement1, measurement1, Measurement.TAG_DATE));
		assertEquals("wrong compare id value?", -1, BaseObjectDateAndIdComparator.compare(measurement3, measurement2, Measurement.TAG_DATE));
		
		measurement2.setData(Measurement.TAG_DATE, MultiCalendar.createFromGregorianYearMonthDay(9999, 9, 9).toString());
		assertTrue("wrong compare value for comparing dates?", BaseObjectDateAndIdComparator.compare(measurement1, measurement2, Measurement.TAG_DATE) > 0);
	}

}
