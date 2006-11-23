/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogfields.ObjectDateInputField;
import org.conservationmeasures.eam.dialogfields.ObjectMultilineInputField;
import org.conservationmeasures.eam.dialogfields.ObjectNumericInputField;
import org.conservationmeasures.eam.dialogfields.ObjectRatingField;
import org.conservationmeasures.eam.dialogfields.ObjectResourceListField;
import org.conservationmeasures.eam.dialogfields.ObjectStringInputField;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.RatingQuestion;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

abstract public class ObjectDataInputPanel extends ModelessDialogPanel implements CommandExecutedListener
{
	public ObjectDataInputPanel(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse)
	{
		GridLayoutPlus layout = new GridLayoutPlus(0, 2);
		layout.setColAlignment(0, Alignment.NORTHEAST);
		setLayout(layout);
		project = projectToUse;
		objectType = objectTypeToUse;
		objectId = objectIdToUse;
		fields = new Vector();
		project.addCommandExecutedListener(this);
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
		super.dispose();
	}

	public Project getProject()
	{
		return project;
	}
	
	public void setObjectId(BaseId newId)
	{
		saveModifiedFields();
		objectId = newId;
		updateFieldsFromProject();
	}
	
	public void setFocusOnFirstField()
	{
		((ObjectDataInputField)fields.get(0)).getComponent().requestFocusInWindow();
	}
		
	public void addField(ObjectDataInputField field)
	{
		fields.add(field);
		addLabel(field.getTag());
		addFieldComponent(field.getComponent());
	}

	public void addLabel(String translatedLabelText)
	{
		UiLabel label = new UiLabel(EAM.fieldLabel(objectType, translatedLabelText));
		label.setVerticalAlignment(SwingConstants.TOP);
		add(label);
	}
	
	public void addFieldComponent(Component component)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.BEFORE_LINE_BEGINS);
		add(panel);
	}
	
	public ObjectDataInputField createListField(Actions actions, String tag)
	{
		return new ObjectResourceListField(actions, project, objectType, objectId, tag);
	}
	
	public ObjectDataInputField createStringField(String tag)
	{
		return new ObjectStringInputField(project, objectType, objectId, tag);
	}
	
	public ObjectDataInputField createDateField(String tag)
	{
		return new ObjectDateInputField(project, objectType, objectId, tag);
	}
	
	public ObjectDataInputField createNumericField(String tag)
	{
		return new ObjectNumericInputField(project, objectType, objectId, tag);
	}
	
	public ObjectDataInputField createMultilineField(String tag)
	{
		return new ObjectMultilineInputField(project, objectType, objectId, tag);
	}
	
	public ObjectDataInputField createMultilineDisplayField(String tag)
	{
		return new ObjectMultilineDisplayField(project, objectType, objectId, tag);
	}
		
	public ObjectDataInputField createRatingField(RatingQuestion question)
	{
		return new ObjectRatingField(project, objectType, objectId, question);
	}
	
	public void saveModifiedFields()
	{
		for(int i = 0; i < fields.size(); ++i)
		{
			ObjectDataInputField field = (ObjectDataInputField)fields.get(i);
			field.saveIfNeeded();
		}
	}
	
	public void updateFieldsFromProject()
	{
		setFieldObjectIds(objectId);
		for(int i = 0; i < fields.size(); ++i)
		{
			ObjectDataInputField field = (ObjectDataInputField)fields.get(i);
			field.updateFromObject();
		}
	}
	
	public void setFieldObjectIds(BaseId newObjectId)
	{
		for(int i = 0; i < fields.size(); ++i)
		{
			ObjectDataInputField field = (ObjectDataInputField)fields.get(i);
			field.setObjectId(newObjectId);
		}
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if(wasOurObjectJustDeleted(event))
		{
			setFieldObjectIds(BaseId.INVALID);
			setObjectId(BaseId.INVALID);
			return;
		}
		updateFieldsFromProject();
	}
	
	public void commandUndone(CommandExecutedEvent event)
	{
		if(wasOurObjectJustCreateUndone(event))
		{
			setFieldObjectIds(BaseId.INVALID);
			setObjectId(BaseId.INVALID);
			return;
		}
		updateFieldsFromProject();
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}
	
	boolean wasOurObjectJustDeleted(CommandExecutedEvent event)
	{
		if(!event.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
			return false;
		
		CommandDeleteObject cmd = (CommandDeleteObject)event.getCommand();
		if(cmd.getObjectType() != objectType)
			return false;
		if(!cmd.getObjectId().equals(objectId))
			return false;
		return true;
	}

	boolean wasOurObjectJustCreateUndone(CommandExecutedEvent event)
	{
		if(!event.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
			return false;
		
		CommandDeleteObject cmd = (CommandDeleteObject)event.getCommand();
		if(cmd.getObjectType() != objectType)
			return false;
		if(cmd.getObjectId().equals(objectId))
			return false;
		return true;
	}

	//FIXME: Delete this when the old ObjectPropertiesPanel goes away
	public EAMObject getObject()
	{
		return null;
	}


	private Project project;
	private int objectType;
	private BaseId objectId;
	private Vector fields;
}
