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
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Task;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.umbrella.ObjectPicker;

public class PlanningViewAssignmentEditorComponent extends MultiTablePanel implements CommandExecutedListener
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
		
		getProject().addCommandExecutedListener(this);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		becomeInactive();
		getProject().removeCommandExecutedListener(this);
		
		resourceTable.dispose();
		workplanTable.dispose();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		resourceTable.becomeActive();
		objectPicker.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		resourceTable.becomeInactive();
		objectPicker.becomeInactive();
		super.becomeInactive();
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
		
		resourceTableModel.fireTableDataChanged();
		workPlanModel.fireTableDataChanged();
	}
	
	private void savePendingEdits()
	{
		resourceTable.stopCellEditing();
		workplanTable.stopCellEditing();
	}

	private void createTables() throws Exception
	{
		resourceTableModel = new PlanningViewResourceTableModel(getProject());
		resourceTable = new PlanningViewResourceTable(mainWindow, resourceTableModel);
		
		workPlanModel = new PlanningViewWorkPlanTableModel(getProject(), resourceTableModel);
		workplanTable = new PlanningViewWorkPlanTable(mainWindow, workPlanModel);		
	}
	
	private void addTables()
	{
		OneRowPanel tables = new OneRowPanel();

		addTableToPanel(tables, resourceTable);
		addToHorizontalController(addTableToPanel(tables, workplanTable));
		
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
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (event.isSetDataCommand())
				dataWasChanged();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("An unexpected error has occurred"));
		}		
	}

	private void dataWasChanged() throws Exception
	{
		resourceTableModel.dataWasChanged();
		
		resourceTable.rebuildColumnEditorsAndRenderers();
		resourceTable.repaint();
		workplanTable.repaint();
	}
	
	private void setTaskId(ORef taskRef)
	{ 
		Task task = Task.find(getProject(), taskRef);
		
		//FIXME need to this for all the tables.  not doing it now becuase resourcetable.stopCellEditing
		//throws command exec inside commandExected exceptions.  also these tables need to be inside a container
		//that way we just loop through the tbales.  
		workplanTable.stopCellEditing();
		
		resourceTableModel.setTask(task);
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		return resourceTable.getSelectedHierarchies();
	}
	
	private MainWindow mainWindow;
	
	private PlanningViewResourceTable resourceTable;
	private PlanningViewWorkPlanTable workplanTable;
	
	private PlanningViewResourceTableModel resourceTableModel;
	private PlanningViewWorkPlanTableModel workPlanModel;
	
	private ObjectPicker objectPicker;
}
