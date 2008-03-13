/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.properties;

import java.awt.BorderLayout;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.threatstressrating.ThreatStressRatingTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.MiradiScrollPane;
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
	
	private void createTables() throws Exception
	{
		threatStressRatingTableModel = new ThreatStressRatingTableModel(mainWindow.getProject());
		threatStressRatingTable = new ThreatStressRatingTable(threatStressRatingTableModel);
	}
	
	private void addTablesWithManageStressesButton()
	{
		MiradiScrollPane resourceScroller = new MiradiScrollPane(threatStressRatingTable);
		add(resourceScroller, BorderLayout.CENTER);
	}
	
	private ObjectPicker getObjectPicker()
	{
		return objectPicker;
	}

	public void updateModelBasedOnPickerList()
	{
		setObjectRefs(getObjectPicker().getSelectedHierarchies()[0].toArray());
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
