/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.ResourceRoleQuestion;

public class ObjectCodeListField extends ObjectDataInputField implements ListSelectionListener
{
	public ObjectCodeListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, ResourceRoleQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, questionToUse.getTag());
		component = new CodeListComponent(questionToUse,this);
	}
	
	public JComponent getComponent()
	{
		return component;
	}

	public String getText()
	{
		return component.getText();
	}

	public void setText(String codes)
	{
		component.setText(codes);
	}
	
	public void updateEditableState()
	{
		component.setEnabled(isValidObject());
	}
	
	public void valueChanged(ListSelectionEvent arg0)
	{
		setNeedsSave();
		saveIfNeeded();
	}
	
	CodeListComponent component;

}
