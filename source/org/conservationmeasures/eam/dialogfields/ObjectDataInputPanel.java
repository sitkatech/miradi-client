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

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
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
	
	public Project getProject()
	{
		return project;
	}
	
	public void addField(ObjectDataInputField field)
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
	
	public void updateFieldsFromProject()
	{
		for(int i = 0; i < fields.size(); ++i)
		{
			ObjectDataInputField field = (ObjectDataInputField)fields.get(i);
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
