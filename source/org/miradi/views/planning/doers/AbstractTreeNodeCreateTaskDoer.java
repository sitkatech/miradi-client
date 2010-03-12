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

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.ObjectPicker;


abstract public class AbstractTreeNodeCreateTaskDoer extends AbstractTreeNodeTaskDoer
{
	@Override
	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			ORefList selecionHiearchy = getSelectionHierarchy();
			BaseObject parent = extractParentFromSelectionHiearchy(selecionHiearchy);
			createTask(parent);
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	private void createTask(BaseObject parentOfTask) throws CommandFailedException, ParseException, Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
			getProject().executeCommand(create);
			
			ORef newTaskRef = create.getObjectRef();
			String containerTag = Task.getTaskIdsTag(parentOfTask);
			CommandSetObjectData addChildCommand = CommandSetObjectData.createAppendIdCommand(parentOfTask, containerTag, newTaskRef.getObjectId());
			getProject().executeCommand(addChildCommand);
			
			selectObjectAfterSwingClearsItDueToCreateTask(getPicker(), newTaskRef);		
		}
		finally
		{
			getProject().executeEndTransaction();
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
		
		private ObjectPicker picker;
		private ORef ref;
	}
}
