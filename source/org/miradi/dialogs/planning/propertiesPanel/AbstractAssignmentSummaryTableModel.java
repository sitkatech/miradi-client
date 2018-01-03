/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objecthelpers.TimePeriodCostsMapsCache;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.utils.ModelColumnTagProvider;

abstract public class AbstractAssignmentSummaryTableModel extends PlanningViewAbstractAssignmentTableModel implements ModelColumnTagProvider
{
	public AbstractAssignmentSummaryTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		if (isTotalsColumn(column))
			return false;

		return true;
	}

	@Override
	public boolean shouldSortRows()
	{
		return false;
	}

	public String getColumnTag(int modelColumn)
	{
		return "";
	}

	protected TimePeriodCosts calculateTotalTimePeriodAssignedCosts(Assignment assignment) throws Exception
	{
		return calculateTimePeriodAssignedCostsMap(assignment).calculateTimePeriodCosts(new DateUnit());
	}

	private TimePeriodCostsMap calculateTimePeriodAssignedCostsMap(BaseObject baseObject) throws Exception
	{
		return getTimePeriodCostsMapsCache().getTotalTimePeriodAssignedCostsMap(baseObject);
	}

	private TimePeriodCostsMapsCache getTimePeriodCostsMapsCache()
	{
		return getProject().getTimePeriodCostsMapsCache();
	}

	abstract public int getColumnCount();
	
	abstract public boolean isResourceColumn(int column);

	abstract public boolean isTotalsColumn(int column);
}
