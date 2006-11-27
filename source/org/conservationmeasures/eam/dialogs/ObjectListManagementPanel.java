package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiScrollPane;

abstract public class ObjectListManagementPanel extends ModelessDialogPanel
{
	public ObjectListManagementPanel(Project projectToUse, FactorId nodeId, Actions actions,
			ObjectListTablePanel tablePanelToUse, ObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(new BorderLayout());
		
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


	public EAMObject getObject()
	{
		return listComponent.getSelectedObject();
	}

	ObjectListTablePanel listComponent;
	ObjectDataInputPanel propertiesPanel;
}
