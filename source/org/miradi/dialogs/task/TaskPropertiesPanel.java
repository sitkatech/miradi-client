/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import javax.swing.BorderFactory;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.BaseId;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;

public class TaskPropertiesPanel extends ObjectDataInputPanel
{
	public TaskPropertiesPanel(MainWindow mainWindow) throws Exception
	{
		this(mainWindow, BaseId.INVALID);
	}
	
	public TaskPropertiesPanel(MainWindow mainWindow, BaseId idToEdit) throws Exception
	{
		super(mainWindow.getProject(), ObjectType.TASK, idToEdit);
		setLayout(new OneColumnGridLayout());
		setBorder(BorderFactory.createEtchedBorder());
		inputPanel = new TaskPropertiesInputPanel(mainWindow, idToEdit);
		
		String hintAboutPlanningView = EAM.text("<html><em>HINT: " +
				"To manage the details about who will do the work and when, " +
				"go to the Planning View and choose Work Plan</em>");
		
		add(inputPanel);
		add(new PanelTitleLabel(hintAboutPlanningView));
	}
	
	public void dispose()
	{
		if(inputPanel != null)
			inputPanel.dispose();
		
		super.dispose();
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		if (orefsToUse.length==0)
			inputPanel.setObjectRef(new ORef(ObjectType.FAKE,BaseId.INVALID));
		else
			inputPanel.setObjectRef(orefsToUse[0]);
	}
	
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Task Properties");
	}
		
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		inputPanel.commandExecuted(event);
	}

	TaskPropertiesInputPanel inputPanel;	
}
