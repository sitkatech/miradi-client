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

import java.text.ParseException;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.doers.AbstractChangeSequenceDoer;


abstract public class AbstractTreeTaskNodeMoveDoer extends AbstractChangeSequenceDoer
{
	protected abstract int getDelta();
	
	public boolean isAvailable()
	{
		try
		{
			Task task = getSingleSelectedTask();
			if(task == null)
				return false;
			
			IdList siblings = getSiblingList(task);
			if(siblings == null)
				return false;
			
			int oldPosition = siblings.find(task.getId());
			if(oldPosition < 0)
				return false;
	
			int newPosition = oldPosition + getDelta();
			if(newPosition < 0 || newPosition >= siblings.size())
				return false;
			
			return true;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			Task task = getSingleSelectedTask();
			IdList newSiblings = new IdList(getSiblingList(task));
	
			int wasAt = newSiblings.find(task.getId());
			newSiblings.removeId(task.getId());
			newSiblings.insertAt(task.getId(), wasAt + getDelta());
	
			ORef parentRef = getSelectedParentRef(task);
			BaseObject parent = getProject().findObject(parentRef);
			String tag = Task.getTaskIdsTag(parent);
			CommandSetObjectData cmd = new CommandSetObjectData(parent.getRef(), tag, newSiblings.toString());
			getProject().executeCommand(cmd);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: " + e.getMessage());
		}
		
	}

	protected Task getSingleSelectedTask()
	{
		BaseObject selected = getSingleSelectedObject();
		if(selected == null)
			return null;
		
		if(selected.getType() != Task.getObjectType())
			return null;
		
		return (Task)selected;
	}

	private IdList getSiblingList(Task task) throws Exception
	{
		ORef parentRef = getSelectedParentRef(task);
		if (parentRef.isInvalid())
			return new IdList(Task.getObjectType());
		
		BaseObject parent = getProject().findObject(parentRef);
		if(parent == null)
			return new IdList(Task.getObjectType());
		
		return getCurrentTaskList(parent);
	}

	private ORef getSelectedParentRef(Task task)
	{
		ORefList selectionHierarchy = getSelectionHierarchy();
		ORefList referrerRefs = getAllReferrersAndOwners(task);
		for(int i = 0; i < referrerRefs.size(); ++i)
		{
			if (selectionHierarchy.contains(referrerRefs.get(i)))
				return referrerRefs.get(i);
		}
		
		return ORef.INVALID;
	}

	private ORefList getAllReferrersAndOwners(Task task)
	{
		ORefList allReferrers = new ORefList();
		allReferrers.addAll(task.findObjectsThatReferToUs(Indicator.getObjectType()));
		allReferrers.addAll(task.findObjectsThatReferToUs(Strategy.getObjectType()));
		allReferrers.add(task.getOwnerRef());
		
		return allReferrers;
	}

	private IdList getCurrentTaskList(BaseObject parent) throws Exception, ParseException
	{
		String parentTasksTag = Task.getTaskIdsTag(parent);
		IdList siblings = new IdList(Task.getObjectType(), parent.getData(parentTasksTag));
		return siblings;
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
		int parentType = task.getTypeOfParent();
		int taskIndex = getTaskIndexInSelectionHierarchy(task);
		int possibleParentIndex = taskIndex + 1;
		ORef possibleParentRef = getSelectionHierarchy().get(possibleParentIndex);
		ORefList parentReferrerRefs = task.findObjectsThatReferToUs(parentType);
		if (parentReferrerRefs.contains(possibleParentRef))
			return possibleParentIndex;
		
		return -1;
	}

	protected static final int DELTA_UP_VALUE = -1;
	protected static final int DELTA_DOWN_VALUE = 1;	
}
