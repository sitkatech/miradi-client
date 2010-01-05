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
import org.miradi.dialogs.base.EditableObjectTableSubPanel;
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

public class ProgressReportSubPanel extends EditableObjectTableSubPanel
{
	public ProgressReportSubPanel(MainWindow mainWindow, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindow.getProject(), objectPickerToUse, ProgressReport.getObjectType());
		
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
		MiradiScrollPane scroller = new MiradiScrollPane(progressReportTable);
		add(createButtonBar(), BorderLayout.PAGE_START);
		add(scroller, BorderLayout.CENTER);
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
		ORefList[] selectedHierarchies = objectPicker.getSelectedHierarchies();
		if (selectedHierarchies.length > 0)
			setObjectRefs(selectedHierarchies[0].toArray());
	}
	
	@Override
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		progressReportTable.stopCellEditing();
		progressReportTableModel.setObjectRefs(hierarchyToSelectedRef);
		progressReportTableModel.fireTableDataChanged();
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
		
		if (event.isSetDataCommandWithThisTag(BaseObject.TAG_PROGRESS_REPORT_REFS))
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
