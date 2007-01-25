/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;
import org.conservationmeasures.eam.dialogfields.ObjectAdjustableStringInputField;
import org.conservationmeasures.eam.dialogfields.ObjectChoiceField;
import org.conservationmeasures.eam.dialogfields.ObjectCodeListField;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogfields.ObjectDateChooserInputField;
import org.conservationmeasures.eam.dialogfields.ObjectMultilineDisplayField;
import org.conservationmeasures.eam.dialogfields.ObjectMultilineInputField;
import org.conservationmeasures.eam.dialogfields.ObjectNumericInputField;
import org.conservationmeasures.eam.dialogfields.ObjectReadonlyChoiceField;
import org.conservationmeasures.eam.dialogfields.ObjectStringInputField;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
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
		//TODO: should be first non read only field.
		if (fields.size()>0)
		{
			((ObjectDataInputField)fields.get(0)).getComponent().requestFocusInWindow();
			Rectangle rect = ((ObjectDataInputField)fields.get(0)).getComponent().getBounds();
			scrollRectToVisible(rect);
		}
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
	
	public ObjectDataInputField createStringField(String tag)
	{
		return new ObjectStringInputField(project, objectType, objectId, tag);
	}
	
	public ObjectDataInputField createStringField(String tag, int column)
	{
		return new ObjectAdjustableStringInputField(project, objectType, objectId, tag, column);
	}
	
	public ObjectDataInputField createDateChooserField(String tag)
	{
		return new ObjectDateChooserInputField(project, objectType, objectId, tag);
	}
	
	public ObjectDataInputField createNumericField(String tag, int column)
	{
		return new ObjectNumericInputField(project, objectType, objectId, tag, column);
	}
	
	public ObjectDataInputField createNumericField(String tag)
	{
		return new ObjectNumericInputField(project, objectType, objectId, tag);
	}
	
	public ObjectDataInputField createMultilineField(String tag)
	{
		return new ObjectMultilineInputField(project, objectType, objectId, tag);
	}
	
	public ObjectDataInputField createMultiCodeField(ChoiceQuestion question)
	{
		return new ObjectCodeListField(project, objectType, objectId, question);
	}
	
	public ObjectDataInputField createReadonlyTextField(String tag)
	{
		return new ObjectMultilineDisplayField(project, objectType, objectId, tag);
	}
		
	public ObjectDataInputField createChoiceField(ChoiceQuestion question)
	{
		return new ObjectChoiceField(project, objectType, objectId, question);
	}
	
	public ObjectDataInputField createReadOnlyChoiceField(ChoiceQuestion question)
	{
		return new ObjectReadonlyChoiceField(project, objectType, objectId, question);
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
		
		CommandCreateObject cmd = (CommandCreateObject)event.getCommand();
		if(cmd.getObjectType() != objectType)
			return false;

		return (cmd.getCreatedId().equals(objectId));
	}

	public EAMObject getObject()
	{
		return null;
	}
	
	public BaseId getObjectId()
	{
		return objectId;
	}


	private Project project;
	private int objectType;
	private BaseId objectId;
	private Vector fields;
}
