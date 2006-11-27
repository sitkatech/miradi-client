/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.objects.EAMObject;
import org.martus.swing.UiScrollPane;

abstract public class ObjectManagementPanel extends ModelessDialogPanel
{
	public ObjectManagementPanel(ObjectTablePanel tablePanelToUse, ObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		listComponent = tablePanelToUse;
		add(listComponent, BorderLayout.CENTER);
		
		propertiesPanel = propertiesPanelToUse;
		listComponent.setPropertiesPanel(propertiesPanel);
		add(new UiScrollPane(propertiesPanel), BorderLayout.AFTER_LAST_LINE);
	}
	
	public void dispose()
	{
		listComponent.dispose();
		listComponent = null;
		
		propertiesPanel.dispose();
		propertiesPanel = null;
		
		super.dispose();
	}

	public void addTablePanelButton(ObjectsAction action)
	{
		listComponent.addButton(action);
	}

	public EAMObject getObject()
	{
		return listComponent.getSelectedObject();
	}

	ObjectTablePanel listComponent;
	ObjectDataInputPanel propertiesPanel;

}
