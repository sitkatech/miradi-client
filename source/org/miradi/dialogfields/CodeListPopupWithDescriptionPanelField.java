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

import org.miradi.dialogfields.editors.CodeListPopupWithDescriptionPanelEditor;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.questions.ChoiceQuestion;

public class CodeListPopupWithDescriptionPanelField extends	ObjectDataInputField
{
	public CodeListPopupWithDescriptionPanelField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, ChoiceQuestion qusetionToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), refToUse, tagToUse);
		
		editorComponent = new CodeListPopupWithDescriptionPanelEditor(mainWindowToUse, qusetionToUse);
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
		editorComponent.setText(code);
	}
	
	private CodeListPopupWithDescriptionPanelEditor editorComponent; 
}
