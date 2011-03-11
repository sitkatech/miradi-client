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

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.RatingIcon;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.CodeList;

public class ReadOnlyCodeListComponent extends AbstractReadOnlyComponent
{
	public ReadOnlyCodeListComponent(ChoiceItem[] choiceItemsToUse)
	{
		this(choiceItemsToUse, 1);
	}
	
	public ReadOnlyCodeListComponent(ChoiceItem[] choiceItemsToUse, int columnCount)
	{
		super(columnCount);
		
		choiceItems = choiceItemsToUse;		
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
		removeAll();
		try
		{
			codeList = new CodeList(codesToUse);
			for (int choiceIndex = 0; choiceIndex < choiceItems.length; ++choiceIndex)
			{
				ChoiceItem choiceItem = choiceItems[choiceIndex];
				if (codeList.contains(choiceItem.getCode()))
				{
					PanelTitleLabel label = new PanelTitleLabel(choiceItem.getLabel());
					if (choiceItem.getColor() != null)
						label.setIcon(new RatingIcon(choiceItem));
					
					add(label);
				}
			}
		}
		catch(Exception e)
		{
			EAM.unexpectedErrorDialog(e);
			EAM.logException(e);
		}
		
		if (getTopLevelAncestor() != null)
			getTopLevelAncestor().validate();
	}
	
	protected ChoiceItem choiceItems[];
	private CodeList codeList;
}
