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
			
			
			getWriter().writeRefIfValid(dateUnitsDetailsParentElementName, RESOURCE_ID, categorizedQuantity.getResourceRef());
			getWriter().writeRefIfValid(dateUnitsDetailsParentElementName, FUNDING_SOURCE_ID, categorizedQuantity.getFundingSourceRef());
			getWriter().writeRefIfValid(dateUnitsDetailsParentElementName, ACCOUNTING_CODE_ID, categorizedQuantity.getAccountingCodeRef());
			getWriter().writeRefIfValid(dateUnitsDetailsParentElementName, BUDGET_CATEGORY_ONE_ID, categorizedQuantity.getCategoryOneRef());
			getWriter().writeRefIfValid(dateUnitsDetailsParentElementName, BUDGET_CATEGORY_TWO_ID, categorizedQuantity.getCategoryTwoRef());
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
			getWriter().writeProjectTotal(dateUnit, getFullProjectTimespanElementName());
		
		if (dateUnit.isYear())
			getWriter().writeYear(dateUnit, getYearElementName());
		
		if (dateUnit.isQuarter())
			getWriter().writeQuarter(dateUnit, getQuarterElementName());
		
		if (dateUnit.isMonth())
			getWriter().writeMonth(dateUnit, getMonthElementName());
		
		if (dateUnit.isDay())
			getWriter().writeDay(dateUnit, getDayElementName());
		
		getWriter().writeEndElement(getDateUnitElementName());
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
