/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.tablerenderers.RowBaseObjectProvider;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

abstract public class EditableObjectTableModel extends AbstractTableModel implements ColumnTagProvider, RowBaseObjectProvider
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
		
	abstract public void setObjectRefs(ORef[] hierarchyToSelectedRef);
	
	private Project project;
}
