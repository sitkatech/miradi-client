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
package org.miradi.dialogs.viability;

import org.miradi.actions.jump.ActionJumpTargetViability3Step;
import org.miradi.icons.KeyEcologicalAttributeIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class TargetPropertiesKeaViabilityTreeManagementPanel extends
		TargetViabilityTreeManagementPanel
{
	public TargetPropertiesKeaViabilityTreeManagementPanel(MainWindow mainWindow, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef factorRef) throws Exception
	{
		super(mainWindow, splitPositionSaverToUse, factorRef);
		panelDescription = PANEL_DESCRIPTION_VIABILITY;
		icon = new KeyEcologicalAttributeIcon();
	}
	
	public String getSplitterDescription()
	{
		return PANEL_DESCRIPTION_VIABILITY + SPLITTER_TAG;
	}

	public Class getJumpActionClass()
	{
		return ActionJumpTargetViability3Step.class;
	}
	
	private static String PANEL_DESCRIPTION_VIABILITY = EAM.text("Tab|Viability"); 

}
