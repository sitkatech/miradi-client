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
package org.miradi.dialogs.diagram;

import org.miradi.actions.jump.ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.threatstressrating.properties.ThreatStressRatingPropertiesPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.views.umbrella.ObjectPicker;

public class FactorLinkPropertiesPanel extends ObjectDataInputPanel
{
	public FactorLinkPropertiesPanel(Project projectToUse, DiagramLink link)
	{
		super(projectToUse, ObjectType.FACTOR_LINK, link.getWrappedId());

		addField(createCheckBoxField(FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));
		
		updateFieldsFromProject();
	}

	public FactorLinkPropertiesPanel(MainWindow mainWindow, DiagramLink link, ObjectPicker objectPicker) throws Exception
	{
		super(mainWindow.getProject(), ObjectType.FACTOR_LINK, link.getWrappedId());
		
		ThreatStressRatingPropertiesPanel threatStressRatingPropertiesPanel = new ThreatStressRatingPropertiesPanel(mainWindow, objectPicker);
		addSubPanel(threatStressRatingPropertiesPanel);
		add(threatStressRatingPropertiesPanel);
		
		setObjectRefs(objectPicker.getSelectedHierarchies()[0]);
		updateFieldsFromProject();
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
