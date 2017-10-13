/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
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
		
		return true;
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
		//NOTE: ActionListeners are transferred since the parent constructor adds actionListener. 
		//Parent saves data using ActionListener vs ItemListener.  
		//Default focus listeners are removed from new comboBox since we want to transfer
		//the default and custom focus listeners from old combo box. 
		// This may be dangerous if more ActionListeners or FocusListeners are used in the future
		// or if they are changed
		transferActionListeners(newCombo);
		transferFocusListeners(newCombo);

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
		removeAllFocusListeners(newCombo);
		FocusListener[] focusListeners = getComboBox().getFocusListeners();
		for(FocusListener focusListener : focusListeners)
		{
			newCombo.addFocusListener(focusListener);
		}
		
		if (getComboBox().getFocusListeners().length != newCombo.getFocusListeners().length)
			EAM.logWarning("Orphan focus listener created");
	}

	private void removeAllFocusListeners(ChoiceItemComboBox combo)
	{
		FocusListener[] focusListeners = combo.getFocusListeners();
		for(FocusListener focusListener : focusListeners)
		{
			combo.removeFocusListener(focusListener);
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
