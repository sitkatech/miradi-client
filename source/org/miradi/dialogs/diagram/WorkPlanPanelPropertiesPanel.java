/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.FillerLabel;

public class WorkPlanPanelPropertiesPanel extends ObjectDataInputPanel
{
	public WorkPlanPanelPropertiesPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		addTopAlignedLabel(new PanelTitleLabel(EAM.text("Dates can be set in the Work Plan view")));
		addTopAlignedLabel(new FillerLabel());
		
		addFieldWithDescriptionOnly(createWhenEditorField(orefToUse), EAM.text("When"));
		addTopAlignedLabel(new FillerLabel());
		addFieldWithDescriptionOnly(createWhoEditorField(orefToUse), EAM.text("Who"));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Work Plan");
	}
}
