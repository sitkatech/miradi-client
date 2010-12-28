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

package org.miradi.xml.xmpz;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.FundingSource;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

public class ExpenseAssignmentPoolImporter extends AbstractAssignmentPoolImporter
{
	public ExpenseAssignmentPoolImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, XmpzXmlConstants.EXPENSE_ASSIGNMENT, ExpenseAssignment.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		super.importFields(node, destinationRef);
		
		importOptionalRef(node, destinationRef, ExpenseAssignment.TAG_FUNDING_SOURCE_REF, XmpzXmlConstants.FUNDING_SOURCE, FundingSource.getObjectType());
		importOptionalRef(node, destinationRef, ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, XmpzXmlConstants.ACCOUNTING_CODE, AccountingCode.getObjectType());
	}
	
	@Override
	protected String getDateUnitsElementName()
	{
		return XmpzXmlConstants.DATE_UNITS_EXPENSE;
	}
	
	@Override
	protected String getDayElementName()
	{
		return XmpzXmlConstants.EXPENSES_DAY;
	}
	
	@Override
	protected String getMonthElementName()
	{
		return XmpzXmlConstants.EXPENSES_MONTH;
	}
	
	@Override
	protected String getQuarterElementName()
	{
		return XmpzXmlConstants.EXPENSES_QUARTER;
	}
	
	@Override
	protected String getYearElementName()
	{
		return XmpzXmlConstants.EXPENSES_YEAR;
	}
	
	@Override
	protected String getFullProjectTimespanElementName()
	{
		return XmpzXmlConstants.EXPENSES_FULL_PROJECT_TIMESPAN;
	}
	
	@Override
	protected String getQuantatityElementName()
	{
		return XmpzXmlConstants.EXPENSE;
	}

	@Override
	protected String getDateUnitElementName()
	{
		return XmpzXmlConstants.EXPENSES_DATE_UNIT;
	}
}
