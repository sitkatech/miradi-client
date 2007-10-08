/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.conservationmeasures.eam.views.planning.RowManager;


abstract public class AbstractTreeNodeMoveDoer extends AbstractTreeNodeDoer
{
	abstract int getDelta();
	
	public boolean isAvailable()
	{
		try
		{
			Task task = getSingleSelectedTask();
			if(task == null)
				return false;
			
			if(!parentIsVisible(task))
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
	
			ORef parentRef = getParentOfTask(task);
			BaseObject parent = getProject().findObject(parentRef);
			String tag = getTaskIdsTag(parent);
			CommandSetObjectData cmd = new CommandSetObjectData(parent.getRef(), tag, newSiblings.toString());
			getProject().executeCommand(cmd);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: " + e.getMessage());
		}
		
	}

	public static String getTaskIdsTag(BaseObject container) throws Exception
	{
		int type = container.getType();
		switch(type)
		{
			case ObjectType.TASK:
				return Task.TAG_SUBTASK_IDS;
			case ObjectType.STRATEGY:
				return Strategy.TAG_ACTIVITY_IDS;
			case ObjectType.INDICATOR:
				return Indicator.TAG_TASK_IDS;
		}
		
		throw new Exception("getTaskIdsTag called for non-task container type " + type);
	}

	Task getSingleSelectedTask()
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
		ORef parentRef = getParentOfTask(task);
		if (parentRef.isInvalid())
			return new IdList();
		
		BaseObject parent = getProject().findObject(parentRef);
		if(parent == null)
			return new IdList();
		
		return getCurrentTaskList(parent);
	}

	private ORef getParentOfTask(Task task)
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
		String parentTasksTag = getTaskIdsTag(parent);
		IdList siblings = new IdList(parent.getData(parentTasksTag));
		return siblings;
	}
	
	private boolean parentIsVisible(Task task) throws Exception
	{
		ViewData viewData = getProject().getViewData(PlanningView.getViewName());
		CodeList visibleRowCodes = RowManager.getVisibleRowCodes(viewData);
		return (visibleRowCodes.contains(task.getParentTypeCode()));
	}
}
