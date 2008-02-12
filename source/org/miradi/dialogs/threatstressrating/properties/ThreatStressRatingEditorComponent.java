/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.properties;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.miradi.actions.ActionManageStresses;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.MultiTablePanel;
import org.miradi.dialogs.threatstressrating.ThreatStressRatingTableModel;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.umbrella.ObjectPicker;

public class ThreatStressRatingEditorComponent extends MultiTablePanel
{
	public ThreatStressRatingEditorComponent(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse.getProject());
		
		mainWindow = mainWindowToUse;
		objectPicker = objectPickerToUse;
		createTables();
		addTablesWithManageStressesButton();
	}
	
	private void createTables() throws Exception
	{
		threatStressRatingTableModel = new ThreatStressRatingTableModel(getProject(), getSelectedThreatStressRatingRef());
		threatStressRatingTable = new ThreatStressRatingTable(threatStressRatingTableModel);
	}
	
	private void addTablesWithManageStressesButton()
	{
		MiradiScrollPane resourceScroller = new MiradiScrollPane(threatStressRatingTable);
		add(createButtonPanel(), BorderLayout.BEFORE_FIRST_LINE);
		add(resourceScroller, BorderLayout.CENTER);
	}
	
	protected JPanel createButtonPanel()
	{
		JPanel buttonPanel = new OneRowPanel();
		ObjectsActionButton manageStressesButton = createObjectsActionButton(getActions().getObjectsAction(ActionManageStresses.class), getObjectPicker());
		buttonPanel.add(manageStressesButton);
		
		return buttonPanel;
	}
	
	private Actions getActions()
	{
		return mainWindow.getActions();
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
	
	private ORef getSelectedThreatStressRatingRef()
	{
		return ORef.INVALID;
	}
	
	private ThreatStressRatingTableModel threatStressRatingTableModel;
	private ThreatStressRatingTable threatStressRatingTable;
	private MainWindow mainWindow;
	private ObjectPicker objectPicker;
}
