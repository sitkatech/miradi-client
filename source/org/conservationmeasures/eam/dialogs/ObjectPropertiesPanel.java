/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.JPanel;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogfields.ChoiceDialogField;
import org.conservationmeasures.eam.dialogfields.DialogField;
import org.conservationmeasures.eam.dialogfields.MessageField;
import org.conservationmeasures.eam.dialogfields.MultiSelectDialogField;
import org.conservationmeasures.eam.dialogfields.StringDialogField;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.RatingQuestion;
import org.conservationmeasures.eam.utils.DialogGridPanel;
import org.martus.swing.UiLabel;

abstract public class ObjectPropertiesPanel extends ModelessDialogPanel
{
	
	public ObjectPropertiesPanel(MainWindow parentToUse, EAMObject objectToEdit)
	{
		mainWindowToUse = parentToUse;
		object = objectToEdit;
	}
	
	
	public EAMObject getObject()
	{
		return object;
	}
	
	void initializeFields(String[] tags) throws Exception
	{
		DialogGridPanel grid = new DialogGridPanel();
		fields = new DialogField[tags.length];
		for(int field = 0; field < fields.length; ++field)
		{
			String tag = tags[field];
			String existingValue = object.getData(tag);
			fields[field] = createDialogField(tag, existingValue);
			fields[field].getComponent().addFocusListener(new FocusHandler());
			grid.add(new UiLabel(fields[field].getLabel()));
			grid.add(createFieldPanel(fields[field].getComponent()));
		}

		//Container contents = getContentPane();
		this.setLayout(new BorderLayout());
		this.add(grid, BorderLayout.CENTER);
		//Utilities.centerDlg(this);
		//pack();
	}
	
	Component createFieldPanel(Component component)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.BEFORE_LINE_BEGINS);
		return panel;
	}

	DialogField createDialogField(String tag, String existingValue) throws Exception
	{
		String label = EAM.fieldLabel(object.getType(), tag);

		if(tag.equals(Task.TAG_RESOURCE_IDS))
			return createResourcePicker(tag, label);
		
		DialogField dialogField = new StringDialogField(tag, label, existingValue);
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
		//FIXME.  taken out while converting from dialog to panel
		//if(cmd.getObjectType() == object.getType() && cmd.getObjectId().equals(object.getId()))
	//		dispose();
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

	public MainWindow getMainWindow()
	{
		return mainWindowToUse;
	}
	
	public Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	protected DialogField createResourcePicker(String tag, String label) throws ParseException
	{
		EAMObject[] availableResources = getProject().getAllProjectResources();
	
		int type = getObject().getType();
		BaseId id = getObject().getId();
		IdList selectedResources = new IdList(getProject().getObjectData(type, id, Task.TAG_RESOURCE_IDS));
		if(availableResources.length == 0)
			return new MessageField(tag, label, selectedResources.toString(), EAM.text("(No resources defined)"));
		return new MultiSelectDialogField(tag, label, availableResources, selectedResources);
	}
	
	protected DialogField createChoiceField(RatingQuestion question, String codeToSelect)
	{
		ChoiceDialogField field = new ChoiceDialogField(question);
		field.selectCode(codeToSelect);
		return field;
	}

	MainWindow mainWindowToUse;
	EAMObject object;
	DialogField[] fields;
}

