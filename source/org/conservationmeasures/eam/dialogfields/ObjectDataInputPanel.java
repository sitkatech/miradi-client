/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Vector;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.dialogfields.legacy.LegacyObjectDataInputField;
import org.conservationmeasures.eam.dialogfields.legacy.LegacyObjectDateInputField;
import org.conservationmeasures.eam.dialogfields.legacy.LegacyObjectMultilineInputField;
import org.conservationmeasures.eam.dialogfields.legacy.LegacyObjectNumericInputField;
import org.conservationmeasures.eam.dialogfields.legacy.LegacyObjectRatingField;
import org.conservationmeasures.eam.dialogfields.legacy.LegacyObjectResourceListField;
import org.conservationmeasures.eam.dialogfields.legacy.LegacyObjectStringInputField;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.RatingQuestion;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

abstract public class ObjectDataInputPanel extends ModelessDialogPanel implements CommandExecutedListener
{
	public ObjectDataInputPanel(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse)
	{
		super(new BasicGridLayout(0, 2));
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
		objectId = newId;
		//FIXME This is not the right solution to where the propertiesPanel 
		//field is changed by the user but the activitiespooltable panel is 
		//not changed.  
		//On the other hand
		//The ActivitiesPoolTablePanel is not updated by the "add rources"
		//of the ResourceTablePoolPanel popup dialog
		//saveModifiedFields();
		updateFieldsFromProject();
	}
	
	public void setFocusOnFirstField()
	{
		((LegacyObjectDataInputField)fields.get(0)).getComponent().requestFocusInWindow();
	}
		
	public void addField(LegacyObjectDataInputField field)
	{
		fields.add(field);
		add(new UiLabel(EAM.fieldLabel(objectType, field.getTag())));
		addFieldComponent(field.getComponent());
	}
	
	public void addFieldComponent(Component component)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.BEFORE_LINE_BEGINS);
		add(panel);
	}
	
	public LegacyObjectDataInputField createListField(Actions actions, String tag)
	{
		return new LegacyObjectResourceListField(actions, project, objectType, objectId, tag);
	}
	
	public LegacyObjectDataInputField createStringField(String tag)
	{
		return new LegacyObjectStringInputField(project, objectType, objectId, tag);
	}
	
	public LegacyObjectDataInputField createDateField(String tag)
	{
		return new LegacyObjectDateInputField(project, objectType, objectId, tag);
	}
	
	public LegacyObjectDataInputField createNumericField(String tag)
	{
		return new LegacyObjectNumericInputField(project, objectType, objectId, tag);
	}
	
	public LegacyObjectDataInputField createMultilineField(String tag)
	{
		return new LegacyObjectMultilineInputField(project, objectType, objectId, tag);
	}
	
	public LegacyObjectDataInputField createRatingField(RatingQuestion question)
	{
		return new LegacyObjectRatingField(project, objectType, objectId, question);
	}
	
	public void saveModifiedFields()
	{
		for(int i = 0; i < fields.size(); ++i)
		{
			LegacyObjectDataInputField field = (LegacyObjectDataInputField)fields.get(i);
			field.save();
		}
	}
	
	public void updateFieldsFromProject()
	{
		for(int i = 0; i < fields.size(); ++i)
		{
			LegacyObjectDataInputField field = (LegacyObjectDataInputField)fields.get(i);
			field.setObjectId(objectId);
			field.updateFromObject();
		}
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		updateFieldsFromProject();
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		updateFieldsFromProject();
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
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
