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
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.martus.swing.UiScrollPane;
import org.miradi.actions.ActionAssignResource;
import org.miradi.actions.ActionRemoveAssignment;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.MultiTablePanel;
import org.miradi.dialogs.treetables.MultiTreeTablePanel.ScrollPaneWithHideableScrollBar;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Task;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.umbrella.ObjectPicker;

public class PlanningViewAssignmentEditorComponent extends MultiTablePanel
{
	public PlanningViewAssignmentEditorComponent(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse);
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		
		mainWindow = mainWindowToUse;
		objectPicker = objectPickerToUse;
		createTables();
		addTables();
		addTablesToSelectionController();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		resourceTable.dispose();
		workplanTable.dispose();
		budgetTable.dispose();
		budgetTotalsTable.dispose();	
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		savePendingEdits();
		
		if (hierarchyToSelectedRef.length == 0)
		{
			setTaskId(ORef.createInvalidWithType(Task.getObjectType()));
		}
		else
		{
			ORefList selectionHierarchyRefs = new ORefList(hierarchyToSelectedRef[0]);
			ORef taskRef = selectionHierarchyRefs.getRefForType(Task.getObjectType());
			setTaskId(taskRef);
		}

		resourceTableModel.setObjectRefs(hierarchyToSelectedRef);
		workPlanModel.setObjectRefs(hierarchyToSelectedRef);
		budgetModel.setObjectRefs(hierarchyToSelectedRef);
		budgetTotalsModel.setObjectRefs(hierarchyToSelectedRef);
		
		resourceTableModel.fireTableDataChanged();
		workPlanModel.fireTableDataChanged();
		budgetModel.fireTableDataChanged();
		budgetTotalsModel.fireTableDataChanged();
	}
	
	private void savePendingEdits()
	{
		resourceTable.stopCellEditing();
		workplanTable.stopCellEditing();
		budgetTable.stopCellEditing();
		budgetTotalsTable.stopCellEditing();
	}

	private void createTables() throws Exception
	{
		resourceTableModel = new PlanningViewResourceTableModel(getProject());
		resourceTable = new PlanningViewResourceTable(mainWindow, resourceTableModel);
		
		workPlanModel = new PlanningViewWorkPlanTableModel(getProject());
		workplanTable = new PlanningViewWorkPlanTable(mainWindow, workPlanModel);
		
		budgetModel = new PlanningViewBudgetTableModel(getProject());
		budgetTable = new PlanningViewBudgetTable(mainWindow, budgetModel);
		
		budgetTotalsModel = new PlanningViewBudgetTotalsTableModel(getProject());
		budgetTotalsTable = new PlanningViewBudgetTotalsTable(mainWindow, budgetTotalsModel);
	}
	
	private void addTables()
	{
		OneRowPanel tables = new OneRowPanel();

		addTableToPanel(tables, resourceTable);
		addToHorizontalController(addTableToPanel(tables, workplanTable));
		addToHorizontalController(addTableToPanel(tables, budgetTable));
		addTableToPanel(tables, budgetTotalsTable);
		
		add(tables, BorderLayout.CENTER);
		add(createButtonBar(), BorderLayout.BEFORE_FIRST_LINE);
	}
	
	static class AssignmentsComponentTableScrollPane extends ScrollPaneWithHideableScrollBar
	{
		public AssignmentsComponentTableScrollPane(AssignmentsComponentTable contents)
		{
			super(contents);
			setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
			setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
			widthSetter = new PersistentWidthSetterComponent(contents.getMainWindow(), this, contents.getUniqueTableIdentifier(), getPreferredSize().width);
		}
		
		public PersistentWidthSetterComponent getWidthSetterComponent()
		{
			return widthSetter;
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			final Dimension size = super.getPreferredSize();
			if(widthSetter != null)
				size.width = widthSetter.getControlledWidth();
			return size;
		}
		
		@Override
		public Dimension getSize()
		{
			final Dimension size = super.getSize();
			if(widthSetter != null)
				size.width = widthSetter.getControlledWidth();
			return size;
		}
		
		private PersistentWidthSetterComponent widthSetter;
	}

	private UiScrollPane addTableToPanel(OneRowPanel tables, AssignmentsComponentTable table)
	{
		addRowHeightControlledTable(table);
		AssignmentsComponentTableScrollPane scroller = new AssignmentsComponentTableScrollPane(table);
		addToVerticalController(scroller);
		tables.add(scroller);
		tables.add(scroller.getWidthSetterComponent());
		return scroller;
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
	
	public void dataWasChanged() throws Exception
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
	
	private void setTaskId(ORef taskRef)
	{ 
		Task task = Task.find(getProject(), taskRef);
		
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
	
	private ObjectPicker objectPicker;
}
