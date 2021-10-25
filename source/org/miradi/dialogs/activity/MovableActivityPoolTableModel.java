/*
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.activity;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.project.ProjectTotalCalculatorStrategy;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;
import org.miradi.schemas.TaskSchema;
import org.miradi.views.planning.PlanningView;
import org.miradi.views.workplan.WorkPlanView;

public class MovableActivityPoolTableModel extends ObjectPoolTableModel
{
    public MovableActivityPoolTableModel(Project projectToUse, ORef parentRefToUse)
    {
        super(projectToUse, ObjectType.TASK, COLUMN_TAGS);
        parentRef = parentRefToUse;
    }

    private static final String[] COLUMN_TAGS = new String[] {
        Task.TAG_SHORT_LABEL, Task.TAG_LABEL, Task.PSEUDO_TAG_ACTIVITY_TYPE_LABEL,
    };

    @Override
    public ORefList getLatestRefListFromProject()
    {
        return new ORefList(getRowObjectType(), getLatestIdListFromProject());
    }

    private IdList getLatestIdListFromProject()
    {
        ORefList filteredTaskRefs = new ORefList();
        Strategy  parentStrategy = (Strategy) getProject().findObject(parentRef);

        ORefList parentActivityRefs = parentStrategy.getActivityRefs();

        ORefList taskRefs = super.getLatestRefListFromProject();
        for (int i = 0; i < taskRefs.size(); ++i)
        {
            Task task = (Task) getProject().findObject(taskRefs.get(i));
            if (!task.isActivity())
                continue;

            if (parentActivityRefs.contains(task.getRef()))
                continue;

            if (!task.isMonitoringActivity() && !shouldIncludeActivities())
                continue;

            if (task.isMonitoringActivity() && !shouldIncludeMonitoringActivities())
                continue;

            filteredTaskRefs.add(taskRefs.get(i));
        }

        return filteredTaskRefs.convertToIdList(TaskSchema.getObjectType());
    }

    @Override
    public String getUniqueTableModelIdentifier()
    {
        return UNIQUE_MODEL_IDENTIFIER;
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }

    private String getWorkPlanBudgetMode()
    {
        ProjectTotalCalculatorStrategy projectTotalCalculatorStrategy = getProject().getTimePeriodCostsMapsCache().getProjectTotalCalculator().getProjectTotalCalculatorStrategy();
        return projectTotalCalculatorStrategy.getWorkPlanBudgetMode();
    }

    private ViewData getPlanningViewData() throws Exception
    {
        return getProject().getViewData(PlanningView.getViewName());
    }

    private boolean isPlanningView()
    {
        return getProject().getCurrentView().equals(PlanningView.getViewName());
    }

    private boolean isWorkPlanView()
    {
        return getProject().getCurrentView().equals(WorkPlanView.getViewName());
    }

    private boolean shouldIncludeActivities()
    {
        try
        {
            if (isWorkPlanView())
                if (getWorkPlanBudgetMode().equals(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE))
                    return false;

            if (isPlanningView())
                return getPlanningViewData().shouldIncludeActivities();

            return true;
        }
        catch (Exception ex)
        {
            EAM.logException(ex);
        }

        return false;
    }

    private boolean shouldIncludeMonitoringActivities()
    {
        try
        {
            if (isWorkPlanView())
                if (getWorkPlanBudgetMode().equals(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE))
                    return false;

            if (isPlanningView())
                return getPlanningViewData().shouldIncludeMonitoringActivities();

            return true;
        }
        catch (Exception ex)
        {
            EAM.logException(ex);
        }

        return false;
    }

    private static final String UNIQUE_MODEL_IDENTIFIER = "MovableActivityPoolTableModel";

    private ORef parentRef;
}
