/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogfields.editors;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.questions.AssignedDateUnitTypeQuestion;
import org.miradi.utils.DateUnitEffortList;

public class WhenAssignedEditorComponent extends WhenEditorComponent
{
	public WhenAssignedEditorComponent(Project project, BaseObject baseObjectToUse) throws Exception
	{
		super(project, baseObjectToUse.getResourceAssignmentRefs(), new AssignedDateUnitTypeQuestion(baseObjectToUse.getProject(), baseObjectToUse.getResourceAssignmentRefs()));
	}

	@Override
	protected DateUnitEffortList getDateUnitEffortList(ORef planningObjectRef) throws Exception
	{
		ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), planningObjectRef);
		return resourceAssignment.getDateUnitEffortList();
	}

	@Override
	protected TimePeriodCostsMap getTimePeriodCostsMap(ORef planningObjectRef) throws Exception
	{
		ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), planningObjectRef);
		return resourceAssignment.getResourceAssignmentsTimePeriodCostsMap();
	}
}