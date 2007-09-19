/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.conservationmeasures.eam.views.planning.RowManager;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class TreeNodeCreateTaskDoer extends AbstractTreeNodeDoer
{
	public boolean isAvailable()
	{
		try
		{
			BaseObject selected = getSingleSelectedObject();
			if(selected == null)
				return false;
			if(!canOwnTask(selected))
				return false;
			if(!childTaskWouldBeVisible(selected.getType()))
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
			BaseObject selectedObject = getSingleSelectedObject();
			if (! userConfirmsCreateTask(selectedObject))
				return;
			
			createTask(getProject(), selectedObject, getPicker());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: " + e.getMessage());
		}
	}
	
	private boolean userConfirmsCreateTask(BaseObject selectedObject)
	{
		if (selectedObject.getType() != Task.getObjectType())
			return false;
		
		Task task = (Task) selectedObject;
		if (task.getAssignmentRefs().size() == 0)
			return true;
		
		return hasUserConfirmed();
	}
	
	private boolean hasUserConfirmed()
	{
		final String CANCEL = "Cancel";
		final String CREATE = "Create";
		String[] buttons = {CANCEL, CREATE};
		String title = EAM.text("Create Task");
		String[] body = {EAM.text("This task already has resources assigned to it. If a task has subtasks, " +
									"those subtasks will be used for all budget calculations, " +
									"and any resource assignments will be ignored. Are you sure you want to create a subtask?")};
	
		String userChoice = EAM.choiceDialog(title, body, buttons);
		return userChoice.equals(CREATE);
	}

	boolean canOwnTask(BaseObject object)
	{
		if(object.getType() == Task.getObjectType())
			return true;
		
		return false;
	}
	
	private boolean childTaskWouldBeVisible(int parentType) throws Exception
	{
		ViewData viewData = getProject().getViewData(PlanningView.getViewName());
		CodeList visibleRowCodes = RowManager.getVisibleRowCodes(viewData);
		return (visibleRowCodes.contains(Task.getChildTaskTypeCode(parentType)));
	}
	
	public static void createTask(Project project, BaseObject parent, ObjectPicker picker) throws CommandFailedException, ParseException, Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
			project.executeCommand(create);
			BaseId createdId = create.getCreatedId();

			String containerTag = AbstractTreeNodeMoveDoer.getTaskIdsTag(parent);
			CommandSetObjectData addChildCommand = CommandSetObjectData.createAppendIdCommand(parent, containerTag, createdId);
			project.executeCommand(addChildCommand);
			
			ORef createdRef = new ORef(ObjectType.TASK, createdId);
			picker.ensureObjectVisible(createdRef);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
}
