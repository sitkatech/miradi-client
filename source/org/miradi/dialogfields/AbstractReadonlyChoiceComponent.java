/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogfields;

import java.util.Vector;

import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.RatingIcon;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

import com.jhlabs.awt.BasicGridLayout;

abstract public class AbstractReadonlyChoiceComponent extends MiradiPanel
{
	public AbstractReadonlyChoiceComponent(ChoiceQuestion questionToUse)
	{
		this(questionToUse, SINGLE_COLUMN_COUNT);
	}

	public AbstractReadonlyChoiceComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		question = questionToUse;
		setLayout(new BasicGridLayout(0, columnCount));
		setBackground(EAM.READONLY_BACKGROUND_COLOR);
		setForeground(EAM.READONLY_FOREGROUND_COLOR);
	}		
	
	protected void createAndAddReadonlyLabels(final CodeList codeList)
	{
		removeAll();
		if (codeList.hasData())
		{
			getQuestion().reloadQuestion();
			Vector<ChoiceItem> choiceItems = getQuestion().getChoicesAsVector();
			createAndAddReadonlyLabelsRecursively(choiceItems, codeList);
		}

		if (getTopLevelAncestor() != null)
		{
			getTopLevelAncestor().validate();
			getTopLevelAncestor().repaint();
		}
	}

	private void createAndAddReadonlyLabelsRecursively(Vector<ChoiceItem> allChoiceItems, final CodeList selectedCodes)
	{
		try
		{
			for (ChoiceItem choiceItem : allChoiceItems)
			{
				if (selectedCodes.contains(choiceItem.getCode()))
				{
					PanelTitleLabel label = new PanelTitleLabel(choiceItem.getTextAsHtmlWrappedLabel());
					if (choiceItem.getColor() != null)
						label.setIcon(new RatingIcon(choiceItem));
					
					add(label);
				}
				
				createAndAddReadonlyLabelsRecursively(choiceItem.getChildren(), selectedCodes);
			}
		}
		catch(Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}
	
	protected ChoiceQuestion getQuestion()
	{
		return question;
	}
	
	abstract public String getText();
	
	abstract public void setText(String text);

	protected static final int SINGLE_COLUMN_COUNT = 1;
	private ChoiceQuestion question;
}
