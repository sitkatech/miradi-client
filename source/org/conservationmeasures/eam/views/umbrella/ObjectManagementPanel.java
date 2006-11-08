/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.MouseAdapterDoubleClickDelegator;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.conservationmeasures.eam.utils.UiTableWithAlternatingRows;
import org.conservationmeasures.eam.views.strategicplan.ObjectPicker;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTable;

public class ObjectManagementPanel extends DisposablePanel implements CommandExecutedListener, ListSelectionListener, ObjectPicker
{
	public ObjectManagementPanel(UmbrellaView viewToUse, String[] columnTags, EAMObjectPool pool, Class[] buttonActionClasses)
	{
		this(viewToUse, new ObjectManagerTableModel(pool, columnTags), buttonActionClasses);
	}
	
	public ObjectManagementPanel(UmbrellaView viewToUse, ObjectManagerTableModel modelToUse, Class[] buttonActionClasses)
	{
		super(new BorderLayout());
		view = viewToUse;
		model = modelToUse;
		
		table = new UiTableWithAlternatingRows(model);
		
		// TODO: set all column widths to "reasonable" values
		// for now, a cheap hack: make the first column small,
		// which happens to work for all our current tables
		table.useMaxWidth();
		table.createDefaultColumnsFromModel();
		table.resizeTable();
		table.getTableHeader().setReorderingAllowed(true);
		table.getTableHeader().addMouseListener(new ObjectManagmentPanelSortListener());
		
		add(new UiScrollPane(table), BorderLayout.CENTER);
		add(createButtonPanel(getMainWindow().getActions(), buttonActionClasses), BorderLayout.AFTER_LAST_LINE);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(this);
		
		getProject().addCommandExecutedListener(this);

	}
	
	public void dispose()
	{
		getProject().removeCommandExecutedListener(this);
		super.dispose();
	}

	public MainWindow getMainWindow()
	{
		return view.getMainWindow();
	}
	
	public Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	public UiTable getTable()
	{
		return table;
	}
	
	public void addButtons(UiButton[] buttons)
	{
		for(int i = 0; i < buttons.length; ++i)
			buttonBox.add(buttons[i]);
	}

	public void addDoubleClickAction(Class doubleClickAction) 
	{
			EAMAction action = getMainWindow().getActions().get(doubleClickAction);
			table.addMouseListener(new MouseAdapterDoubleClickDelegator(action));
	}
	
	public EAMObject[] getSelectedObjects()
	{
		int[] rows = table.getSelectedRows();
		EAMObject[] objects = new EAMObject[rows.length];
		for(int i = 0; i < objects.length; ++i)
			objects[i] = model.getObjectFromRow(rows[i]);
		return objects;
	}
	
	public void clearSelection()
	{
		table.clearSelection();
	}

	public void addListSelectionListener(ListSelectionListener listener)
	{
		table.getSelectionModel().addListSelectionListener(listener);
	}

	
	public void commandExecuted(CommandExecutedEvent event)
	{
		reactToChanges();
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		reactToChanges();
	}

	private void reactToChanges()
	{
		int wasSelected = table.getSelectedRow();
		model.fireTableDataChanged();
		if(wasSelected >= 0 && wasSelected < table.getRowCount())
			table.setRowSelectionInterval(wasSelected, wasSelected);
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}

	public void valueChanged(ListSelectionEvent event)
	{
		try
		{
			int row = table.getSelectedRow();
			BaseId selectedId = BaseId.INVALID;
			if(row >= 0)
			{
				EAMObject selectedObject = model.getObjectFromRow(row);
				selectedId = selectedObject.getId();
				view.objectWasSelected(selectedObject);
			}
			objectWasSelected(selectedId);
			getMainWindow().getActions().updateActionStates();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void objectWasSelected(BaseId newId)
	{
		
	}
	
	Component createButtonPanel(Actions actions, Class[] buttonActionClasses)
	{
		buttonBox = Box.createHorizontalBox();
		for(int i = 0; i < buttonActionClasses.length; ++i)
			buttonBox.add(new UiButton(actions.get(buttonActionClasses[i])));
		
		return buttonBox;
	}
	
	public void selectObject(EAMObject objectToSelect)
	{
		for(int row = 0; row < model.getRowCount(); ++row)
		{
			if(model.getObjectFromRow(row).equals(objectToSelect))
			{
				table.clearSelection();
				table.addRowSelectionInterval(row, row);
				return;
			}
		}
	}

	protected ObjectsActionButton createObjectsActionButton(Class actionClass)
	{
		return new ObjectsActionButton(getMainWindow().getActions().getObjectsAction(actionClass), this);
	}

	protected void setMaxColumnWidthToHeaderWidth(int column)
	{
		table.setMaxColumnWidthToHeaderWidth(column);
	}

	protected void setColumnVeryWide(int column)
	{
		table.setColumnWidth(column, view.getWidth() / 2);
	}
	
	protected int getSelectedRow()
	{
		return table.getSelectedRow();
	}

	private UmbrellaView view;
	private ObjectManagerTableModel model;
	private UiTableWithAlternatingRows table;
	private Box buttonBox;
}
