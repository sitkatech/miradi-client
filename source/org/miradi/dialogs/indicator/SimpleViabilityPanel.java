/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.indicator;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import org.miradi.actions.jump.ActionJumpTargetViabilityMethodChoiceStep;
import org.miradi.dialogs.viability.FactorPropertiesViabilityTreeManagementPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class SimpleViabilityPanel extends FactorPropertiesViabilityTreeManagementPanel
{
	public SimpleViabilityPanel(MainWindow mainWindowToUse, ORef factorRef) throws Exception
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
