/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.indicator;

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.viability.FactorPropertiesViabilityTreeManagementPanel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class SimpleViabilityPanel extends ObjectDataInputPanel
{
	public SimpleViabilityPanel(MainWindow mainWindowToUse, ORef factorRef) throws Exception
	{
		super(mainWindowToUse.getProject(), factorRef);
		
		setLayout(new OneColumnGridLayout());
		
		managementPanel = new FactorPropertiesViabilityTreeManagementPanel(mainWindowToUse, factorRef, mainWindowToUse.getActions());
		simpleViabilityPropertiesPanel = new SimpleViabilityPropertiesPanel(mainWindowToUse.getProject(), factorRef);
		addSubPanelWithTitledBorder(simpleViabilityPropertiesPanel);
		add(managementPanel);
	}

	public void updateSplitterLocation()
	{
		managementPanel.updateSplitterLocation();
	}

	public void dispose()
	{
		managementPanel.dispose();
		simpleViabilityPropertiesPanel.dispose();
	}
	
	public String getPanelDescription()
	{
		return managementPanel.getPanelDescription();
	}

	public Icon getIcon()
	{
		return managementPanel.getIcon();
	}
	
	private FactorPropertiesViabilityTreeManagementPanel managementPanel;
	private SimpleViabilityPropertiesPanel simpleViabilityPropertiesPanel;
}
