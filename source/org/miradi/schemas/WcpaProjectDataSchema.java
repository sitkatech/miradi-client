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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.WcpaProjectData;

public class WcpaProjectDataSchema extends BaseObjectSchema
{
	public WcpaProjectDataSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_LEGAL_STATUS);
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_LEGISLATIVE);
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_PHYSICAL_DESCRIPTION);
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION);
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION);
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_HISTORICAL_DESCRIPTION);
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_CULTURAL_DESCRIPTION);
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_ACCESS_INFORMATION);
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_VISITATION_INFORMATION);
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_CURRENT_LAND_USES);
		createFieldSchemaMultiLineUserText(WcpaProjectData.TAG_MANAGEMENT_RESOURCES);
	}
	
	public static int getObjectType()
	{
		return ObjectType.WCPA_PROJECT_DATA;
	}
	
	@Override
	public int getType()
	{
		return ObjectType.FAKE;
	}

	@Override
	public String getObjectName()
	{
		return OBJECT_NAME;
	}
	
	public static final String OBJECT_NAME = "WCPAProjectData";
}
