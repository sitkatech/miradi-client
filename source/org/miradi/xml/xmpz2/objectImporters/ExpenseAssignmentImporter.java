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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

public class ExpenseAssignmentImporter extends AbstractAssignmentImporter
{
	public ExpenseAssignmentImporter(Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(importerToUse, baseObjectSchemaToUse);
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
