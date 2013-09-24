/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import javax.swing.JComponent;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.base.ReadonlyPanelWithPopupEditor;
import org.miradi.dialogs.base.ReadonlyPanelWithPopupEditorWithoutMainScrollPane;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.Translation;

abstract public class AbstractEditableCodeListField extends ObjectDataInputField implements ReadonlyPanelAndPopupEditorProvider
{
	public AbstractEditableCodeListField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, refToUse, tagToUse);
		
		question = questionToUse;
	}
	
	@Override
	protected boolean shouldBeEditable()
	{
		return isValidObject();
	}
	
	@Override
	public JComponent getComponent()
	{
		if (readonlyPanelPopupEditor == null)
		{
			final String dialogTitle = Translation.fieldLabel(getObjectType(), getTag());
			readonlyPanelPopupEditor = new ReadonlyPanelWithPopupEditorWithoutMainScrollPane(this, dialogTitle, question);
		}
		
		return readonlyPanelPopupEditor;
	}

	@Override
	public String getText()
	{
		return readonlyPanelPopupEditor.getText();
	}

	@Override
	public void setText(String newValue)
	{
		readonlyPanelPopupEditor.setText(newValue);
	}
	
	protected void addDialogMainPanel(ModalDialogWithClose dialog, DisposablePanel editorPanel)
	{
		dialog.setScrollableMainPanel(editorPanel);
	}
	
	protected ChoiceQuestion getQuestion()
	{
		return question;
	}
	
	abstract public DisposablePanel createEditorPanel() throws Exception;
	
	abstract public AbstractReadonlyChoiceComponent createReadOnlyComponent(ChoiceQuestion questionToUse, int columnCount);
	
	protected ChoiceQuestion question;
	private ReadonlyPanelWithPopupEditor readonlyPanelPopupEditor;
}
