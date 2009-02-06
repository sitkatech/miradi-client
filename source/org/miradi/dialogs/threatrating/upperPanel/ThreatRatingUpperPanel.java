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
package org.miradi.dialogs.threatrating.upperPanel;

import java.awt.BorderLayout;

import javax.swing.event.ListSelectionEvent;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.MultiTableUpperPanel;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Stress;
import org.miradi.objects.ThreatStressRating;
import org.miradi.views.umbrella.ObjectPicker;

public class ThreatRatingUpperPanel extends MultiTableUpperPanel
{
	public static ThreatRatingUpperPanel createThreatStressRatingListTablePanel(MainWindow mainWindowToUse, ThreatRatingMultiTablePanel threatStressRatingMultiTablePanel, ObjectDataInputPanel propertiesPanel) throws Exception
	{
		return new ThreatRatingUpperPanel(mainWindowToUse, threatStressRatingMultiTablePanel, propertiesPanel);
	}
	
	private ThreatRatingUpperPanel(MainWindow mainWindowToUse, ThreatRatingMultiTablePanel multiTablePanelToUse, ObjectDataInputPanel propertiesPanelToUse)
	{
		super(mainWindowToUse, multiTablePanelToUse.getObjectPicker());

		multiTablePanel = multiTablePanelToUse;
		propertiesPanel = propertiesPanelToUse;

		// NOTE: Replace scroll pane that super automatically added
		add(multiTablePanelToUse, BorderLayout.CENTER);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		multiTablePanel.dispose();
	}
	
	public ThreatRatingMultiTablePanel getMultiTablePanel()
	{
		return multiTablePanel;
	}
	
	public ObjectPicker getObjectPicker()
	{
		return getMultiTablePanel();
	}

	public void valueChanged(ListSelectionEvent event)
	{
		super.valueChanged(event);
		ORefList[] selectedHierarcies = multiTablePanel.getSelectedHierarchies();
		propertiesPanel.setObjectRefs(selectedHierarcies[0]);
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
		propertiesPanel.setObjectRefs(selectedHierarcies[0]);
	}

	public BaseObject getSelectedObject()
	{
		return null;
	}
	
	private ObjectDataInputPanel propertiesPanel;
	private ThreatRatingMultiTablePanel multiTablePanel;
}
