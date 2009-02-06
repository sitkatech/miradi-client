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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class RefListComponent extends AbstractDataValueListComponent
{
	public RefListComponent(ChoiceQuestion questionToUse, int columnCount, ListSelectionListener listener)
	{
		super(questionToUse, columnCount, listener);
	}

	public String getText()
	{
		ORefList refList = new ORefList();
		for (int checkBoxIndex = 0; checkBoxIndex<checkBoxes.length; ++checkBoxIndex )
		{
			JCheckBox checkBox = checkBoxes[checkBoxIndex];
			if (checkBox.isSelected())
			{
				ChoiceItem choiceItem = choiceItems[checkBoxIndex];
				refList.add(ORef.createFromString(choiceItem.getCode()));
			}
		}
		
		return refList.toString();
	}
	
	public void setText(String codesToUse)
	{
		skipNotice=true;
		try
		{
			ORefList refs = new ORefList(codesToUse);

			for (int choiceIndex = 0; choiceIndex<choiceItems.length; ++choiceIndex)
			{
				checkBoxes[choiceIndex].setSelected(false);
				ChoiceItem choiceItem = choiceItems[choiceIndex];
				String code = choiceItem.getCode();
				ORef ref = ORef.createFromString(code);
				boolean isChecked  = refs.contains(ref);
				checkBoxes[choiceIndex].setSelected(isChecked);
			}
		}
		catch(Exception e)
		{
			EAM.errorDialog(EAM.text("Internal Error"));
			EAM.logException(e);
		}
		finally
		{
			skipNotice=false;
		}
	}
}
