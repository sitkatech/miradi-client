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

import javax.swing.SwingUtilities;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.utils.ConstantButtonNames;
import org.miradi.views.umbrella.ObjectPicker;

public class TreeNodeCreateTaskDoer extends AbstractTreeNodeCreateTaskDoer
{
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
	
	//TODO refactor this method
	private boolean userConfirmsCreateTask(BaseObject selectedObject)
	{
		if (selectedObject.getType() == Indicator.getObjectType())
			return true;
		
		if (selectedObject.getType() == Strategy.getObjectType())
			return true;
		
		if (selectedObject.getType() != Task.getObjectType())
			return false;
		
		Task task = (Task) selectedObject;
		if (task.getAssignmentRefs().size() == 0)
			return true;
		
		String[] buttons = {ConstantButtonNames.CANCEL, ConstantButtonNames.CREATE};
		String title = EAM.text("Create Task");
		String[] body = {EAM.text("This activity/method/task already has resources assigned to it. If it also " +
								  "contains tasks/subtasks, those tasks will be rolled up into the overall activity, " +
								  "leading to possible double counting. Are you sure you want to proceed?")};
		
		String userChoice = EAM.choiceDialog(title, body, buttons);
		return userChoice.equals(ConstantButtonNames.CREATE);
	}
	
	public void createTask(Project project, BaseObject parent, ObjectPicker picker) throws CommandFailedException, ParseException, Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
			project.executeCommand(create);
			BaseId createdId = create.getCreatedId();

			String containerTag = Task.getTaskIdsTag(parent);
			CommandSetObjectData addChildCommand = CommandSetObjectData.createAppendIdCommand(parent, containerTag, createdId);
			project.executeCommand(addChildCommand);
			
			ORef createdRef = new ORef(ObjectType.TASK, createdId);
			selectObjectAfterSwingClearsItDueToCreateTask(picker, createdRef);		
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
	
	//TODO this shoul be done more cleanly inside the Planning view Tree table
	private void selectObjectAfterSwingClearsItDueToCreateTask(ObjectPicker picker, ORef selectedRef)
	{
		SwingUtilities.invokeLater(new Reselecter(picker, selectedRef));
	}
	
	class Reselecter implements Runnable
	{
		public Reselecter(ObjectPicker pickerToUse, ORef refToSelect)
		{
			picker = pickerToUse;
			ref = refToSelect;
		}
		
		public void run()
		{
			picker.ensureObjectVisible(ref);
		}
		
		ObjectPicker picker;
		ORef ref;
	}
}
