/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
	
	private boolean parentIsVisible(Task task) throws Exception
	{
		ViewData viewData = getProject().getViewData(PlanningView.getViewName());
		CodeList visibleRowCodes = RowManager.getVisibleRowCodes(viewData);
		return (visibleRowCodes.contains(task.getParentTypeCode()));
	}
}
