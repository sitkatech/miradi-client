/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.miradi.actions.ActionAssignResource;
import org.miradi.actions.ActionRemoveAssignment;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.MultiTablePanel;
import org.miradi.ids.BaseId;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Task;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.umbrella.ObjectPicker;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;
import org.miradi.views.umbrella.PersistentSplitPane;

public class PlanningViewAssignmentEditorComponent extends MultiTablePanel
{
	public PlanningViewAssignmentEditorComponent(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse.getProject());
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		
		mainWindow = mainWindowToUse;
		objectPicker = objectPickerToUse;
		createTables();
		addTables();
		addTablesToSelectionController();
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		savePendingEdits();
		
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
		
		restoreSplitters();
	}
	
	private void savePendingEdits()
	{
		resourceTable.stopCellEditing();
		workplanTable.stopCellEditing();
		budgetTable.stopCellEditing();
		budgetTotalsTable.stopCellEditing();
	}

	public void restoreSplitters()
	{
		if(areAllSplittersAtDefaultLocation())
			restoreSplittersToGoodDefaults();
		else
			restoreSplittersToSavedLocations();
	}

	private void restoreSplittersToSavedLocations()
	{
		resourceOtherSplitter.restoreSavedLocation();
		numbersSplitter.restoreSavedLocation();
		detailsSplitter.restoreSavedLocation();
	}

	private void restoreSplittersToGoodDefaults()
	{
		detailsSplitter.resetToPreferredSizes();
		numbersSplitter.resetToPreferredSizes();
		resourceOtherSplitter.resetToPreferredSizes();
	}

	private boolean areAllSplittersAtDefaultLocation()
	{
		if(!resourceOtherSplitter.isSavedLocationDefault())
			return false;
		if(!numbersSplitter.isSavedLocationDefault())
			return false;
		if(!detailsSplitter.isSavedLocationDefault())
			return false;
		
		return true;
	}
	
	private void createTables() throws Exception
	{
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
		addRowHeightControlledTable(resourceTable);
		addRowHeightControlledTable(workplanTable);
		addRowHeightControlledTable(budgetTable);
		addRowHeightControlledTable(budgetTotalsTable);

		JScrollPane resourceScroller = new ScrollPaneWithInvisibleVerticalScrollBar(resourceTable);
		addToVerticalController(resourceScroller);

		JScrollPane workPlanScroller = new ScrollPaneWithInvisibleVerticalScrollBar(workplanTable);
		addToHorizontalController(workPlanScroller);
		addToVerticalController(workPlanScroller);

		JScrollPane budgetScroller = new ScrollPaneWithInvisibleVerticalScrollBar(budgetTable);
		addToHorizontalController(budgetScroller);
		addToVerticalController(budgetScroller);

		JScrollPane budgetTotalsScroller = new ScrollPaneWithInvisibleVerticalScrollBar(budgetTotalsTable);
		addToVerticalController(budgetTotalsScroller);

		resourceOtherSplitter = new PersistentHorizontalSplitPane(this, mainWindow, getSplitterName("Assignment"));
		numbersSplitter = new PersistentHorizontalSplitPane(resourceOtherSplitter, mainWindow, getSplitterName("Numbers"));
		detailsSplitter = new PersistentHorizontalSplitPane(numbersSplitter, mainWindow, getSplitterName("Details"));

		detailsSplitter.setLeftComponent(workPlanScroller);
		detailsSplitter.setRightComponent(budgetScroller);
		
		numbersSplitter.setLeftComponent(detailsSplitter);
		numbersSplitter.setRightComponent(budgetTotalsScroller);
		
		resourceOtherSplitter.setLeftComponent(resourceScroller);
		resourceOtherSplitter.setRightComponent(numbersSplitter);
		
		add(resourceOtherSplitter, BorderLayout.CENTER);
		add(createButtonBar(), BorderLayout.BEFORE_FIRST_LINE);

	}
	
	private String getSplitterName(String subName)
	{
		return "PlanningViewAssignmentTable.Splitter." + subName;
	}
			
	protected void addTablesToSelectionController()
	{
		selectionController.addTable(resourceTable);
		selectionController.addTable(workplanTable);
		selectionController.addTable(budgetTable);
		selectionController.addTable(budgetTotalsTable);
	}
	
	protected JPanel createButtonBar()
	{
		OneRowPanel box = new OneRowPanel();
		box.setBackground(AppPreferences.getDataPanelBackgroundColor());
		box.setGaps(3);
		ObjectsActionButton addButton = createObjectsActionButton(getActions().getObjectsAction(ActionAssignResource.class), objectPicker);
		box.add(addButton);
		
		ObjectsActionButton removeButton = createObjectsActionButton(getActions().getObjectsAction(ActionRemoveAssignment.class), resourceTable);
		box.add(removeButton);
		
		return box;
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
		
		resourceTable.rebuildColumnEditorsAndRenderers();
		resourceTable.repaint();
		workplanTable.repaint();
		budgetTable.repaint();
		budgetTotalsTable.repaint();
	}
	
	private void setTaskId(BaseId taskId)
	{ 
		Task task = (Task)getProject().findObject(ObjectType.TASK, taskId);
		
		//FIXME need to this for all the tables.  not doing it now becuase resourcetable.stopCellEditing
		//throws command exec inside commandExected exceptions.  also these tables need to be inside a container
		//that way we just loop through the tbales.  
		workplanTable.stopCellEditing();
		
		resourceTableModel.setTask(task);
		workPlanModel.setTask(task);
		budgetModel.setTask(task);
		budgetTotalsModel.setTask(task);
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		return objectPicker.getSelectedHierarchies();
	}
	
	private MainWindow mainWindow;
	
	private PlanningViewResourceTable resourceTable;
	private PlanningViewWorkPlanTable workplanTable;
	private PlanningViewBudgetTable budgetTable;
	private PlanningViewBudgetTotalsTable budgetTotalsTable;
	
	private PlanningViewResourceTableModel resourceTableModel;
	private PlanningViewAbstractBudgetTableModel workPlanModel;
	private PlanningViewBudgetTableModel budgetModel;
	private PlanningViewBudgetTotalsTableModel budgetTotalsModel;
	
	private PersistentSplitPane resourceOtherSplitter;
	private PersistentSplitPane numbersSplitter;
	private PersistentSplitPane detailsSplitter;

	private ObjectPicker objectPicker;
}
