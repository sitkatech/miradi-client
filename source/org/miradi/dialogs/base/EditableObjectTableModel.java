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
package org.miradi.dialogs.base;

import javax.swing.table.AbstractTableModel;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.ColumnTagProvider;

abstract public class EditableObjectTableModel extends AbstractTableModel implements ColumnTagProvider, RowColumnBaseObjectProvider
{
	public EditableObjectTableModel(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	protected ObjectManager getObjectManager()
	{
		return getProject().getObjectManager();
	}
	
	public void setValueUsingCommand(ORef refToUse, String fieldTag, BaseId idToSave)
	{
		setValueUsingCommand(refToUse, fieldTag, idToSave.toString());
	}
	
	public void setValueUsingCommand(ORef refToUse, String fieldTag, ChoiceItem choiceToSave)
	{
		setValueUsingCommand(refToUse, fieldTag, choiceToSave.getCode());
	}
	
	public void setValueUsingCommand(ORef  refToUse, String fieldTag, String valueToSave)
	{
		try
		{
			Command command = new CommandSetObjectData(refToUse, fieldTag, valueToSave);
			getProject().executeCommand(command);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
	}
	
	public int getProportionShares(int row)
	{
		return 1;
	}
	
	public boolean areBudgetValuesAllocated(int row)
	{
		return false;
	}
	
	abstract public void setObjectRefs(ORef[] hierarchyToSelectedRef);
	
	private Project project;
}
