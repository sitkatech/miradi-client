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
package org.miradi.dialogs.threatrating.properties;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.FactorLink;
import org.miradi.views.umbrella.ObjectPicker;

public class SimpleThreatRatingPropertiesPanel extends ObjectDataInputPanel
{
	public SimpleThreatRatingPropertiesPanel(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), FactorLink.getObjectType(), BaseId.INVALID);
		setLayout(new OneColumnGridLayout());
		
		commonPanel = new ThreatRatingCommonPropertiesSubpanel(mainWindowToUse.getProject(), mainWindowToUse.getActions());
		dropdownsPanel = new SimpleThreatRatingDropdownsPanel(mainWindowToUse.getProject());
		
		add(commonPanel);
		add(dropdownsPanel);
		
		updateFieldsFromProject();
	}
	
	@Override
	public void dispose()
	{
		commonPanel.dispose();
		commonPanel = null;
		dropdownsPanel.dispose();
		dropdownsPanel = null;
		super.dispose();
	}

	@Override
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		super.setObjectRefs(hierarchyToSelectedRef);
		
		commonPanel.setObjectRefs(hierarchyToSelectedRef);
		dropdownsPanel.setObjectRefs(hierarchyToSelectedRef);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Simple Threat Rating");
	}

	private ThreatRatingCommonPropertiesSubpanel commonPanel;
	private SimpleThreatRatingDropdownsPanel dropdownsPanel;
}
