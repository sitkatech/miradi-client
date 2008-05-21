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
package org.miradi.dialogs.groupboxLink;

import org.miradi.actions.jump.ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.threatstressrating.properties.ThreatStressRatingPropertiesPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramLink;
import org.miradi.views.umbrella.ObjectPicker;

public class GroupBoxLinkPropertiesPanel extends ObjectDataInputPanel
{
	public GroupBoxLinkPropertiesPanel(MainWindow mainWindow, DiagramLink link, ObjectPicker objectPicker) throws Exception
	{
		super(mainWindow.getProject(), link.getWrappedRef());
		
		ThreatStressRatingPropertiesPanel threatStressRatingPropertiesPanel = new ThreatStressRatingPropertiesPanel(mainWindow, objectPicker);
		addSubPanel(threatStressRatingPropertiesPanel);
		add(threatStressRatingPropertiesPanel);
		
		updateFieldsFromProject();
	}

	@Override
	public void setObjectRef(ORef oref)
	{
		DiagramLink diagramLink = DiagramLink.find(getProject(), oref);
		
		super.setObjectRef(diagramLink.getWrappedRef());
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Link Properties");
	}
	
	public Class getJumpActionClass()
	{
		return ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep.class;
	}
}
