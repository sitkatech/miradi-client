/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.actions.ActionAddAssignment;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.MultiTableHorizontalScrollController;
import org.conservationmeasures.eam.utils.MultiTableVerticalScrollController;
import org.conservationmeasures.eam.utils.MultipleTableSelectionController;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewAssignmentEditorComponent extends DisposablePanel
{
	public PlanningViewAssignmentEditorComponent(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(new BorderLayout());
		
		mainWindow = mainWindowToUse;
		objectPicker = objectPickerToUse;
		createTables();
		addTables();
		addTablesToSelectionController();
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		if (hierarchyToSelectedRef.length == 0)
			setTaskId(BaseId.INVALID);
		else
			setTaskId(hierarchyToSelectedRef[0].getObjectId());

		resourceTableModel.setObjectRefs(hierarchyToSelectedRef);
		workPlanModel.setObjectRefs(hierarchyToSelectedRef);
		budgetModel.setObjectRefs(hierarchyToSelectedRef);
		budgetTotalsModel.setObjectRefs(hierarchyToSelectedRef);
		
		resourceTableModel.fireTableDataChanged();
		workPlanModel.fireTableDataChanged();
		budgetModel.fireTableDataChanged();
		budgetTotalsModel.fireTableDataChanged();
	}
	
	private void createTables() throws Exception
	{
		selectionController = new MultipleTableSelectionController();
		verticalController = new MultiTableVerticalScrollController();
		horizontalController = new MultiTableHorizontalScrollController();
		
		resourceTableModel = new PlanningViewResourceTableModel(getProject());
		resourceTable = new PlanningViewResourceTable(resourceTableModel);
		
		workPlanModel = new PlanningViewWorkPlanTableModel(getProject());
		workplanTable = new PlanningViewWorkPlanTable(getProject(), workPlanModel);
		
		budgetModel = new PlanningViewBudgetTableModel(getProject());
		budgetTable = new PlanningViewBudgetTable(budgetModel);
		
		budgetTotalsModel = new PlanningViewBudgetTotalsTableModel(getProject());
		budgetTotalsTable = new PlanningViewBudgetTotalsTable(budgetTotalsModel);
	}
	
	private void addTables()
	{
		Box horizontalBox = Box.createHorizontalBox();
		JScrollPane resourceScroller = createScrollPane(resourceTable);
		resourceScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		resourceScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		addVerticalScrollableControlledTable(horizontalBox, resourceScroller);
		
		JScrollPane workPlanScroller = createScrollPane(workplanTable);
		workPlanScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		addVerticalScrollableControlledTable(horizontalBox, workPlanScroller);
		
		JScrollPane budgetScroller = createScrollPane(budgetTable);
		budgetScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		addVerticalScrollableControlledTable(horizontalBox, budgetScroller);
		
		JScrollPane budgetTotalsScroller = createScrollPane(budgetTotalsTable);
		budgetTotalsScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		addVerticalScrollableControlledTable(horizontalBox, budgetTotalsScroller);
		
		add(horizontalBox, BorderLayout.CENTER);
		add(createButtonBar(), BorderLayout.BEFORE_FIRST_LINE);
	}

	private JScrollPane createScrollPane(PlanningViewAbstractTable table)
	{
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setBackground(table.getBackground());
		return scrollPane;
	}

	private void addVerticalScrollableControlledTable(Box horizontalBox, JScrollPane scroller)
	{
		verticalController.addTable(scroller);
		horizontalController.addTable(scroller);
		horizontalBox.add(scroller);
	}

	private void addTablesToSelectionController()
	{
		selectionController.addTable(resourceTable);
		selectionController.addTable(workplanTable);
		selectionController.addTable(budgetTable);
		selectionController.addTable(budgetTotalsTable);
	}
	
	protected JPanel createButtonBar()
	{
		GridLayoutPlus layout = new GridLayoutPlus(1, 0);
		JPanel box = new JPanel(layout);
				
		ObjectsActionButton addButton = createObjectsActionButton(getActions().getObjectsAction(ActionAddAssignment.class), objectPicker);
		box.add(addButton);
		
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
	
	public void dataWasChanged()
	{
		resourceTableModel.dataWasChanged();
		workPlanModel.dataWasChanged();
		budgetModel.dataWasChanged();
		budgetTotalsModel.dataWasChanged();
		
		resourceTable.repaint();
		workplanTable.repaint();
		budgetTable.repaint();
		budgetTotalsTable.repaint();
	}
	
	private void setTaskId(BaseId taskId)
	{ 
		Task task = (Task)getProject().findObject(ObjectType.TASK, taskId);
		resourceTableModel.setTask(task);
		workPlanModel.setTask(task);
		budgetModel.setTask(task);
		budgetTotalsModel.setTask(task);
	}

	private MainWindow mainWindow;
	private MultiTableVerticalScrollController verticalController;
	private MultiTableHorizontalScrollController horizontalController;
	private MultipleTableSelectionController selectionController;
	
	private PlanningViewResourceTable resourceTable;
	private PlanningViewWorkPlanTable workplanTable;
	private PlanningViewBudgetTable budgetTable;
	private PlanningViewBudgetTotalsTable budgetTotalsTable;
	
	private PlanningViewResourceTableModel resourceTableModel;
	private PlanningViewAbstractBudgetTableModel workPlanModel;
	private PlanningViewBudgetTableModel budgetModel;
	private PlanningViewBudgetTotalsTableModel budgetTotalsModel;
	private ObjectPicker objectPicker;
}
