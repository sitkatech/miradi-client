/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.OneFieldObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class SingleCodeEditableField extends AbstractEditableCodeListField
{
	public SingleCodeEditableField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		super(mainWindowToUse.getProject(), refToUse, tagToUse, questionToUse, columnCount);
		
		mainWindow = mainWindowToUse;
	}

	@Override
	protected DisposablePanel createEditorPanel() throws Exception
	{
		RadiobButtonEditorComponentWIthHierarchicalRows editor = new RadiobButtonEditorComponentWIthHierarchicalRows(mainWindow, question);
		ComponentWrapperObjectDataInputField field = new ComponentWrapperObjectDataInputField(getProject(), getORef(), getTag(), editor);
		OneFieldObjectDataInputPanel leftPanel = new OneFieldObjectDataInputPanel(getProject(), getORef(), getTag(), field);

		return leftPanel;
	}
	
	@Override
	public void setText(String newValue)
	{
		CodeList codeList = new CodeList();
		codeList.add(newValue);
		
		super.setText(codeList.toString());
	}
	
	@Override
	public String getText()
	{
		try
		{
			String superGetTextValue = super.getText();
			CodeList codeList = new CodeList(superGetTextValue);
			if (codeList.size() > 0)
				return codeList.firstElement();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
		
		return "";
	}
	
	private MainWindow mainWindow;
}
