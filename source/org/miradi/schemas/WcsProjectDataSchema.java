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

import org.miradi.objects.WcsProjectData;

public class WcsProjectDataSchema extends BaseObjectSchema
{
	public WcsProjectDataSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaSingleLineUserText(WcsProjectData.TAG_ORGANIZATIONAL_FOCUS);
		createFieldSchemaSingleLineUserText(WcsProjectData.TAG_ORGANIZATIONAL_LEVEL);
		createFieldSchemaBoolean(WcsProjectData.TAG_SWOT_COMPLETED);
		createFieldSchemaSingleLineUserText(WcsProjectData.TAG_SWOT_URL);
		createFieldSchemaBoolean(WcsProjectData.TAG_STEP_COMPLETED);
		createFieldSchemaSingleLineUserText(WcsProjectData.TAG_STEP_URL);
	}
	
	@Override
	public String getXmpz2ElementName()
	{
		return WcsProjectData.OBJECT_NAME;
	}
}
