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

package org.miradi.objecthelpers;

import java.util.Comparator;

import org.miradi.objects.ProjectResource;

public class ProjectResourceLeaderAtTopSorter implements Comparator<ProjectResource>
{
	public ProjectResourceLeaderAtTopSorter(ORef resourceLeaderRefToUse)
	{
		resourceLeaderRef = resourceLeaderRefToUse;
	}

	public int compare(ProjectResource resource1, ProjectResource resource2)
	{
		if (resource1.getRef().equals(resource2.getRef()))
			return 0;
		
		if (resource1.getRef().equals(resourceLeaderRef))
			return -1;
		
		if (resource2.getRef().equals(resourceLeaderRef))
			return 1;
		
		
		return BaseObjectByFullNameSorter.compareBaseObjects(resource1, resource2);
	}
	
	private ORef resourceLeaderRef;
}
