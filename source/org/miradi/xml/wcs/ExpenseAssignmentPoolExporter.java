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
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;

public class ExpenseAssignmentPoolExporter extends AbstractAssignmentPoolExporter
{
	public ExpenseAssignmentPoolExporter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, EXPENSE_ASSIGNMENT, ExpenseAssignment.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		ExpenseAssignment expenseAssignment = (ExpenseAssignment) baseObject;
		exportDateUnitEfforList(expenseAssignment.getDateUnitEffortList(), XmpzXmlConstants.DATE_UNITS_EXPENSE);
	}
	
	protected String getDateUnitElementName()
	{
		return XmpzXmlConstants.EXPENSES_DATE_UNIT;
	}
	
	protected String getDayElementName()
	{
		return EXPENSES_DAY;
	}
	
	protected String getMonthElementName()
	{
		return EXPENSES_MONTH;
	}
	
	protected String getQuarterElementName()
	{
		return EXPENSES_QUARTER;
	}
	
	protected String getYearElementName()
	{
		return EXPENSES_YEAR;
	}
	
	protected String getFullProjectTimespanElementName()
	{
		return EXPENSES_FULL_PROJECT_TIMESPAN;
	}
	
	protected String getQuantatityElementName()
	{
		return XmpzXmlConstants.EXPENSE;
	}
}
