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

import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.CodeList;

import com.jhlabs.awt.BasicGridLayout;

public class ReadOnlyCodeListComponent extends MiradiPanel
{
	public ReadOnlyCodeListComponent(ChoiceItem[] choiceItemsToUse, int columnCount)
	{
		setLayout(new BasicGridLayout(0, columnCount));
		choiceItems = choiceItemsToUse;		
		setBackground(EAM.READONLY_BACKGROUND_COLOR);
		setForeground(EAM.READONLY_FOREGROUND_COLOR);
	}

	public String getText()
	{
		CodeList codeList = new CodeList();
		for (int choiceIndex = 0; choiceIndex < choiceItems.length; ++choiceIndex )
		{
			codeList.add(choiceItems[choiceIndex].getCode());
		}
		
		return codeList.toString();
	}
	
	public void setText(String codesToUse)
	{
		removeAll();
		try
		{
			CodeList codeList = new CodeList(codesToUse);
			for (int choiceIndex = 0; choiceIndex < choiceItems.length; ++choiceIndex)
			{
				if (codeList.contains(choiceItems[choiceIndex].getCode()))
				{
					add(new PanelTitleLabel(choiceItems[choiceIndex].getLabel()));
				}
			}
		}
		catch(Exception e)
		{
			EAM.errorDialog(EAM.text("Internal Error"));
			EAM.logException(e);
		}
		
		if (getTopLevelAncestor() != null)
			getTopLevelAncestor().validate();
	}
	
	protected ChoiceItem choiceItems[];
}
