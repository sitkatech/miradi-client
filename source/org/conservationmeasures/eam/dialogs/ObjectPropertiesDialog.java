/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.JDialog;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DialogGridPanel;
import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;

abstract public class ObjectPropertiesDialog extends JDialog
{
	ObjectPropertiesDialog(MainWindow parentToUse, EAMObject objectToEdit)
	{
		super(parentToUse);
		object = objectToEdit;
		
		setModal(false);
	}
	
	public EAMObject getObject()
	{
		return object;
	}
	
	void initializeFields(String[] tags)
	{
		DialogGridPanel grid = new DialogGridPanel();
		fields = new DialogField[tags.length];
		for(int field = 0; field < fields.length; ++field)
		{
			fields[field] = createDialogField(tags[field]);
			fields[field].getComponent().addFocusListener(new FocusHandler());
			grid.add(new UiLabel(fields[field].getLabel()));
			grid.add(fields[field].getComponent());
		}

		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());
		contents.add(grid, BorderLayout.CENTER);
		Utilities.centerDlg(this);
		pack();
	}

	DialogField createDialogField(String tag)
	{
		String label = EAM.text("Label|" + tag);
		String value = object.getData(tag);
		DialogField dialogField = new StringDialogField(tag, label, value);
		return dialogField;
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		Command rawCommand = event.getCommand();
		if(rawCommand.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
			closeIfObjectDeleted((CommandDeleteObject)rawCommand);
	}

	public void commandUndone(CommandExecutedEvent event)
	{
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}
	
	private void closeIfObjectDeleted(CommandDeleteObject cmd)
	{
		if(cmd.getObjectType() == object.getType() && cmd.getObjectId() == object.getId())
			dispose();
	}

	private Project getProject()
	{
		return ((MainWindow)getParent()).getProject();
	}

	class FocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
		}

		public void focusLost(FocusEvent e)
		{
			applyChanges();
		}
	}
	
	public void applyChanges()
	{
		try
		{
			Vector commands = new Vector();
			for(int field = 0; field < fields.length; ++field)
			{
				String tag = fields[field].getTag();
				String oldText = object.getData(tag);
				String newText = fields[field].getText();
				if(!oldText.equals(newText))
					commands.add(new CommandSetObjectData(object.getType(), object.getId(), tag, newText));
			}

			if(commands.size() > 0)
			{
				getProject().executeCommand(new CommandBeginTransaction());
				for(int i = 0; i < commands.size(); ++i)
					getProject().executeCommand((Command)commands.get(i));
				getProject().executeCommand(new CommandEndTransaction());
			}
		}
		catch (CommandFailedException e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error prevented this operation");
		}
	}

	EAMObject object;
	DialogField[] fields;
}

