/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;

public class WorkPlanViewMainTableModel extends PlanningViewMainTableModel
{
	public WorkPlanViewMainTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse) throws Exception
	{
		super(projectToUse, providerToUse, rowColumnProviderToUse);
	}

	@Override
	protected ChoiceItem createAppendedRelevantIndicatorLabels(BaseObject baseObject) throws Exception
	{
		if (Task.is(baseObject.getRef()) && ((Task) baseObject).isMonitoringActivity())
			return super.createAppendedRelevantIndicatorLabels(baseObject);

		return new EmptyChoiceItem();
	}

	@Override
	public String getColumnName(int column)
	{
		String columnTag = getColumnTag(column);
		return EAM.fieldLabel(ObjectType.FAKE, columnTag);
	}
}
