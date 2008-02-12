/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.upperPanel;

import java.awt.BorderLayout;

import javax.swing.event.ListSelectionEvent;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.MultiTableUpperPanel;
import org.miradi.dialogs.threatstressrating.properties.ThreatStressRatingPropertiesPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Stress;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;
import org.miradi.views.umbrella.ObjectPicker;

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

		// NOTE: Replace scroll pane that super automatically added
		add(multiTablePanelToUse, BorderLayout.CENTER);
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
		if (event.isSetDataCommand())
			handleExecutedSetDataCommand((CommandSetObjectData) event.getCommand());
		
		repaint();	
	}

	private void handleExecutedSetDataCommand(CommandSetObjectData setCommand )
	{
		if (setCommand.getObjectType() != ThreatStressRating.getObjectType() && setCommand.getObjectType() != Stress.getObjectType())
			return;
		
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
