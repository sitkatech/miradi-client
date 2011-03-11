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
package org.miradi.views.umbrella.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.ResourceAssignment;
import org.miradi.utils.CommandVector;
import org.miradi.views.ObjectsDoer;

abstract public class DeletePoolObjectDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		BaseObject singleSelectedObject = getSingleSelectedObject();
		if (singleSelectedObject == null)
			return false;
		
		return canDelete(singleSelectedObject);
	}

	@Override
	protected void doIt() throws Exception
	{
		if(!isAvailable())
			return;
		
		String[] buttons = {EAM.text("Yes"), EAM.text("No"), };
		final String title = EAM.substitute(EAM.text("Delete %s"), getCustomText());
		final String text = EAM.substitute(EAM.text("\nAre you sure you want to delete the selected %s?"), getCustomText());
		if(!EAM.confirmDialog(title, new String[]{text}, buttons))
			return;

		try
		{
			getProject().executeCommand(new CommandBeginTransaction());
			try
			{
				BaseObject objectToDelete = getObjects()[0];
				doWork(objectToDelete);
				CommandVector commandsToDeleteChildrenAndObject = objectToDelete.createCommandsToDeleteChildrenAndObject();
				getProject().executeCommandsWithoutTransaction(commandsToDeleteChildrenAndObject);
			}
			finally
			{
				getProject().executeCommand(new CommandEndTransaction());
			}
		}
		catch(CommandFailedException e)
		{
			throw(e);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	protected void removeAssignmentReferenceToObject(BaseObject objectToDelete, String referringTag) throws Exception
	{
		removeResourceAssignmentReferenceToObject(objectToDelete, referringTag);
		removeExpenseAssignmentReferenceToObject(objectToDelete, referringTag);
	}
	
	protected void removeResourceAssignmentReferenceToObject(BaseObject objectToDelete, String referringTag) throws Exception
	{
		removeAssignmentReferenceToObject(objectToDelete, ResourceAssignment.getObjectType(), referringTag);
	}
	
	protected void removeExpenseAssignmentReferenceToObject(BaseObject objectToDelete, String referringTag) throws Exception
	{
		removeAssignmentReferenceToObject(objectToDelete, ExpenseAssignment.getObjectType(), referringTag);
	}

	private void removeAssignmentReferenceToObject(BaseObject objectToDelete, int objectType, String referringTag) throws Exception
	{
		ORefList expenseAssignmentReferrerRefs = objectToDelete.findObjectsThatReferToUs(objectType);
		for (int index = 0; index < expenseAssignmentReferrerRefs.size(); ++index)
		{
			ORef assignmentRef = expenseAssignmentReferrerRefs.get(index);
			Assignment assignment = Assignment.findAssignment(getProject(), assignmentRef);
			if (!assignment.doesFieldExist(referringTag))
				EAM.unexpectedErrorDialog(new Exception());
			
			CommandSetObjectData clearTag = new CommandSetObjectData(assignmentRef, referringTag, "");
			getProject().executeCommand(clearTag);
		}
	}
	
	protected void doWork(BaseObject objectToDelete) throws Exception
	{
	}

	abstract protected String getCustomText();	
	abstract protected boolean canDelete(BaseObject singleSelectedObject);
}
