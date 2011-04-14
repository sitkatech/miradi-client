/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.wcs;

import java.util.HashMap;
import java.util.Vector;

import org.miradi.objecthelpers.CategorizedQuantity;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.TimePeriodCosts;

public class ExpenseEntryWriter extends AbstractTimePeriodCostsWriter
{
	public ExpenseEntryWriter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse);
	}
	
	public void writeExpenseAssignmentTimePeriodCosts(HashMap<DateUnit, TimePeriodCosts> dateUnitTimePeriodCostsMap)	throws Exception
	{
		getWcsXmlExporter().writeStartElement(getCalculatedEntriesElementName());
		for (DateUnit dateUnit : dateUnitTimePeriodCostsMap.keySet())
		{
			TimePeriodCosts timePeriodCosts = dateUnitTimePeriodCostsMap.get(dateUnit);
			Vector<CategorizedQuantity> categorizedQuantaties = getCategorizedQuantaties(timePeriodCosts);
			writeCategorizedQuantaties(dateUnit, categorizedQuantaties, getEntryElementName());
		}
		
		getWcsXmlExporter().writeEndElement(getCalculatedEntriesElementName());
	}

	@Override
	protected Vector<CategorizedQuantity> getCategorizedQuantaties(TimePeriodCosts timePeriodCosts)
	{
		return timePeriodCosts.getExpensesCategorizedQuantities();
	}

	@Override
	protected String getDateUnitElementName()
	{
		return XmpzXmlConstants.EXPENSES_DATE_UNIT;
	}
	
	@Override
	protected String getQuantityDateUnitElementName()
	{
		return XmpzXmlConstants.DATE_UNITS_EXPENSE;
	}
	
	@Override
	protected String getDayElementName()
	{
		return EXPENSES_DAY;
	}
	
	@Override
	protected String getMonthElementName()
	{
		return EXPENSES_MONTH;
	}
	
	@Override
	protected String getQuarterElementName()
	{
		return EXPENSES_QUARTER;
	}
	
	@Override
	protected String getYearElementName()
	{
		return EXPENSES_YEAR;
	}
	
	@Override
	protected String getFullProjectTimespanElementName()
	{
		return EXPENSES_FULL_PROJECT_TIMESPAN;
	}
	
	@Override
	protected String getQuantatityElementName()
	{
		return XmpzXmlConstants.EXPENSE;
	}

	@Override
	protected String getEntryElementName()
	{
		return EXPENSE_ENTRY;
	}

	@Override
	protected String getCalculatedEntriesElementName()
	{
		return CALCULATED_EXPENSE_ENTRIES;
	}
}
