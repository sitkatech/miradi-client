/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.Icon;

import org.conservationmeasures.eam.objects.EAMObject;

abstract public class ObjectPoolManagementPanel extends ModelessDialogPanel
{
	public ObjectPoolManagementPanel(ObjectPoolTablePanel tablePanelToUse, ObjectDataInputPanel propertiesPanelToUse)
	{
		poolComponent = tablePanelToUse;
		add(poolComponent, BorderLayout.CENTER);
		
		propertiesPanel = propertiesPanelToUse;
		poolComponent.setPropertiesPanel(propertiesPanel);
		add(propertiesPanel, BorderLayout.AFTER_LAST_LINE);

	}
	
	abstract public String getPanelDescription();
	abstract public Icon getIcon();
	
	public void dispose()
	{
		poolComponent.dispose();
		poolComponent = null;
		
		propertiesPanel.dispose();
		propertiesPanel = null;
		
		super.dispose();
	}


	public EAMObject getObject()
	{
		return poolComponent.getSelectedObject();
	}

	
	ObjectPoolTablePanel poolComponent;
	ObjectDataInputPanel propertiesPanel;
}
