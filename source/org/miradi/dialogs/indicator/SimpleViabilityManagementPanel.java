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
package org.miradi.dialogs.indicator;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import org.miradi.actions.jump.ActionJumpTargetViabilityMethodChoiceStep;
import org.miradi.dialogs.viability.FactorPropertiesViabilityTreeManagementPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class SimpleViabilityManagementPanel extends FactorPropertiesViabilityTreeManagementPanel
{
	public SimpleViabilityManagementPanel(MainWindow mainWindowToUse, ORef factorRef) throws Exception
	{
		super(mainWindowToUse, factorRef, mainWindowToUse.getActions());
		simpleViabilityPropertiesPanel = new SimpleViabilityFieldsPanel(mainWindowToUse.getProject(), factorRef);
		Border border = BorderFactory.createTitledBorder(simpleViabilityPropertiesPanel.getPanelDescription());
		simpleViabilityPropertiesPanel.setBorder(border);
		getListPanel().addAboveTable(simpleViabilityPropertiesPanel);
	}

	public void dispose()
	{
		super.dispose();
		simpleViabilityPropertiesPanel.dispose();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Viability");
	}

	@Override
	public Class getJumpActionClass()
	{
		return ActionJumpTargetViabilityMethodChoiceStep.class;
	}
	
	private SimpleViabilityFieldsPanel simpleViabilityPropertiesPanel;
}
