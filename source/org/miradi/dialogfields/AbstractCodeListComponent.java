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


import javax.swing.JCheckBox;
import javax.swing.event.ListSelectionListener;

import org.martus.util.xml.XmlUtilities;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

abstract public class AbstractCodeListComponent extends AbstractDataValueListComponent
{
	public AbstractCodeListComponent(ChoiceQuestion questionToUse, int columnCount, ListSelectionListener listener)
	{
		super(questionToUse, columnCount, listener);
		
		codesToDisable = new CodeList();
	}
	
	protected CodeList getSelectedCodes()
	{
		CodeList codes = new CodeList();
		for (int checkBoxIndex = 0; checkBoxIndex<checkBoxes.length; ++checkBoxIndex )
		{
			JCheckBox checkBox = checkBoxes[checkBoxIndex];
			if (checkBox.isSelected())
			{
				ChoiceItem choiceItem = choiceItems[checkBoxIndex];
				codes.add(choiceItem.getCode());
			}
		}
		
		return codes;
	}
	
	protected void createCheckBoxes(CodeList codes)
	{
		enableSkipNotice();
		try
		{
			for (int choiceIndex = 0; choiceIndex<choiceItems.length; ++choiceIndex)
			{
				checkBoxes[choiceIndex].setSelected(false);
				ChoiceItem choiceItem = choiceItems[choiceIndex];
				boolean isChecked  = codes.contains(choiceItem.getCode());
				checkBoxes[choiceIndex].setSelected(isChecked);
			}
			
			setSameToolTipForAllCheckBoxes();
		}
		finally
		{
			disableSkipNotice();
		}
	}

	protected String setSameToolTipForAllCheckBoxes()
	{
		String partialToolTip = ""; 
		int selectionCount = 0;
		for (int choiceIndex = 0; (choiceIndex < choiceItems.length && selectionCount <= MAX_ITEMS_COUNT_IN_TOOLTIP); ++choiceIndex)
		{
			ChoiceItem choiceItem = choiceItems[choiceIndex];
			if (checkBoxes[choiceIndex].isSelected() )
			{
				partialToolTip += XmlUtilities.getXmlEncoded(choiceItem.getLabel()) + "<BR>";
				++selectionCount;
			}
		}
		
		String moreText = "";
		if (getSelectedCodes().size() > (MAX_ITEMS_COUNT_IN_TOOLTIP  + 1))
			moreText = "...more";
		
		String toolTip = "<HTML>" + partialToolTip + moreText + "</HTML>";
		for (int choiceIndex = 0; choiceIndex < choiceItems.length; ++choiceIndex)
		{
			checkBoxes[choiceIndex].setToolTipText(toolTip);
		}
		
		return toolTip;
	}
	
	public void setEnabled(boolean isValidObject)
	{
		super.setEnabled(isValidObject);
		disableCheckBoxes();
	}

	private void disableCheckBoxes()
	{
		for (int choiceIndex = 0; choiceIndex<checkBoxes.length; ++choiceIndex)
		{
			ChoiceItem choiceItem = choiceItems[choiceIndex];
			if (codesToDisable.contains(choiceItem.getCode()))
				checkBoxes[choiceIndex].setEnabled(false);
		}
	}

	public void setDisabledCodes(CodeList codesToDiableToUse)
	{
		codesToDisable = codesToDiableToUse;
	}
	
	@Override
	public String getText()
	{
		return "";
	}

	@Override
	public void setText(String codesToUse)
	{
	}
	
	private CodeList codesToDisable;
	private static final int MAX_ITEMS_COUNT_IN_TOOLTIP = 5;
}
