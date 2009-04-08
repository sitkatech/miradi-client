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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.dialogs.diagram.ShareSelectionDialog;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;


abstract public class AbstractShareDoer extends AbstractTreeNodeCreateTaskDoer
{
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		appendSelectedObjectAsShared();
	}
	
	private void appendSelectedObjectAsShared() throws CommandFailedException
	{
		ORef parentOfSharedRef = getParentRefOfShareableObjects();
		if (parentOfSharedRef.isInvalid())
			return;
	
		ObjectPoolTablePanel shareableObjectPoolTablePanel = createShareableObjectPoolTablePanel(parentOfSharedRef);
		try
		{
			ShareSelectionDialog listDialog = new ShareSelectionDialog(getMainWindow(), getShareDialogTitle(), shareableObjectPoolTablePanel);
			listDialog.setVisible(true); 
			
			BaseObject objectToShare = listDialog.getSelectedObject();
			if (objectToShare == null)
				return;
			
			BaseObject parentOfShared = getProject().findObject(parentOfSharedRef);
			CommandSetObjectData appendSharedObjectCommand = CommandSetObjectData.createAppendIdCommand(parentOfShared, getParentTaskIdsTag(), objectToShare.getId());
			getProject().executeCommand(appendSharedObjectCommand);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			shareableObjectPoolTablePanel.dispose();
		}
	}
	
	protected ORef getParentRefOfShareableObjects()
	{
		BaseObject foundObject = getSingleSelected(getParentType());
		if (foundObject == null)
			return ORef.INVALID;
		
		return foundObject.getRef();
	}
	
	@Override
	protected boolean canOwnTask(BaseObject selectedObject) throws Exception
	{
		if(getParentType() == selectedObject.getType())
			return true;
		
		if (Task.is(selectedObject))
			return hasAdjacentParentInSelectionHierarchy((Task) selectedObject);
		
		return false;
	}

	abstract protected String getShareDialogTitle();
	
	abstract protected ObjectPoolTablePanel createShareableObjectPoolTablePanel(ORef parentOfSharedObjectRefs);
	
	abstract protected int getParentType();
	
	abstract protected String getParentTaskIdsTag();
}
