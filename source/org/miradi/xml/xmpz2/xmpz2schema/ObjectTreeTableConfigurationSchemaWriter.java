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

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.schemas.BaseObjectSchema;

public class ObjectTreeTableConfigurationSchemaWriter extends BaseObjectSchemaWriter
{
	public ObjectTreeTableConfigurationSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(creatorToUse, baseObjectSchemaToUse);
	}

	@Override
	protected boolean shouldOmitField(String tag)
	{
		//NOTE: These were omitted from XMPZ1. See Jira MRD-4920 for status in XMPZ2
		if (tag.equals(ObjectTreeTableConfiguration.TAG_COL_CONFIGURATION))
			return true;
		
		if (tag.equals(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION))
			return true;
		
		return super.shouldOmitField(tag);
	}
}
