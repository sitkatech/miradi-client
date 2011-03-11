/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.generic;

import java.io.IOException;

public class DashboardStatusEntrySchemaElement extends FieldSchemaElement
{
	protected DashboardStatusEntrySchemaElement(String objectTypeNameToUse, String tagToUse)
	{
		super(objectTypeNameToUse, tagToUse);
	}
	
	@Override
	public void output(SchemaWriter writer) throws IOException
	{
		super.output(writer);
		
		writer.write(" { " +  DASHBOARD_STATUS_ENTRY + ".element* }?");
	}
}
