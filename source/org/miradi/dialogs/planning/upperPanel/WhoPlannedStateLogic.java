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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourcePlan;
import org.miradi.project.Project;
import org.miradi.utils.DateUnitEffortList;

public class WhoPlannedStateLogic
{
	public WhoPlannedStateLogic(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public boolean isWhoCellEditable(BaseObject baseObjectToUse)
	{
		try
		{
			if (!BaseObject.canOwnPlanningObjects(baseObjectToUse.getRef()))
				return false;

			return doAllResourcePlansHaveIdenticalWorkUnits(baseObjectToUse);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	private boolean doAllResourcePlansHaveIdenticalWorkUnits(BaseObject baseObjectToUse) throws Exception
	{
			ORefList resourcePlans = baseObjectToUse.getResourcePlanRefs();
			DateUnitEffortList expectedDateUnitEffortList = null;
			for (int index = 0; index < resourcePlans.size(); ++index)
			{
				ResourcePlan resourcePlan = ResourcePlan.find(getProject(), resourcePlans.get(index));
				DateUnitEffortList thisDateUnitEffortList = resourcePlan.getDateUnitEffortList();
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
