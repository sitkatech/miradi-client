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

import javax.swing.JScrollPane;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ProgressReportTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.umbrella.ObjectPicker;

public class ProgressReportEditorComponent extends DisposablePanel
{
	public ProgressReportEditorComponent(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		mainWindow = mainWindowToUse;
		objectPicker = objectPickerToUse;
		createTables();
		addTables();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		progressReportTable.dispose();
	}
	
	private void createTables() throws Exception
	{
		progressReportTableModel = new ProgressReportTableModel(mainWindow.getProject());
		progressReportTable = new ProgressReportTable(mainWindow, progressReportTableModel);
	}
	
	private void addTables()
	{
		MiradiScrollPane scroller = new MiradiScrollPane(progressReportTable);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroller, BorderLayout.CENTER);
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
	
	private void setObjectRefs(ORef[] hierarchyToSelectedRef)
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
	
	private ProgressReportTableModel progressReportTableModel;
	private ProgressReportTable progressReportTable;
	private MainWindow mainWindow;
	private ObjectPicker objectPicker;
}
