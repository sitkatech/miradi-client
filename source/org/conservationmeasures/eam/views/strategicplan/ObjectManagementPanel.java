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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.UiTableWithAlternatingRows;
import org.conservationmeasures.eam.views.umbrella.ObjectManagerTableModel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTable;

public class ObjectManagementPanel extends JPanel implements CommandExecutedListener, ListSelectionListener
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
		table.setMaxColumnWidthToHeaderWidth(0);
		table.resizeTable();

		add(new UiScrollPane(table), BorderLayout.CENTER);
		add(createButtonPanel(getMainWindow().getActions(), buttonActionClasses), BorderLayout.AFTER_LAST_LINE);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(this);
		
		getProject().addCommandExecutedListener(this);

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

	public void valueChanged(ListSelectionEvent event)
	{
		try
		{
			int row = table.getSelectedRow();
			if(row >= 0)
				view.objectWasSelected(model.getObjectFromRow(row));
			getMainWindow().getActions().updateActionStates();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	Component createButtonPanel(Actions actions, Class[] buttonActionClasses)
	{
		Box buttonBox = Box.createHorizontalBox();
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
	


	UmbrellaView view;
	ObjectManagerTableModel model;
	UiTable table;
}
