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
		Measurement measurement1979 = getProject().createMeasurement();
		measurement1979.setData(Measurement.TAG_DATE, MultiCalendar.createFromGregorianYearMonthDay(1979, 1, 1).toString());
		
		Measurement measurementWithNoDate = getProject().createMeasurement();
		
		ORef measurementRef3 = getProject().createObject(Measurement.getObjectType(), new BaseId(9999));
		Measurement measurementWithNoDateAndId9999 = Measurement.find(getProject(), measurementRef3);
	
		assertEquals("identical without date?", 0, BaseObjectDateAndIdComparator.compare(measurementWithNoDate, measurementWithNoDate, Measurement.TAG_DATE));
		assertEquals("blank date after 1979?", -1, BaseObjectDateAndIdComparator.compare(measurement1979, measurementWithNoDate, Measurement.TAG_DATE));
		assertEquals("blank date before 1979?", 1, BaseObjectDateAndIdComparator.compare(measurementWithNoDate, measurement1979, Measurement.TAG_DATE));
		assertEquals("identical with same dates?", 0, BaseObjectDateAndIdComparator.compare(measurement1979, measurement1979, Measurement.TAG_DATE));
		assertEquals("blank date after id 9999?", -1, BaseObjectDateAndIdComparator.compare(measurementWithNoDateAndId9999, measurementWithNoDate, Measurement.TAG_DATE));
		
		Measurement measurementWithDate2008 = getProject().createMeasurement();
		measurementWithDate2008.setData(Measurement.TAG_DATE, MultiCalendar.createFromGregorianYearMonthDay(2008, 9, 9).toString());
		assertTrue("2008 not after 1979?", BaseObjectDateAndIdComparator.compare(measurement1979, measurementWithDate2008, Measurement.TAG_DATE) < 0);
	}
}
