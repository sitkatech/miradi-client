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

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.ObjectPool;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTable;

public class ObjectManagementPanel extends JPanel implements CommandExecutedListener, ListSelectionListener
{
	public ObjectManagementPanel(MainWindow mainWindowToUse, String[] columnTags, ObjectPool pool, Class[] buttonActionClasses)
	{
		this(mainWindowToUse, new ObjectManagerTableModel(pool, columnTags), buttonActionClasses);
	}
	
	public ObjectManagementPanel(MainWindow mainWindowToUse, ObjectManagerTableModel modelToUse, Class[] buttonActionClasses)
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		model = modelToUse;
		
		table = new UiTable(model);
		
		// TODO: set all column widths to "reasonable" values
		// for now, a cheap hack: make the first column small,
		// which happens to work for all our current tables
		table.useMaxWidth();
		table.createDefaultColumnsFromModel();
		table.setMaxColumnWidthToHeaderWidth(0);
		table.resizeTable();

		add(new UiScrollPane(table), BorderLayout.CENTER);
		add(createButtonPanel(mainWindow.getActions(), buttonActionClasses), BorderLayout.AFTER_LAST_LINE);
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
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		model.fireTableDataChanged();
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		model.fireTableDataChanged();
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}

	public void valueChanged(ListSelectionEvent e)
	{
		mainWindow.getActions().updateActionStates();
	}
	
	Component createButtonPanel(Actions actions, Class[] buttonActionClasses)
	{
		Box buttonBox = Box.createHorizontalBox();
		for(int i = 0; i < buttonActionClasses.length; ++i)
			buttonBox.add(new UiButton(actions.get(buttonActionClasses[i])));
		
		return buttonBox;
	}
	


	MainWindow mainWindow;
	ObjectManagerTableModel model;
	UiTable table;
}
