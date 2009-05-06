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
package org.miradi.dialogs.task;

import org.miradi.dialogs.activity.ActivityFactorVisibilityControlPanel;
import org.miradi.dialogs.assignment.AssignmentsPropertiesPanel;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.diagram.ForecastSubPanel;
import org.miradi.dialogs.expense.ExpensesPropertiesPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.ObjectPicker;

public abstract class TaskPropertiesPanel extends ObjectDataInputPanelWithSections
{
	protected TaskPropertiesPanel(MainWindow mainWindow, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindow.getProject(), Task.getObjectType());
		
		addSubPanelWithTitledBorder(new TaskDetailsPanel(getProject(), mainWindow.getActions()));

		if(shouldHaveVisibilityPanel())
			addSubPanelWithTitledBorder(new ActivityFactorVisibilityControlPanel(mainWindow));
		
		addSubPanelWithTitledBorder(new ForecastSubPanel(mainWindow, new ORef(Task.getObjectType(), BaseId.INVALID)));		
		addSubPanelWithTitledBorder(new AssignmentsPropertiesPanel(mainWindow, Task.getObjectType(), objectPickerToUse));
		addSubPanelWithTitledBorder(new ExpensesPropertiesPanel(getMainWindow(), Task.getObjectType(), objectPickerToUse));
		
		updateFieldsFromProject();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
			
		if (event.isSetDataCommandWithThisTypeAndTag(Target.getObjectType(), Target.TAG_VIABILITY_MODE))
			reloadSelectedRefs();
	}
	
	abstract protected boolean shouldHaveVisibilityPanel();
}
