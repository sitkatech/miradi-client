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

package org.miradi.dialogs.tablerenderers;

import org.miradi.dialogfields.BaseObjectQuestionEditorComponent;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class QuestionPopupEditorTableCellEditorFactory extends AbstractPopupTableCellEditorFactory
{
	public QuestionPopupEditorTableCellEditorFactory(MainWindow mainWindowToUse, RowColumnSelectionProvider tableToUse, String tagToUse, ChoiceQuestion questionToUse, CodeList codesToDisableToUse)
	{
		super(mainWindowToUse, tableToUse);
		
		tag = tagToUse;
		question = questionToUse;
		codesToDisable = codesToDisableToUse;
	}

	@Override
	protected DisposablePanel createEditorComponenet(BaseObject baseObjectForRow)
	{
		return new BaseObjectQuestionEditorComponent(baseObjectForRow, tag, question, codesToDisable);
	}
	
	@Override
	protected String getDialogTitle()
	{
		return question.getQuestionDescription();
	}
	
	private String tag;
	private ChoiceQuestion question;
	private CodeList codesToDisable;
}
