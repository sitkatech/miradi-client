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

import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.QuestionPopupEditorComponent;

public class PopupQuestionEditorField extends ObjectDataInputField
{
	public PopupQuestionEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion qusetionToUse) throws Exception
	{
		super(projectToUse, refToUse, tagToUse);
		
		editorComponent = new QuestionPopupEditorComponent(qusetionToUse);
		editorComponent.addFocusListener(this);
	}

	@Override
	public JComponent getComponent()
	{
		return editorComponent;
	}

	@Override
	public String getText()
	{
		return editorComponent.getText();
	}

	@Override
	public void setText(String code)
	{
		editorComponent.setCode(code);
	}
	
	@Override
	void clearNeedsSave()
	{
		editorComponent.clearNeedsSaving();
		super.clearNeedsSave();
	}
	
	@Override
	public boolean needsToBeSaved()
	{
		return editorComponent.needsToBeSaved();
	}
	
	private QuestionPopupEditorComponent editorComponent; 
}
