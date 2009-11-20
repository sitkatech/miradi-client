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

package org.miradi.dialogs.progressReport;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProgressReport;
import org.miradi.views.umbrella.ObjectPicker;

public class ProgressReportSubPanel extends ObjectDataInputPanel
{
	public ProgressReportSubPanel(MainWindow mainWindow, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindow.getProject(), ORef.createInvalidWithType(ProgressReport.getObjectType()));
		
		setLayout(new OneColumnGridLayout());
		editorComponent = new ProgressReportEditorComponent(mainWindow, objectPickerToUse);
		add(editorComponent);
		updateFieldsFromProject();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		editorComponent.dispose();
		editorComponent = null;
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Progress Report");
	}

	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		editorComponent.refreshModel();
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if (event.isSetDataCommandWithThisType(ProgressReport.getObjectType()))
			editorComponent.fireTableDataChanged();
		
		if (event.isDeleteCommandForThisType(ProgressReport.getObjectType()) || event.isCreateCommandForThisType(ProgressReport.getObjectType())) 
			editorComponent.refreshModel();
	}
	
	private ProgressReportEditorComponent editorComponent;
}
