/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.properties;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionManageStresses;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.MultiTablePanel;
import org.conservationmeasures.eam.dialogs.threatstressrating.ThreatStressRatingTableModel;
import org.conservationmeasures.eam.layout.OneRowPanel;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

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
		FastScrollPane resourceScroller = new FastScrollPane(threatStressRatingTable);
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

	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		threatStressRatingTableModel.setObjectRefs(hierarchyToSelectedRef);
		threatStressRatingTableModel.fireTableDataChanged();
	}
	
	public void dataWasChanged()
	{		
		threatStressRatingTable.repaint();
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
