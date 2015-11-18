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

package org.miradi.dialogs.planning.upperPanel;

import org.miradi.dialogs.planning.AssignmentDateUnitsTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.OptionalDouble;

public class WhoStateLogic
{
	public WhoStateLogic(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public boolean isAssignedWhoCellEditable(BaseObject baseObjectToUse)
	{
		try
		{
			if (!AssignmentDateUnitsTableModel.canOwnAssignments(baseObjectToUse.getRef()))
				return false;

			if (doAnySubtasksHaveAnyWorkUnitData(baseObjectToUse))
				return false;

			return doAllResourceAssignmentsHaveIdenticalWorkUnits(baseObjectToUse);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;		
		}
	}
	
	private boolean doAnySubtasksHaveAnyWorkUnitData(BaseObject baseObjectForRow) throws Exception
	{
		TimePeriodCostsMap timePeriodCostsMap = baseObjectForRow.getTotalTimePeriodCostsMapForSubTasks(baseObjectForRow.getSubTaskRefs(), BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
		TimePeriodCosts wholeProjectTimePeriodCosts = timePeriodCostsMap.calculateTimePeriodCosts(new DateUnit());
		OptionalDouble totalSubtaskWorkUnitsForAllTimePeriods = wholeProjectTimePeriodCosts.getTotalWorkUnits();

		return totalSubtaskWorkUnitsForAllTimePeriods.hasValue();
	}
	
	private boolean doAllResourceAssignmentsHaveIdenticalWorkUnits(BaseObject baseObjectToUse) throws Exception
	{
			ORefList resourceAssignments = baseObjectToUse.getResourceAssignmentRefs();
			DateUnitEffortList expectedDateUnitEffortList = null;
			for (int index = 0; index < resourceAssignments.size(); ++index)
			{
				ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignments.get(index));
				DateUnitEffortList thisDateUnitEffortList = resourceAssignment.getDateUnitEffortList();
				if (expectedDateUnitEffortList == null)
					expectedDateUnitEffortList = thisDateUnitEffortList;
				
				if (!expectedDateUnitEffortList.equals(thisDateUnitEffortList))
					return false;
			}
			
			return true;
	}
	
	private Project getProject()
	{
		return project;
	}

	private Project project;
}
