/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogs.threatrating.properties;

import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.schemas.FactorLinkSchema;
import org.miradi.schemas.ThreatSimpleRatingDataSchema;
import org.miradi.views.umbrella.ObjectPicker;

public class SimpleThreatRatingPropertiesPanel extends AbstractThreatRatingPropertiesPanel
{
	public SimpleThreatRatingPropertiesPanel(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), FactorLinkSchema.getObjectType(), ThreatSimpleRatingDataSchema.getObjectType());
		
		setLayout(new OneColumnGridLayout());
		
		final Project project = mainWindowToUse.getProject();
		factorNamesPanel = new LinkPropertiesFactorsSubPanel(project, mainWindowToUse.getActions());
		dropdownsPanel = new SimpleThreatRatingDropdownsPanel(project);
		
		addSubPanelField(factorNamesPanel);
		addSubPanelField(dropdownsPanel);
		addSubPanelWithoutTitledBorder(new ThreatRatingDataRollupSubPanel(project, mainWindowToUse.getActions()));
		addSubPanelWithoutTitledBorder(new ThreatRatingDataEvidenceSubPanel(project, mainWindowToUse.getActions()));
		
		updateFieldsFromProject();
	}

	@Override
	protected void setEditorEnabled(boolean isEditable)
	{
		dropdownsPanel.setEnabled(isEditable);
	}

	@Override
	protected void setEditorObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		factorNamesPanel.setObjectRefs(hierarchyToSelectedRef);
		dropdownsPanel.setObjectRefs(hierarchyToSelectedRef);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Simple Threat Rating");
	}

	private LinkPropertiesFactorsSubPanel factorNamesPanel;
	private SimpleThreatRatingDropdownsPanel dropdownsPanel;
}
