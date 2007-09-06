/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.MultiTableVerticalScrollController;
import org.conservationmeasures.eam.utils.MultipleTableSelectionController;
import org.conservationmeasures.eam.utils.TableWithHelperMethods;

public class PlanningViewAssignmentEditorComponent extends DisposablePanel
{
	public PlanningViewAssignmentEditorComponent(Project projectToUse)
	{
		super(new BorderLayout());
		
		project = projectToUse;
		createTables();
		addTables();
		addTablesToSelectionController();
	}

	private void createTables()
	{
		selectionController = new MultipleTableSelectionController();
		verticalController = new MultiTableVerticalScrollController();
		
		resourceTable = new PlanningViewResourceTable(project);
		workplanTable = new PlanningViewWorkPlanTable();
		budgetTable = new PlanningViewBudgetTable();
	}
	
	private void addTables()
	{
		Box horizontalBox = Box.createHorizontalBox();
		addVerticalScrollableControlledTable(horizontalBox, resourceTable);
		addVerticalScrollableControlledTable(horizontalBox, workplanTable);
		addVerticalScrollableControlledTable(horizontalBox, budgetTable);
		
		add(horizontalBox);
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

	private Project project;
	private MultiTableVerticalScrollController verticalController;
	private MultipleTableSelectionController selectionController;
	
	private PlanningViewResourceTable resourceTable;
	private PlanningViewBudgetTable budgetTable;
	private PlanningViewWorkPlanTable workplanTable;
}
