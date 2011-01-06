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
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.xml.generic.XmlSchemaCreator;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

public class ResourceAssignmentPoolImporter extends	AbstractAssignmentPoolImporter
{
	public ResourceAssignmentPoolImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, RESOURCE_ASSIGNMENT, ResourceAssignment.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		super.importFields(node, destinationRef);
		
		importOptionalId(node, destinationRef, ResourceAssignment.TAG_ACCOUNTING_CODE_ID, ACCOUNTING_CODE, AccountingCode.getObjectType());
		importOptionalId(node, destinationRef, ResourceAssignment.TAG_FUNDING_SOURCE_ID, FUNDING_SOURCE, FundingSource.getObjectType());
		importOptionalId(node, destinationRef, ResourceAssignment.TAG_RESOURCE_ID, XmlSchemaCreator.RESOURCE_ID_ELEMENT_NAME, ProjectResource.getObjectType());
	}
	
	@Override
	protected String getDateUnitsElementName()
	{
		return XmpzXmlConstants.DATE_UNIT_WORK_UNITS;
	}
	
	@Override
	protected String getDateUnitElementName()
	{
		return XmpzXmlConstants.WORK_UNITS_DATE_UNIT;
	}
	
	@Override
	protected String getDayElementName()
	{
		return WORK_UNITS_DAY;
	}
	
	@Override
	protected String getMonthElementName()
	{
		return WORK_UNITS_MONTH;
	}
	
	@Override
	protected String getQuarterElementName()
	{
		return WORK_UNITS_QUARTER;
	}
	
	@Override
	protected String getYearElementName()
	{
		return WORK_UNITS_YEAR;
	}
	
	@Override
	protected String getFullProjectTimespanElementName()
	{
		return WORK_UNITS_FULL_PROJECT_TIMESPAN;
	}
	
	@Override
	protected String getQuantatityElementName()
	{
		return XmpzXmlConstants.WORK_UNITS;
	}
}
