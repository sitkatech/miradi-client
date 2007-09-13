/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.MultiTableVerticalScrollController;
import org.conservationmeasures.eam.utils.MultipleTableSelectionController;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.conservationmeasures.eam.utils.TableWithHelperMethods;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewAssignmentEditorComponent extends DisposablePanel
{
	public PlanningViewAssignmentEditorComponent(MainWindow mainWindowToUse)
	{
		super(new BorderLayout());
		
		mainWindow = mainWindowToUse;
		createTables();
		addTables();
		addTablesToSelectionController();
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		resourceTable.setObjectRefs(hierarchyToSelectedRef);
		resourceTableModel.fireTableDataChanged();
	}
	
	private void createTables()
	{
		selectionController = new MultipleTableSelectionController();
		verticalController = new MultiTableVerticalScrollController();
		
		resourceTableModel = new PlanningViewResourceTableModel(getProject());
		resourceTable = new PlanningViewResourceTable(resourceTableModel);
		
		workplanTable = new PlanningViewWorkPlanTable();
		budgetTable = new PlanningViewBudgetTable();
	}
	
	private void addTables()
	{
		Box horizontalBox = Box.createHorizontalBox();
		addVerticalScrollableControlledTable(horizontalBox, resourceTable);
		addVerticalScrollableControlledTable(horizontalBox, workplanTable);
		addVerticalScrollableControlledTable(horizontalBox, budgetTable);
		
		add(horizontalBox, BorderLayout.CENTER);
		//TODO planning - put in line begins to avoid scorlling while in dev mode
		add(createButtonBar(), BorderLayout.BEFORE_LINE_BEGINS);
	}

	private void addVerticalScrollableControlledTable(Box horizontalBox, TableWithHelperMethods tableToAdd)
	{
		JScrollPane resourceScroller = new JScrollPane(tableToAdd);
		verticalController.addTable(resourceScroller);
		horizontalBox.add(resourceScroller);
	}

	private void addTablesToSelectionController()
	{
		selectionController.addTable(resourceTable);
		selectionController.addTable(workplanTable);
		selectionController.addTable(budgetTable);
	}
	
	protected JPanel createButtonBar()
	{
		GridLayoutPlus layout = new GridLayoutPlus(0, 1);
		JPanel box = new JPanel(layout);
		
		//ObjectsActionButton addButton = createObjectsActionButton(actions.getObjectsAction(ActionAddAssignment.class), objectPicker);
		//box.add(addButton);
		
		ObjectsActionButton removeButton = createObjectsActionButton(getActions().getObjectsAction(ActionRemoveAssignment.class), resourceTable);
		box.add(removeButton);
		
		return box;
	}
	
	private Project getProject()
	{
		return mainWindow.getProject();
	}
	
	private Actions getActions()
	{
		return mainWindow.getActions();
	}

	private MainWindow mainWindow;
	private MultiTableVerticalScrollController verticalController;
	private MultipleTableSelectionController selectionController;
	
	private PlanningViewResourceTable resourceTable;
	private PlanningViewBudgetTable budgetTable;
	private PlanningViewWorkPlanTable workplanTable;
	
	private PlanningViewResourceTableModel resourceTableModel;
}
