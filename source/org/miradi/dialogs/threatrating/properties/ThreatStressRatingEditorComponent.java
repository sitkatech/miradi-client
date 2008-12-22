/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.threatrating.properties;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.miradi.actions.ActionManageStresses;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.umbrella.ObjectPicker;

public class ThreatStressRatingEditorComponent extends DisposablePanel
{
	public ThreatStressRatingEditorComponent(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		mainWindow = mainWindowToUse;
		objectPicker = objectPickerToUse;
		createTables();
		addTablesWithManageStressesButton();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		threatStressRatingTable.dispose();
	}
	
	protected JPanel createManageStressesComponent(Actions actions)
	{
		OneRowPanel buttonPanel = new OneRowPanel();
		buttonPanel.setGaps(5);
		buttonPanel.setVerticalMargin(20);
		buttonPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());

		ObjectsActionButton manageStressesButton = createObjectsActionButton(actions.getObjectsAction(ActionManageStresses.class), objectPicker);
		buttonPanel.add(manageStressesButton);
		buttonPanel.add(new PanelTitleLabel(EAM.text("(Create, manage, and rate the stresses for this target)")));
		return buttonPanel;
	}
	
	private void createTables() throws Exception
	{
		threatStressRatingTableModel = new ThreatStressRatingTableModel(mainWindow.getProject());
		threatStressRatingTable = new ThreatStressRatingTable(mainWindow, threatStressRatingTableModel);
	}
	
	private void addTablesWithManageStressesButton()
	{
		MiradiScrollPane scroller = new MiradiScrollPane(threatStressRatingTable);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroller, BorderLayout.CENTER);
		
		JPanel manageStressesPanel = createManageStressesComponent(mainWindow.getActions());
		add(manageStressesPanel, BorderLayout.BEFORE_FIRST_LINE);
	}
	
	private ObjectPicker getObjectPicker()
	{
		return objectPicker;
	}

	public void refreshModel()
	{
		ORefList[] selectedHierarchies = getObjectPicker().getSelectedHierarchies();
		if (selectedHierarchies.length > 0)
			setObjectRefs(selectedHierarchies[0].toArray());
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		threatStressRatingTable.stopCellEditing();
		threatStressRatingTableModel.setObjectRefs(hierarchyToSelectedRef);
		threatStressRatingTableModel.fireTableDataChanged();
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		return new ORefList[0];
	}
	
	private ThreatStressRatingTableModel threatStressRatingTableModel;
	private ThreatStressRatingTable threatStressRatingTable;
	private MainWindow mainWindow;
	private ObjectPicker objectPicker;
}
