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

import org.miradi.dialogs.planning.AbstractWorkPlanRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNodeAlwaysExpanded;
import org.miradi.dialogs.planning.upperPanel.rebuilder.AbstractTreeRebuilder;
import org.miradi.dialogs.planning.upperPanel.rebuilder.SharedWorkPlanTreeRebuilder;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.schemas.TaskSchema;

import java.util.Vector;

public class SharedWorkPlanTreeTableModel extends WorkPlanTreeTableModel
{
	public SharedWorkPlanTreeTableModel(Project project, PlanningTreeRootNodeAlwaysExpanded rootNode, AbstractWorkPlanRowColumnProvider rowColumnProvider) throws Exception
	{
		super(project, rootNode, rowColumnProvider);
	}

	@Override
	protected AbstractTreeRebuilder createTreeRebuilder()
	{
		return new SharedWorkPlanTreeRebuilder(getProject(), getRowColumnProvider());
	}

	public boolean treeHasSubTasks()
	{
		try
		{
			Vector<ORefList> fullyExpandedRefs = getFullyExpandedHierarchyRefListListIncludingLeafNodes();
			for(ORefList expandedRefList : fullyExpandedRefs)
			{
				if (expandedRefList.getFirstElement().getObjectType() == TaskSchema.getObjectType())
				{
					ORefList taskRefList = expandedRefList.getFilteredBy(TaskSchema.getObjectType());
					for (ORef taskRef : taskRefList)
					{
						Task task = Task.find(getProject(), taskRef);
						if (task.hasSubTasks())
							return true;
					}
				}
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}

		return false;
	}

	public static final String HAS_HIDDEN_SUB_TASKS_DOUBLE_ASTERISK = " ** ";
}
