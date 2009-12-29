/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import java.text.ParseException;

import javax.swing.event.ListSelectionListener;

import org.miradi.main.EAM;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class QuestionBasedEditorComponent extends AbstractQuestionBasedComponent
{
	public QuestionBasedEditorComponent(ChoiceQuestion questionToUse,	int columnCount, ListSelectionListener listener)
	{
		super(questionToUse, columnCount, listener);
	}

	@Override
	public String getText()
	{
		CodeList codes = getSelectedCodes();
		setSameToolTipForAllToggleButtons();
		
		return codes.toString();
	}

	@Override
	public void setText(String codesToUse)
	{
		CodeList codes = createCodeListFromString(codesToUse);
		updateToggleButtonSelections(codes);
	}

	protected CodeList createCodeListFromString(String codesToUse)
	{
		try
		{
			return new CodeList(codesToUse);
		}
		catch(ParseException e)
		{
			EAM.errorDialog(EAM.text("Internal Error"));
			EAM.logException(e);
			return new CodeList();
		}
	}
}
