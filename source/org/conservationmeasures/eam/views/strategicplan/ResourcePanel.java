/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.annotations.ResourcePool;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.ObjectPool;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTable;
import org.martus.swing.UiTableModel;

public class ResourcePanel extends JPanel implements CommandExecutedListener, ListSelectionListener
{
	public ResourcePanel(MainWindow mainWindowToUse)
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		
		String[] columnTags = {"Initials", "Name", "Position", };
		ObjectPool pool = getProject().getResourcePool();
		model = new ObjectManagerTableModel(pool, columnTags);
		table = new UiTable(model);
		add(new UiScrollPane(table), BorderLayout.CENTER);
		add(createButtonPanel(mainWindow.getActions()), BorderLayout.AFTER_LAST_LINE);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(this);
		
		getProject().addCommandExecutedListener(this);
	}
	
	public Project getProject()
	{
		return mainWindow.getProject();
	}
	
	public UiTable getTable()
	{
		return table;
	}
	
	public ProjectResource getSelectedResource()
	{
		int row = table.getSelectedRow();
		if(row < 0)
			return null;
		
		ResourcePool pool = getProject().getResourcePool();
		int resourceId = pool.getIds()[row];
		ProjectResource resource = pool.find(resourceId);
		return resource;
	}
	
	static Component createButtonPanel(Actions actions)
	{
		Box buttonBox = Box.createHorizontalBox();
		UiButton addButton = new UiButton(actions.get(ActionCreateResource.class));
		UiButton editButton = new UiButton(actions.get(ActionModifyResource.class));
		UiButton deleteButton = new UiButton(actions.get(ActionDeleteResource.class));
		buttonBox.add(addButton);
		buttonBox.add(editButton);
		buttonBox.add(deleteButton);
		
		return buttonBox;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		model.fireTableStructureChanged();
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		model.fireTableStructureChanged();
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}

	public void valueChanged(ListSelectionEvent e)
	{
		mainWindow.getActions().updateActionStates();
	}

	static class ObjectManagerTableModel extends UiTableModel
	{
		public ObjectManagerTableModel(ObjectPool resourcePool, String[] columnTagsToUse)
		{
			pool = resourcePool;
			columnTags = columnTagsToUse;
		}
		
		public boolean isEnabled(int row)
		{
			return false;
		}

		public int getColumnCount()
		{
			return columnTags.length;
		}

		public int getRowCount()
		{
			return pool.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex)
		{
			int objectId = pool.getIds()[rowIndex];
			EAMObject object = (EAMObject)pool.getRawObject(objectId);
			return object.getData(columnTags[columnIndex]);
		}

		public String getColumnName(int column)
		{
			return columnTags[column];
		}
		
		ObjectPool pool;
		String[] columnTags;
	}
	
	MainWindow mainWindow;
	ObjectManagerTableModel model;
	UiTable table;
}
