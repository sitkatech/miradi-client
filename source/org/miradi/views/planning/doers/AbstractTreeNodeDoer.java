/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;
import org.miradi.utils.CodeList;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractTreeNodeDoer extends ObjectsDoer
{
	protected BaseObject getSingleSelectedObject()
	{
		BaseObject[] selectedObjects = getObjects();
		if(selectedObjects.length != 1)
			return null;
		
		return selectedObjects[0];
	}

	protected boolean childWouldBeVisible(String objectTypeName) throws Exception
	{
		//TODO this method only applies if we are inside planning view
		if (isPlanningView())
		{
			RowColumnProvider rowColumnProvider = getPlanningView().getRowColumnProvider();
			CodeList visibleRowCodes = rowColumnProvider.getRowListToShow();
			return (visibleRowCodes.contains(objectTypeName));
		}

		return true;
	}

	protected boolean hasAdjacentParentInSelectionHierarchy(Task task) throws Exception
	{
		ORefList selectionHierarchy = getSelectionHierarchy();
		int taskIndex = getTaskIndex(task, selectionHierarchy);
		int parentIndex = getParentIndex(selectionHierarchy, task);
		if (parentIndex < 0 || taskIndex < 0)
			return false;
		  
		int expectedParentIndexOfTask = taskIndex + 1;
		return parentIndex == expectedParentIndexOfTask;
	}

	private int getTaskIndex(Task task, ORefList selectionHierarchy)
	{
		return selectionHierarchy.find(task.getRef());
	}

	private int getParentIndex(ORefList selectionHierarchy, Task task)
	{
		int parentType = task.getTypeOfParent();
		int taskIndex = getTaskIndex(task, selectionHierarchy);
		int possibleParentIndex = taskIndex + 1;
		ORef possibleParentRef = selectionHierarchy.get(possibleParentIndex);
		ORefList parentReferrerRefs = task.findObjectsThatReferToUs(parentType);
		if (parentReferrerRefs.contains(possibleParentRef))
			return possibleParentIndex;
		
		return -1;
	}
}
