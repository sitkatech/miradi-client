/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2;

import org.martus.util.MultiCalendar;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.DoubleUtilities;

abstract public class AbstractAssignmentExporter extends BaseObjectExporter
{
	public AbstractAssignmentExporter(Xmpz2XmlUnicodeWriter writerToUse)
	{
		super(writerToUse);
	}
	
	@Override
	protected void writeFields(final BaseObject baseObject,	BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		Assignment expenseAssignment = (Assignment) baseObject;
		exportDateUnitEfforList(expenseAssignment.getDateUnitEffortList(), getDateUnitsElementName());
	}

	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Assignment.TAG_DATEUNIT_EFFORTS))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
	
	protected void exportDateUnitEfforList(DateUnitEffortList dateUnitEffortList, String dateUnitsElementName) throws Exception
	{
		final String dateUnitEffortsElementName = getPoolName() + Assignment.TAG_DATEUNIT_EFFORTS;
		getWriter().writeStartElement(dateUnitEffortsElementName);
		for (int index = 0; index < dateUnitEffortList.size(); ++index)
		{
			DateUnitEffort dateUnitEffort = dateUnitEffortList.getDateUnitEffort(index);
			getWriter().writeStartElement(dateUnitsElementName);
			
			writeDateUnit(dateUnitEffort.getDateUnit());
			writeQuantity(dateUnitEffort.getQuantity());
			
			getWriter().writeEndElement(dateUnitsElementName);
		}
		getWriter().writeEndElement(dateUnitEffortsElementName);
	}

	private void writeDateUnit(DateUnit dateUnit) throws Exception
	{
		getWriter().writeStartElement(getDateUnitElementName());
		
		if (dateUnit.isProjectTotal())
			writeProjectTotal(dateUnit);
		
		if (dateUnit.isYear())
			writeYear(dateUnit);
		
		if (dateUnit.isQuarter())
			writeQuarter(dateUnit);
		
		if (dateUnit.isMonth())
			writeMonth(dateUnit);
		
		if (dateUnit.isDay())
			writeDay(dateUnit);
		
		getWriter().writeEndElement(getDateUnitElementName());
	}

	private void writeDay(DateUnit dateUnit) throws Exception
	{
		getWriter().writeStartElementWithAttribute(getDayElementName(), DATE, dateUnit.toString());
		getWriter().writeEndElement(getDayElementName());
	}


	private void writeMonth(DateUnit dateUnit) throws Exception
	{
		getWriter().writeStartElementWithTwoAttributes(getMonthElementName(), YEAR, dateUnit.getYear(), MONTH, dateUnit.getMonth());
		getWriter().writeEndElement(getMonthElementName());
	}

	private void writeQuarter(DateUnit dateUnit) throws Exception
	{
		MultiCalendar start = dateUnit.getQuarterDateRange().getStartDate();
		getWriter().writeStartElementWithTwoAttributes(getQuarterElementName(), YEAR, start.getGregorianYear(), START_MONTH, start.getGregorianMonth());
		getWriter().writeEndElement(getQuarterElementName());
	}

	private void writeYear(DateUnit dateUnit) throws Exception
	{
		int yearStartMonth = dateUnit.getYearStartMonth();
		int year2 = Integer.parseInt(dateUnit.getYearYearString());
		getWriter().writeStartElementWithTwoAttributes(getYearElementName(), START_YEAR, year2, START_MONTH, yearStartMonth);
		getWriter().writeEndElement(getYearElementName());
	}

	private void writeProjectTotal(DateUnit dateUnit) throws Exception
	{		
		getWriter().writeStartElementWithAttribute(getFullProjectTimespanElementName(), FULL_PROJECT_TIMESPAN, "Total");
		getWriter().writeEndElement(getFullProjectTimespanElementName());
	}

	private void writeQuantity(double expense) throws Exception
	{
		getWriter().writeStartElement(getQuantatityElementName());
		
		String formattedForExpense = DoubleUtilities.toStringForData(expense);
		getWriter().write(formattedForExpense);
		
		getWriter().writeEndElement(getQuantatityElementName());
	}
	
	abstract protected String getDateUnitElementName();
	
	abstract protected String getDayElementName();
	
	abstract protected String getMonthElementName();
	
	abstract protected String getQuarterElementName();
	
	abstract protected String getYearElementName();
	
	abstract protected String getFullProjectTimespanElementName();
	
	abstract protected String getQuantatityElementName();
	
	abstract protected String getDateUnitsElementName();
	
	abstract protected String getPoolName();
}
