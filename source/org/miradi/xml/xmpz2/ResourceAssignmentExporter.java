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

import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.generic.XmlSchemaCreator;


public class ResourceAssignmentExporter extends AbstractAssignmentExporter
{
	public ResourceAssignmentExporter(Xmpz2XmlUnicodeWriter writerToUse)
	{
		super(writerToUse);
	}
	
	@Override
	protected void writeFields(BaseObject baseObject, final BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		ResourceAssignment resourceAssignment = (ResourceAssignment) baseObject;
		String idElementName = XmlSchemaCreator.RESOURCE_ID_ELEMENT_NAME + ID;
		
		getWriter().writeRef(baseObjectSchema.getObjectName(), idElementName, resourceAssignment.getResourceRef());
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(ResourceAssignment.TAG_RESOURCE_ID))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}

	@Override
	protected String getDateUnitElementName()
	{
		return WORK_UNITS_DATE_UNIT;
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
		return WORK_UNITS;
	}

	@Override
	protected String getDateUnitsElementName()
	{
		return DATE_UNIT_WORK_UNITS;
	}
	
	@Override
	protected String getPoolName()
	{
		return RESOURCE_ASSIGNMENT;
	}
}
