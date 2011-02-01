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

package org.miradi.dialogs.planning;

import org.miradi.objects.PlanningTreeConfiguration;
import org.miradi.objects.ProjectResource;
import org.miradi.utils.CodeList;

public class ProjectResourceCoreRowColumnProvider implements PlanningTreeConfiguration
{
	public CodeList getColumnCodesToShow() throws Exception
	{
		return getVisibleColumns();
	}

	public static CodeList getVisibleColumns()
	{
		return new CodeList(new String[] {
				ProjectResource.TAG_RESOURCE_TYPE,
				ProjectResource.TAG_INITIALS,
				ProjectResource.TAG_GIVEN_NAME,
				ProjectResource.TAG_SUR_NAME,
				ProjectResource.TAG_ORGANIZATION,
				ProjectResource.TAG_POSITION,
				ProjectResource.TAG_LOCATION,
				ProjectResource.TAG_EMAIL,
		});
	}

	public CodeList getRowCodesToShow() throws Exception
	{
		return new CodeList(new String[] {
				ProjectResource.OBJECT_NAME,
		});
	}

	public boolean shouldIncludeResultsChain() throws Exception
	{
		return true;
	}

	public boolean shouldIncludeConceptualModelPage() throws Exception
	{
		return true;
	}
}
