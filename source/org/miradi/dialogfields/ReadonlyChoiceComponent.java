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

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.RatingIcon;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class ReadonlyChoiceComponent extends AbstractReadOnlyComponent
{
	public ReadonlyChoiceComponent(ChoiceQuestion questionToUse)
	{
		this(questionToUse, SINGLE_COULMN_COUNT);
	}
	
	public ReadonlyChoiceComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		super(columnCount);
		
		question = questionToUse;
		currentCode = "";
		initliazeRadioButtons();
	}

	private void initliazeRadioButtons()
	{
		setText(currentCode);
	}

	@Override
	public String getText()
	{
		return currentCode;
	}
	
	@Override
	public void setText(String code)
	{
		removeAll();
		try
		{
			currentCode = code;
			ChoiceItem choiceItem = question.findChoiceByCode(code);
			PanelTitleLabel label = new PanelTitleLabel(choiceItem.getLabel());
			if (choiceItem.getColor() != null)
				label.setIcon(new RatingIcon(choiceItem));

			add(label);
			
			if (getTopLevelAncestor() != null)
				getTopLevelAncestor().validate();
		}
		catch(Exception e)
		{
			EAM.unexpectedErrorDialog(e);
			EAM.logException(e);
		}
	}

	private ChoiceQuestion question;
	private String currentCode;
}
