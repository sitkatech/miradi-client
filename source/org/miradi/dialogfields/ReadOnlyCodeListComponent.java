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

import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class ReadOnlyCodeListComponent extends AbstractReadOnlyComponent
{
	public ReadOnlyCodeListComponent(ChoiceQuestion questionToUse)
	{
		this(questionToUse, SINGLE_COLUMN_COUNT);
	}
	
	public ReadOnlyCodeListComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		super(columnCount);
		
		choiceItems = questionToUse.getChoices();		
		codeList = new CodeList();
	}

	@Override
	public String getText()
	{
		return codeList.toString();
	}
	
	@Override
	public void setText(String codesToUse)
	{
		try
		{
			codeList = new CodeList(codesToUse);
			createAndAddReadonlyLabels(codeList, choiceItems);
		}
		catch(ParseException e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	protected ChoiceItem choiceItems[];
	private CodeList codeList;
}
