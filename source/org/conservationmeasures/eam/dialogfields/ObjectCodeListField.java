/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class ObjectCodeListField extends ObjectDataInputField implements ListSelectionListener
{
	public ObjectCodeListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, ChoiceQuestion questionToUse)
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
