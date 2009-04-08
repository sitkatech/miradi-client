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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Task;

abstract public class AbstractTreeNodeSequenceChangeDoer extends AbstractTreeTaskNodeMoveDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;

		Task task = getSingleSelectedTask();
		if(!hasAdjacentParentInSelectionHierarchy(task))
			return false;

		return true;
	}

	private boolean hasAdjacentParentInSelectionHierarchy(Task task)
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
		int parentType = task.getTypeOfParent();
		int taskIndex = getTaskIndexInSelectionHierarchy(task);
		int possibleParentIndex = taskIndex + 1;
		ORef possibleParentRef = getSelectionHierarchy().get(possibleParentIndex);
		ORefList parentReferrerRefs = task.findObjectsThatReferToUs(parentType);
		if (parentReferrerRefs.contains(possibleParentRef))
			return possibleParentIndex;
		
		return -1;
	}
}
