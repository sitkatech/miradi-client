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

package org.miradi.dialogs.progressReport;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.miradi.actions.ActionDeleteProgressReport;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.DataInputPanel;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.base.ProgressReportTableModel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProgressReport;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.umbrella.ActionCreateProgressReport;
import org.miradi.views.umbrella.ObjectPicker;

public class ProgressReportSubPanel extends ObjectDataInputPanel
{
	public ProgressReportSubPanel(MainWindow mainWindow, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindow.getProject(), ORef.createInvalidWithType(ProgressReport.getObjectType()));
		
		setLayout(new OneColumnGridLayout());
		
		objectPicker = objectPickerToUse;
		createTable();
		addComponents();
		updateFieldsFromProject();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		progressReportTable.dispose();
	}
	
	private void createTable() throws Exception
	{
		progressReportTableModel = new ProgressReportTableModel(getMainWindow().getProject());
		progressReportTable = new ProgressReportTable(getMainWindow(), progressReportTableModel);
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		
		progressReportTable.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		progressReportTable.becomeInactive();

		super.becomeInactive();
	}
	
	private void addComponents()
	{
		DataInputPanel tablesPanel = new DataInputPanel(getProject());
		MiradiScrollPane scroller = new MiradiScrollPane(progressReportTable);
		tablesPanel.add(scroller);
		add(createButtonBar(), BorderLayout.PAGE_START);
		add(tablesPanel, BorderLayout.CENTER);
	}
	
	protected JPanel createButtonBar()
	{
		OneRowPanel box = new OneRowPanel();
		box.setBackground(AppPreferences.getDataPanelBackgroundColor());
		box.setGaps(3);
		box.add(createObjectsActionButton(getActions().getObjectsAction(ActionCreateProgressReport.class), objectPicker));
		box.add(createObjectsActionButton(getActions().getObjectsAction(ActionDeleteProgressReport.class), progressReportTable));
		
		return box;
	}
	
	public void refreshModel()
	{
		ORefList[] selectedHierarchies = getPicker().getSelectedHierarchies();
		if (selectedHierarchies.length > 0)
			setObjectRefs(selectedHierarchies[0].toArray());
	}
	
	@Override
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		progressReportTable.stopCellEditing();
		progressReportTableModel.setObjectRefs(hierarchyToSelectedRef);
		fireTableDataChanged();
	}
	
	public void fireTableDataChanged()
	{
		progressReportTableModel.fireTableDataChanged();
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		return new ORefList[0];
	}
	
	private Actions getActions()
	{
		return getMainWindow().getActions();
	}
		
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Progress Report");
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if (event.isSetDataCommandWithThisType(ProgressReport.getObjectType()))
			progressReportTableModel.fireTableDataChanged();
		
		if (event.isDeleteCommandForThisType(ProgressReport.getObjectType()) || event.isCreateCommandForThisType(ProgressReport.getObjectType())) 
			refreshModel();
	}
		
	@Override
	protected boolean doesSectionContainFieldWithTag(String tagToUse)
	{
		if (tagToUse.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return true;
		
		return super.doesSectionContainFieldWithTag(tagToUse);
	}
	
	private ProgressReportTableModel progressReportTableModel;
	private ProgressReportTable progressReportTable;
	private ObjectPicker objectPicker;
}
