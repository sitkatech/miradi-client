/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.Component;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiTextField;

public class ObjectTextInputField extends ObjectDataInputField
{
	public ObjectTextInputField(Project projectToUse, int objectType, BaseId objectId, String tag, UiTextField componentToUse)
	{
		super(projectToUse, objectType, objectId, tag);
		field = componentToUse;
		addFocusListener();
	}

	public Component getComponent()
	{
		return field;
	}

	public String getText()
	{
		return field.getText();
	}

	public void setText(String newValue)
	{
		field.setText(newValue);
	}
	
	public void setEditable(boolean newState)
	{
		field.setEditable(newState);
	}

	UiTextField field;


}
