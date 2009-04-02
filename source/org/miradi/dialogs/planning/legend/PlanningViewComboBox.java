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
package org.miradi.dialogs.planning.legend;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.main.EAM;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.UiComboBoxWithSaneActionFiring;

abstract public class PlanningViewComboBox extends UiComboBoxWithSaneActionFiring implements RowColumnProvider
{
	public PlanningViewComboBox(Project projectToUse, ChoiceItem[] choices) throws Exception
	{
		super(choices);
		
		project = projectToUse;
		addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event)
	{		
		try
		{
			saveState();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: " + e.getMessage());
		}
	}

	private void saveState() throws Exception
	{	
		Vector commands = new Vector();
		commands.addAll(getComboSaveCommnds());
		if (commands.size() == 0)
			return;
		
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			getProject().executeCommandsWithoutTransaction((Command[])commands.toArray(new Command[0]));
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	private Vector getComboSaveCommnds() throws Exception
	{
		if (! comboBoxNeedsSave())
			return new Vector();
		
		ChoiceItem selectedItem = (ChoiceItem) getSelectedItem();
		String newValue = selectedItem.getCode();
		Vector comboSaveCommands = new Vector();
		ViewData viewData = getProject().getCurrentViewData();

		comboSaveCommands.add(new CommandSetObjectData(viewData.getRef(), getChoiceTag(), newValue));
		return comboSaveCommands;
	}
	
	protected Project getProject()
	{
		return project;
	}

	abstract public String getStyleChoiceName();
	abstract public String getChoiceTag();
	abstract boolean comboBoxNeedsSave() throws Exception;
	
	private Project project;
}
