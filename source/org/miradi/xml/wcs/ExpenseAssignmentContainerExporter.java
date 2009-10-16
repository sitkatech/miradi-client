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

package org.miradi.xml.wcs;

import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

public class ExpenseAssignmentContainerExporter extends AbstractAssignmentContainerExporter
{
	public ExpenseAssignmentContainerExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, EXPENSE_ASSIGNMENT, ExpenseAssignment.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		ExpenseAssignment expenseAssignment = (ExpenseAssignment) baseObject;
		exportDateUnitExpenses(expenseAssignment.getDateUnitEffortList());
	}
	
	private void exportDateUnitExpenses(DateUnitEffortList dateUnitEffortList) throws Exception
	{
		getWcsXmlExporter().writeStartElement(getContainerName() + ExpenseAssignment.TAG_DATEUNIT_EFFORTS);
		for (int index = 0; index < dateUnitEffortList.size(); ++index)
		{
			DateUnitEffort dateUnitEffort = dateUnitEffortList.getDateUnitEffort(index);
			getWcsXmlExporter().writeStartElement(WcsXmlConstants.DATE_UNITS_EXPENSE);
			
			writeDateUnit(dateUnitEffort.getDateUnit());
			writeExpense(dateUnitEffort.getQuantity());
			
			getWcsXmlExporter().writeEndElement(WcsXmlConstants.DATE_UNITS_EXPENSE);
		}
		getWcsXmlExporter().writeEndElement(getContainerName() + ExpenseAssignment.TAG_DATEUNIT_EFFORTS);
	}

	private void writeDateUnit(DateUnit dateUnit) throws Exception
	{
		getWcsXmlExporter().writeStartElement(WcsXmlConstants.EXPENSES_DATE_UNIT);
		
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
		
		getWcsXmlExporter().writeEndElement(WcsXmlConstants.EXPENSES_DATE_UNIT);
	}

	private void writeDay(DateUnit dateUnit) throws Exception
	{
		getWcsXmlExporter().writeStartElementWithAttribute(getWriter(), EXPENSES_DAY, DATE, "9999-11-11");
		getWcsXmlExporter().writeEndElement(EXPENSES_DAY);
	}

	private void writeMonth(DateUnit dateUnit) throws Exception
	{
		getWcsXmlExporter().writeStartElementWithTwoAttributes(getWriter(), EXPENSES_MONTH, YEAR, dateUnit.getYear(), MONTH, dateUnit.getMonth());
		getWcsXmlExporter().writeEndElement(EXPENSES_QUARTER);
	}

	private void writeQuarter(DateUnit dateUnit) throws Exception
	{		
		getWcsXmlExporter().writeStartElementWithTwoAttributes(getWriter(), EXPENSES_QUARTER, YEAR, dateUnit.getYear(), START_MONTH, dateUnit.getYearStartMonth());
		getWcsXmlExporter().writeEndElement(EXPENSES_QUARTER);
	}

	private void writeYear(DateUnit dateUnit) throws Exception
	{
		int yearStartMonth = dateUnit.getYearStartMonth();
		int year2 = Integer.parseInt(dateUnit.getYearYearString());
		getWcsXmlExporter().writeStartElementWithTwoAttributes(getWriter(), EXPENSES_YEAR, START_YEAR, year2, START_MONTH, yearStartMonth);
		getWcsXmlExporter().writeEndElement(EXPENSES_YEAR);
	}

	private void writeProjectTotal(DateUnit dateUnit) throws Exception
	{		
		getWcsXmlExporter().writeStartElementWithAttribute(getWriter(), EXPENSES_FULL_PROJECT_TIMESPAN, WORK_UNITS_FULL_PROJECT_TIMESPAN, "Total");
		getWcsXmlExporter().writeEndElement(EXPENSES_FULL_PROJECT_TIMESPAN);
	}

	private void writeExpense(double expense) throws Exception
	{
		getWcsXmlExporter().writeStartElement(WcsXmlConstants.EXPENSE);
		
		getWriter().writeln(Double.toString(expense));
		
		getWcsXmlExporter().writeEndElement(WcsXmlConstants.EXPENSE);
	}
}
