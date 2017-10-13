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

package org.miradi.dialogs.planning.upperPanel;

import org.miradi.actions.*;
import org.miradi.dialogs.planning.RightClickActionProvider;
import org.miradi.dialogs.planning.propertiesPanel.PlanningRightClickHandler;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.UmbrellaView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class WorkPlanningTreeTableWithVisibleRootNode extends PlanningTreeTableWithVisibleRootNode implements RightClickActionProvider
{
	public WorkPlanningTreeTableWithVisibleRootNode(MainWindow mainWindowToUse, GenericTreeTableModel planningTreeModelToUse)
	{
		super(mainWindowToUse, planningTreeModelToUse);

		addMouseListener(new PlanningRightClickHandler(getMainWindow(), this, this));
	}

	@Override
	protected Set<Class> getRelevantActions()
	{
		HashSet<Class> relevantActions = new HashSet<Class>();
		relevantActions.addAll(super.getRelevantActions());
		relevantActions.add(ActionTreeCreateResourceAssignment.class);
		relevantActions.add(ActionTreeCreateExpenseAssignment.class);
		relevantActions.add(ActionExpandToMenu.class);
		relevantActions.add(ActionExpandToStrategy.class);
		relevantActions.add(ActionExpandToActivity.class);

		return relevantActions;
	}

	@Override
	public boolean elementMatchesTypeToExpandTo(int typeToExpandTo, ORef elementRef)
	{
		if (elementRef.getObjectType() == ObjectType.TASK)
		{
			BaseObject baseObject = getProject().findObject(elementRef);
			return Task.isActivity(baseObject);
		}

		return super.elementMatchesTypeToExpandTo(typeToExpandTo, elementRef);
	}

	@Override
	public Vector<Action> getActionsForRightClickMenu(int row, int tableColumn)
	{
		Vector<Action> actions = new Vector<Action>();

		BaseObject baseObject = getBaseObjectForRowColumn(row, tableColumn);
		actions.addAll(WorkPlanUpperMultiTable.getWorkPlanActionsForBaseObject(baseObject, getActions()));
		actions.add(getActions().get(ActionDeletePlanningViewTreeNode.class));
		actions.add(null);
		actions.add(getActions().get(ActionExpandAllRows.class));
		actions.add(getActions().get(ActionCollapseAllRows.class));
		actions.add(null);
		actions.addAll(getActionsForAssignments());

		return actions;
	}

	protected ArrayList<Action> getActionsForAssignments()
	{
		UmbrellaView currentView = getMainWindow().getCurrentView();
		if(currentView == null)
			return new ArrayList<Action>();

		return WorkPlanUpperMultiTable.getActionsForAssignments(currentView);
	}

	private Actions getActions()
	{
		return getMainWindow().getActions();
	}
}
