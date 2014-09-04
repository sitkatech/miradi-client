/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz1;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.schemas.AccountingCodeSchema;
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.FundingSourceSchema;
import org.miradi.xml.wcs.Xmpz1XmlConstants;
import org.w3c.dom.Node;

public class ExpenseAssignmentPoolImporter extends AbstractAssignmentPoolImporter
{
	public ExpenseAssignmentPoolImporter(Xmpz1XmlImporter importerToUse)
	{
		super(importerToUse, Xmpz1XmlConstants.EXPENSE_ASSIGNMENT, ExpenseAssignmentSchema.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		super.importFields(node, destinationRef);
		
		importOptionalRef(node, destinationRef, ExpenseAssignment.TAG_FUNDING_SOURCE_REF, Xmpz1XmlConstants.FUNDING_SOURCE, FundingSourceSchema.getObjectType());
		importOptionalRef(node, destinationRef, ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, Xmpz1XmlConstants.ACCOUNTING_CODE, AccountingCodeSchema.getObjectType());
	}
	
	@Override
	protected String getDateUnitsElementName()
	{
		return Xmpz1XmlConstants.DATE_UNITS_EXPENSE;
	}
	
	@Override
	protected String getDayElementName()
	{
		return Xmpz1XmlConstants.EXPENSES_DAY;
	}
	
	@Override
	protected String getMonthElementName()
	{
		return Xmpz1XmlConstants.EXPENSES_MONTH;
	}
	
	@Override
	protected String getQuarterElementName()
	{
		return Xmpz1XmlConstants.EXPENSES_QUARTER;
	}
	
	@Override
	protected String getYearElementName()
	{
		return Xmpz1XmlConstants.EXPENSES_YEAR;
	}
	
	@Override
	protected String getFullProjectTimespanElementName()
	{
		return Xmpz1XmlConstants.EXPENSES_FULL_PROJECT_TIMESPAN;
	}
	
	@Override
	protected String getQuantatityElementName()
	{
		return Xmpz1XmlConstants.EXPENSE;
	}

	@Override
	protected String getDateUnitElementName()
	{
		return Xmpz1XmlConstants.EXPENSES_DATE_UNIT;
	}
}
