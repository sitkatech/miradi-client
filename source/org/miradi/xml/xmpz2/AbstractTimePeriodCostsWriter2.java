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

import java.util.HashMap;
import java.util.Vector;

import org.martus.util.MultiCalendar;
import org.miradi.objecthelpers.CategorizedQuantity;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.utils.DoubleUtilities;
import org.miradi.xml.wcs.XmpzXmlConstants;

abstract public class AbstractTimePeriodCostsWriter2 implements XmpzXmlConstants
{
	public AbstractTimePeriodCostsWriter2(final Xmpz2XmlWriter writerToUse)
	{
		writer = writerToUse;
	}

	private Xmpz2XmlWriter getWriter()
	{
		return writer;
	}
	
	public void writeTimePeriodCosts(HashMap<DateUnit, TimePeriodCosts> dateUnitTimePeriodCostsMap) throws Exception
	{
		getWriter().writeStartElement(getCalculatedEntriesElementName());
		for (DateUnit dateUnit : dateUnitTimePeriodCostsMap.keySet())
		{
			TimePeriodCosts timePeriodCosts = dateUnitTimePeriodCostsMap.get(dateUnit);
			Vector<CategorizedQuantity> categorizedQuantities = getCategorizedQuantaties(timePeriodCosts);
			writeCategorizedQuantaties(dateUnit, categorizedQuantities, getEntryElementName());
		}
		
		getWriter().writeEndElement(getCalculatedEntriesElementName());
	}
	
	private void writeCategorizedQuantaties(DateUnit dateUnit, Vector<CategorizedQuantity> categorizedQuantaties, String dateUnitsDetailsParentElementName) throws Exception
	{
		for (CategorizedQuantity categorizedQuantity : categorizedQuantaties)
		{
			getWriter().writeStartElement(dateUnitsDetailsParentElementName);
			
			
			getWriter().writeValidRef(dateUnitsDetailsParentElementName, RESOURCE_ID, categorizedQuantity.getResourceRef());
			getWriter().writeValidRef(dateUnitsDetailsParentElementName, FUNDING_SOURCE_ID, categorizedQuantity.getFundingSourceRef());
			getWriter().writeValidRef(dateUnitsDetailsParentElementName, ACCOUNTING_CODE_ID, categorizedQuantity.getAccountingCodeRef());
			getWriter().writeValidRef(dateUnitsDetailsParentElementName, BUDGET_CATEGORY_ONE_ID, categorizedQuantity.getCategoryOneRef());
			getWriter().writeValidRef(dateUnitsDetailsParentElementName, BUDGET_CATEGORY_TWO_ID, categorizedQuantity.getCategoryTwoRef());
			if (categorizedQuantity.getQuantity().hasValue())
				writeEffortDetails(dateUnitsDetailsParentElementName + DETAILS, dateUnit, categorizedQuantity.getQuantity().getValue());
			
			getWriter().writeEndElement(dateUnitsDetailsParentElementName);
		}
	}
	
	public void writeEffortDetails(String dateUnitsElementName, DateUnit dateUnit, double quantity) throws Exception
	{
		getWriter().writeStartElement(dateUnitsElementName);
		getWriter().writeStartElement(getQuantityDateUnitElementName());
		writeDateUnit(dateUnit);
		writeQuantity(quantity);
		getWriter().writeEndElement(getQuantityDateUnitElementName());
		getWriter().writeEndElement(dateUnitsElementName);
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
		getWriter().writeEnclosedElement(getDayElementName(), DATE, dateUnit.toString());
	}


	private void writeMonth(DateUnit dateUnit) throws Exception
	{
		getWriter().writeSelfEnclosedElement(getMonthElementName(), YEAR, dateUnit.getYear(), MONTH, dateUnit.getMonth());
	}

	private void writeQuarter(DateUnit dateUnit) throws Exception
	{
		MultiCalendar start = dateUnit.getQuarterDateRange().getStartDate();
		getWriter().writeSelfEnclosedElement(getQuarterElementName(), YEAR, start.getGregorianYear(), START_MONTH, start.getGregorianMonth());
	}

	private void writeYear(DateUnit dateUnit) throws Exception
	{
		final int yearStartMonth = dateUnit.getYearStartMonth();
		final int startYear = Integer.parseInt(dateUnit.getYearYearString());
		getWriter().writeSelfEnclosedElement(getYearElementName(), START_YEAR, startYear, START_MONTH, yearStartMonth);
	}

	private void writeProjectTotal(DateUnit dateUnit) throws Exception
	{		
		getWriter().writeEnclosedElement(getFullProjectTimespanElementName(), FULL_PROJECT_TIMESPAN, "Total");
	}

	private void writeQuantity(double expense) throws Exception
	{
		getWriter().writeStartElement(getQuantatityElementName());
		
		String formattedForExpense = DoubleUtilities.toStringForData(expense);
		getWriter().write(formattedForExpense);
		
		getWriter().writeEndElement(getQuantatityElementName());
	}
	
	abstract protected Vector<CategorizedQuantity> getCategorizedQuantaties(TimePeriodCosts timePeriodCosts);
	
	abstract protected String getDateUnitElementName();
	
	abstract protected String getQuantityDateUnitElementName();
	
	abstract protected String getDayElementName();
	
	abstract protected String getMonthElementName();
	
	abstract protected String getQuarterElementName();
	
	abstract protected String getYearElementName();
	
	abstract protected String getFullProjectTimespanElementName();
	
	abstract protected String getQuantatityElementName();
	
	abstract protected String getCalculatedEntriesElementName();

	abstract protected String getEntryElementName();
	
	private Xmpz2XmlWriter writer;
}
