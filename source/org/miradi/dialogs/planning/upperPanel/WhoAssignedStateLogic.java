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
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.utils.OptionalDouble;

public class WhoAssignedStateLogic
{
	public WhoAssignedStateLogic()
	{
	}
	
	public boolean isWhoCellEditable(BaseObject baseObjectToUse)
	{
		try
		{
			if (!BaseObject.canOwnPlanningObjects(baseObjectToUse.getRef()))
				return false;

			if (doAnySubTasksHaveAnyWorkUnitData(baseObjectToUse))
				return false;

			return true;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;		
		}
	}
	
	private boolean doAnySubTasksHaveAnyWorkUnitData(BaseObject baseObjectForRow) throws Exception
	{
		TimePeriodCostsMap timePeriodCostsMap = baseObjectForRow.getTotalTimePeriodCostsMapForChildTasks(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
		TimePeriodCosts wholeProjectTimePeriodCosts = timePeriodCostsMap.calculateTimePeriodCosts(new DateUnit());
		OptionalDouble totalSubTaskWorkUnitsForAllTimePeriods = wholeProjectTimePeriodCosts.getTotalWorkUnits();

		return totalSubTaskWorkUnitsForAllTimePeriods.hasValue();
	}

}
