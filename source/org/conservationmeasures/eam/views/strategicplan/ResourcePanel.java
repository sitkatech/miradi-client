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
import org.conservationmeasures.eam.objects.ProjectResource;
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
		model = new ResourceTableModel(getProject().getResourcePool());
		table = new UiTable(model);
		add(new UiScrollPane(table), BorderLayout.CENTER);
		add(createButtonPanel(this, mainWindow.getActions()), BorderLayout.AFTER_LAST_LINE);
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
	
	static Component createButtonPanel(ResourcePanel owner, Actions actions)
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

	static class ResourceTableModel extends UiTableModel
	{
		public ResourceTableModel(ResourcePool resourcePool)
		{
			resources = resourcePool;
		}
		
		public boolean isEnabled(int row)
		{
			return false;
		}

		public int getColumnCount()
		{
			return 3;
		}

		public int getRowCount()
		{
			return resources.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex)
		{
			int resourceId = resources.getIds()[rowIndex];
			ProjectResource resource = resources.find(resourceId);
			switch(columnIndex)
			{
				case 0: return resource.getLabel();
				default: return "";
			}
		}

		public String getColumnName(int column)
		{
			switch(column)
			{
				case 0: return "Name";
				case 1: return "Description";
				case 2: return "Rate";
				default: throw new RuntimeException("Unknown column " + column);
			}
		}
		
		ResourcePool resources;
	}

	MainWindow mainWindow;
	ResourceTableModel model;
	UiTable table;
}
