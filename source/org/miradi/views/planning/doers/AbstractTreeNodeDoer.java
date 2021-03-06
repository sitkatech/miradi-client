/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.planning.doers;

import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Task;
import org.miradi.utils.CodeList;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractTreeNodeDoer extends ObjectsDoer
{
	protected boolean childWouldBeVisible(String rowTypeCode) throws Exception
	{
		if (isPlanningView())
		{
			RowColumnProvider rowColumnProvider = getPlanningView().getRowColumnProvider();
			CodeList visibleRowCodes = rowColumnProvider.getRowCodesToShow();
			return (visibleRowCodes.contains(rowTypeCode));
		}

		if (isWorkPlanView())
		{
			RowColumnProvider rowColumnProvider = getWorkPlanView().getRowColumnProvider();
			CodeList visibleRowCodes = rowColumnProvider.getRowCodesToShow();
			return (visibleRowCodes.contains(rowTypeCode));
		}

		return true;
	}

	protected boolean hasAdjacentParentInSelectionHierarchy(Task task) throws Exception
	{
		int taskIndex = getTaskIndexInSelectionHierarchy(task);
		int parentIndex = getParentIndexInSelectionHierarchy(task);
		if (parentIndex < 0 || taskIndex < 0)
			return false;
		  
		int expectedParentIndexOfTask = taskIndex + 1;
		return parentIndex == expectedParentIndexOfTask;
	}

	private int getTaskIndexInSelectionHierarchy(Task task)
	{
		ORefList selectionHierarchy = getSelectionHierarchy();
		return selectionHierarchy.find(task.getRef());
	}

	private int getParentIndexInSelectionHierarchy(Task task)
	{
		int taskIndex = getTaskIndexInSelectionHierarchy(task);
		int possibleParentIndex = taskIndex + 1;
		ORef possibleParentRef = getSelectionHierarchy().get(possibleParentIndex);
		ORefList parentReferrerRefs = task.getParentRefs();
		if (parentReferrerRefs.contains(possibleParentRef))
			return possibleParentIndex;
		
		return -1;
	}
}
