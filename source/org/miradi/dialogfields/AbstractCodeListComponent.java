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


import javax.swing.JToggleButton;
import javax.swing.event.ListSelectionListener;

import org.martus.util.xml.XmlUtilities;
import org.miradi.main.EAM;
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
		for (int index = 0; index<toggleButtons.length; ++index )
		{
			JToggleButton toggleButton = toggleButtons[index];
			if (toggleButton.isSelected())
			{
				ChoiceItem choiceItem = choiceItems[index];
				codes.add(choiceItem.getCode());
			}
		}
		
		return codes;
	}
	
	protected void updateCheckBoxesSelection(CodeList codes)
	{
		enableSkipNotification();
		try
		{
			for (int index = 0; index<choiceItems.length; ++index)
			{
				toggleButtons[index].setSelected(false);
				ChoiceItem choiceItem = choiceItems[index];
				boolean isChecked  = codes.contains(choiceItem.getCode());
				toggleButtons[index].setSelected(isChecked);
			}
			
			setSameToolTipForAllCheckBoxes();
		}
		finally
		{
			disableSkipNotification();
		}
	}

	protected String setSameToolTipForAllCheckBoxes()
	{
		String partialToolTip = ""; 
		int selectionCount = 0;
		for (int choiceIndex = 0; (choiceIndex < choiceItems.length && selectionCount <= MAX_ITEMS_COUNT_IN_TOOLTIP); ++choiceIndex)
		{
			ChoiceItem choiceItem = choiceItems[choiceIndex];
			if (toggleButtons[choiceIndex].isSelected() )
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
			toggleButtons[choiceIndex].setToolTipText(toolTip);
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
		for (int choiceIndex = 0; choiceIndex<toggleButtons.length; ++choiceIndex)
		{
			ChoiceItem choiceItem = choiceItems[choiceIndex];
			if (codesToDisable.contains(choiceItem.getCode()))
				toggleButtons[choiceIndex].setEnabled(false);
		}
	}

	public void setDisabledCodes(CodeList codesToDiableToUse)
	{
		codesToDisable = codesToDiableToUse;
	}
	
	@Override
	public String getText()
	{
		throw new RuntimeException(EAM.text("Unexpected call to getText"));
	}

	@Override
	public void setText(String codesToUse)
	{
		throw new RuntimeException(EAM.text("Unexpected call to setText"));
	}
	
	private CodeList codesToDisable;
	private static final int MAX_ITEMS_COUNT_IN_TOOLTIP = 5;
}
