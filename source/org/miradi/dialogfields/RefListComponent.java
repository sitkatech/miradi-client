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

import java.util.Set;

import javax.swing.JToggleButton;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class RefListComponent extends AbstractDataValueListComponent
{
	public RefListComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		super(questionToUse, columnCount);
	}

	public String getText()
	{
		ORefList refList = new ORefList();
		Set<ChoiceItem> choices = choiceItemToToggleButtonMap.keySet();
		for(ChoiceItem choiceItem : choices)
		{
			JToggleButton toggleButton = choiceItemToToggleButtonMap.get(choiceItem);
			if (toggleButton.isSelected())
			{
				refList.add(ORef.createFromString(choiceItem.getCode()));
			}
		}
		
		return refList.toString();
	}
	
	public void setText(String refListToUse)
	{
		enableSkipNotification();
		try
		{
			ORefList refs = new ORefList(refListToUse);

			Set<ChoiceItem> choices = choiceItemToToggleButtonMap.keySet();
			for(ChoiceItem choiceItem : choices)
			{
				JToggleButton toggleButton = choiceItemToToggleButtonMap.get(choiceItem);
				toggleButton.setSelected(false);
				String code = choiceItem.getCode();
				ORef ref = ORef.createFromString(code);
				boolean isChecked  = refs.contains(ref);
				toggleButton.setSelected(isChecked);
			}
		}
		catch(Exception e)
		{
			EAM.unexpectedErrorDialog();
			EAM.logException(e);
		}
		finally
		{
			disableSkipNotification();
		}
	}
}
