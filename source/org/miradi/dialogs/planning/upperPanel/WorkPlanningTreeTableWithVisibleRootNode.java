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

import org.miradi.actions.*;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;

import java.util.HashSet;
import java.util.Set;

public class WorkPlanningTreeTableWithVisibleRootNode extends PlanningTreeTableWithVisibleRootNode
{
	public WorkPlanningTreeTableWithVisibleRootNode(MainWindow mainWindowToUse, GenericTreeTableModel planningTreeModelToUse)
	{
		super(mainWindowToUse, planningTreeModelToUse);
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
}
