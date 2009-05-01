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
package org.miradi.dialogs.assignment;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAssignmentEditorComponent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.views.umbrella.ObjectPicker;

public class AssignmentsPropertiesPanel extends ObjectDataInputPanel
{
	public AssignmentsPropertiesPanel(MainWindow mainWindowToUse, int objectType, ObjectPicker picker) throws Exception
	{
		super(mainWindowToUse.getProject(), objectType);
		
		assignmentEditor = new PlanningViewAssignmentEditorComponent(mainWindowToUse, picker);
		add(assignmentEditor);
		updateFieldsFromProject();
	}

	public void dispose()
	{
		assignmentEditor.dispose();
		assignmentEditor = null;

		super.dispose();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		assignmentEditor.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		assignmentEditor.becomeInactive();
		super.becomeInactive();
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		super.setObjectRefs(hierarchyToSelectedRef);
		assignmentEditor.setObjectRefs(hierarchyToSelectedRef);
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Assignments");
	}
	
	private PlanningViewAssignmentEditorComponent assignmentEditor;
}
