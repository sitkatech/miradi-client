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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.WcsProjectDataSchema;

public class WcsProjectData extends BaseObject
{
	public WcsProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, new WcsProjectDataSchema());
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public static WcsProjectData find(ObjectManager objectManager, ORef wcsProjectDataRef)
	{
		return (WcsProjectData) objectManager.findObject(wcsProjectDataRef);
	}
	
	public static WcsProjectData find(Project project, ORef wcsProjectDataRef)
	{
		return find(project.getObjectManager(), wcsProjectDataRef);
	}
	
	public static final String TAG_ORGANIZATIONAL_FOCUS = "OrganizationalFocus";
	public static final String TAG_ORGANIZATIONAL_LEVEL = "OrganizationalLevel";
	public static final String TAG_SWOT_COMPLETED = "SwotCompleted"; 
	public static final String TAG_SWOT_URL = "SwotUrl";
	public static final String TAG_STEP_COMPLETED = "StepCompleted";
	public static final String TAG_STEP_URL = "StepUrl";
}
