/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.utils.SplitterPositionSaver;

abstract public class ObjectManagementPanel extends VerticalSplitPanel
{
	public ObjectManagementPanel(SplitterPositionSaver splitPositionSaverToUse, ObjectTablePanel tablePanelToUse, ObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(splitPositionSaverToUse, tablePanelToUse, propertiesPanelToUse);
		listComponent = tablePanelToUse;
		
		propertiesPanel = propertiesPanelToUse;
		listComponent.setPropertiesPanel(propertiesPanel);
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
		if(listComponent == null)
			return null;
		return listComponent.getSelectedObject();
	}

	ObjectTablePanel listComponent;
	ObjectDataInputPanel propertiesPanel;

}
