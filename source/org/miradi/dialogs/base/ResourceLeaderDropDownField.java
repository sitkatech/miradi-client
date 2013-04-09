/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs.base;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

import javax.swing.JComponent;

import org.miradi.dialogfields.ObjectChoiceField;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.dialogs.planning.upperPanel.WhoStateLogic;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ResourceLeaderQuestionWithUnspecifiedChoice;

public class ResourceLeaderDropDownField extends ObjectChoiceField
{
	public ResourceLeaderDropDownField(Project projectToUse, ORef refToUse, String tagToUse, ResourceLeaderQuestionWithUnspecifiedChoice questionToUse)
	{
		super(projectToUse, refToUse, tagToUse, questionToUse);
		
		question = questionToUse;
	}
	
	@Override
	public boolean isValidObject()
	{
		if (!super.isValidObject())
			return false;
		
		BaseObject baseObject = BaseObject.find(getProject(), getORef());
		
		return new WhoStateLogic(getProject()).isLeaderEditable(baseObject);
	}

	@Override
	public void updateFromObject()
	{
		question.setObjectContainingLeaderRef(getORef());
		replaceExistingComboBoxWithNewToAvoidSwingReselectAfterOurSelection();
		
		super.updateFromObject();
	}

	private void replaceExistingComboBoxWithNewToAvoidSwingReselectAfterOurSelection()
	{
		JComponent parent = (JComponent) getComponent().getParent();
		if (parent == null)
			return;

		ChoiceItemComboBox newCombo = new ChoiceItemComboBox(question.getChoices());
		transferFocusListeners(newCombo);
		transferActionListeners(newCombo);

		int currentComboIndex = getComponentIndex(getComboBox());
		parent.add(newCombo, currentComboIndex);
		parent.remove(getComboBox());
		setComboBox(newCombo);
		parent.revalidate();
	}

	private void transferActionListeners(ChoiceItemComboBox newCombo)
	{
		ActionListener[] actionListeners = getComboBox().getActionListeners();
		for(ActionListener actionListener : actionListeners)
		{
			newCombo.addActionListener(actionListener);
			getComboBox().removeActionListener(actionListener);
		}
	}

	private void transferFocusListeners(ChoiceItemComboBox newCombo)
	{
		FocusListener[] focusListeners = getComboBox().getFocusListeners();
		for(FocusListener focusListener : focusListeners)
		{
			newCombo.addFocusListener(focusListener);
			getComboBox().removeFocusListener(focusListener);
		}
	}
	
	private final int getComponentIndex(Component component) 
	{
		Container container = component.getParent();
		for (int index = 0; index < container.getComponentCount(); index++) 
		{
			if (container.getComponent(index) == component)
				return index;
		}

		return -1;
	}

	private ResourceLeaderQuestionWithUnspecifiedChoice question;
}
