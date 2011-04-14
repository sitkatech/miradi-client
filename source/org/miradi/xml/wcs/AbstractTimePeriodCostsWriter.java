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

import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.CategorizedQuantity;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.utils.DoubleUtilities;

abstract public class AbstractTimePeriodCostsWriter implements XmpzXmlConstants
{
	public AbstractTimePeriodCostsWriter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		wcsXmlExporter = wcsXmlExporterToUse;
	}

	private UnicodeWriter getWriter()
	{
		return getWcsXmlExporter().getWriter();
	}
	
	protected XmpzXmlExporter getWcsXmlExporter()
	{
		return wcsXmlExporter;
	}
	
	public void writeTimePeriodCosts(HashMap<DateUnit, TimePeriodCosts> dateUnitTimePeriodCostsMap) throws Exception
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
	
	private void writeCategorizedQuantaties(DateUnit dateUnit, Vector<CategorizedQuantity> categorizedQuantaties, String dateUnitsDetailsParentElementName) throws Exception
	{
		for (CategorizedQuantity categorizedQuantity : categorizedQuantaties)
		{
			getWcsXmlExporter().writeStartElement(dateUnitsDetailsParentElementName);
			
			getWcsXmlExporter().exportValidId(categorizedQuantity.getResourceRef(), dateUnitsDetailsParentElementName, RESOURCE_ID);
			getWcsXmlExporter().exportValidId(categorizedQuantity.getFundingSourceRef(), dateUnitsDetailsParentElementName, FUNDING_SOURCE_ID);
			getWcsXmlExporter().exportValidId(categorizedQuantity.getAccountingCodeRef(), dateUnitsDetailsParentElementName, ACCOUNTING_CODE_ID);
			getWcsXmlExporter().exportValidId(categorizedQuantity.getCategoryOneRef(), dateUnitsDetailsParentElementName, BUDGET_CATEGORY_ONE_ID);
			getWcsXmlExporter().exportValidId(categorizedQuantity.getCategoryTwoRef(), dateUnitsDetailsParentElementName, BUDGET_CATEGORY_TWO_ID);
			if (categorizedQuantity.getQuantity().hasValue())
				writeEffortDetails(dateUnitsDetailsParentElementName + DETAILS, dateUnit, categorizedQuantity.getQuantity().getValue());
			
			getWcsXmlExporter().writeEndElement(dateUnitsDetailsParentElementName);
		}
	}
	
	public void writeEffortDetails(String dateUnitsElementName, DateUnit dateUnit, double quantity) throws Exception
	{
		getWcsXmlExporter().writeStartElement(dateUnitsElementName);
		getWcsXmlExporter().writeStartElement(getQuantityDateUnitElementName());
		writeDateUnit(dateUnit);
		writeQuantity(quantity);
		getWcsXmlExporter().writeEndElement(getQuantityDateUnitElementName());
		getWcsXmlExporter().writeEndElement(dateUnitsElementName);
	}

	private void writeDateUnit(DateUnit dateUnit) throws Exception
	{
		getWcsXmlExporter().writeStartElement(getDateUnitElementName());
		
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
		
		getWcsXmlExporter().writeEndElement(getDateUnitElementName());
	}

	private void writeDay(DateUnit dateUnit) throws Exception
	{
		getWcsXmlExporter().writeStartElementWithAttribute(getWriter(), getDayElementName(), DATE, dateUnit.toString());
		getWcsXmlExporter().writeEndElement(getDayElementName());
	}


	private void writeMonth(DateUnit dateUnit) throws Exception
	{
		getWcsXmlExporter().writeStartElementWithTwoAttributes(getWriter(), getMonthElementName(), YEAR, dateUnit.getYear(), MONTH, dateUnit.getMonth());
		getWcsXmlExporter().writeEndElement(getMonthElementName());
	}

	private void writeQuarter(DateUnit dateUnit) throws Exception
	{
		MultiCalendar start = dateUnit.getQuarterDateRange().getStartDate();
		getWcsXmlExporter().writeStartElementWithTwoAttributes(getWriter(), getQuarterElementName(), YEAR, start.getGregorianYear(), START_MONTH, start.getGregorianMonth());
		getWcsXmlExporter().writeEndElement(getQuarterElementName());
	}

	private void writeYear(DateUnit dateUnit) throws Exception
	{
		int yearStartMonth = dateUnit.getYearStartMonth();
		int year2 = Integer.parseInt(dateUnit.getYearYearString());
		getWcsXmlExporter().writeStartElementWithTwoAttributes(getWriter(), getYearElementName(), START_YEAR, year2, START_MONTH, yearStartMonth);
		getWcsXmlExporter().writeEndElement(getYearElementName());
	}

	private void writeProjectTotal(DateUnit dateUnit) throws Exception
	{		
		getWcsXmlExporter().writeStartElementWithAttribute(getWriter(), getFullProjectTimespanElementName(), WORK_UNITS_FULL_PROJECT_TIMESPAN, "Total");
		getWcsXmlExporter().writeEndElement(getFullProjectTimespanElementName());
	}

	private void writeQuantity(double expense) throws Exception
	{
		getWcsXmlExporter().writeStartElement(getQuantatityElementName());
		
		String formattedForExpense = DoubleUtilities.toStringForData(expense);
		getWriter().write(formattedForExpense);
		
		getWcsXmlExporter().writeEndElement(getQuantatityElementName());
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
	
	private XmpzXmlExporter wcsXmlExporter;
}
