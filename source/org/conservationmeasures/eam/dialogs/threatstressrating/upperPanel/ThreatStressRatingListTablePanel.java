/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.event.ListSelectionEvent;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.MultiTableUpperPanel;
import org.conservationmeasures.eam.dialogs.threatstressrating.properties.ThreatStressRatingPropertiesPanel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class ThreatStressRatingListTablePanel extends MultiTableUpperPanel
{
	public static ThreatStressRatingListTablePanel createThreatStressRatingListTablePanel(Project projectToUse, ThreatStressRatingMultiTablePanel threatStressRatingMultiTablePanel, ThreatStressRatingPropertiesPanel propertiesPanel) throws Exception
	{
		return new ThreatStressRatingListTablePanel(projectToUse, threatStressRatingMultiTablePanel, propertiesPanel);
	}
	
	private ThreatStressRatingListTablePanel(Project projectToUse, ThreatStressRatingMultiTablePanel multiTablePanelToUse, ThreatStressRatingPropertiesPanel propertiesPanelToUse)
	{
		super(projectToUse, multiTablePanelToUse.getObjectPicker());

		multiTablePanel = multiTablePanelToUse;
		propertiesPanel = propertiesPanelToUse;
		
		add(multiTablePanelToUse);
	}
	
	public ObjectPicker getObjectPicker()
	{
		return multiTablePanel;
	}

	public void valueChanged(ListSelectionEvent event)
	{
		super.valueChanged(event);
		ORefList[] selectedHierarcies = multiTablePanel.getSelectedHierarchies();
		propertiesPanel.setObjectRefs(selectedHierarcies);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(FactorLink.getObjectType(), FactorLink.TAG_THREAT_STRESS_RATING_REFS))
			handleExecutedSetDataCommand((CommandSetObjectData) event.getCommand());
		
		repaint();	
	}

	private void handleExecutedSetDataCommand(CommandSetObjectData setCommand)
	{
		ORefList[] selectedHierarcies = multiTablePanel.getSelectedHierarchies();
		propertiesPanel.setObjectRefs(selectedHierarcies);
	}

	public BaseObject getSelectedObject()
	{
		return null;
	}
	
	private ThreatStressRatingPropertiesPanel propertiesPanel;
	private ThreatStressRatingMultiTablePanel multiTablePanel;
}
