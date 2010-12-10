/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class ReadonlyClickableIconField extends ObjectDataInputField
{
	public ReadonlyClickableIconField(Project projectToUse, ORef refToUse,	String tagToUse, String stringMapCodeToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, refToUse, tagToUse);
		
		stringMapCode = stringMapCodeToUse;
		iconComponent = new PanelTitleLabel();
		question = questionToUse;
	}
	
	@Override
	public void setText(String stringCodeMapAsString)
	{
		try
		{
			StringChoiceMap map = new StringChoiceMap(stringCodeMapAsString);
			String code = map.get(stringMapCode);
			ChoiceItem progressChoiceItem = question.findChoiceByCode(code);
			Icon icon = progressChoiceItem.getIcon();
			iconComponent.setIcon(icon);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	@Override
	public void updateEditableState()
	{
		iconComponent.setEnabled(true);
	}

	@Override
	public JComponent getComponent()
	{
		return iconComponent;
	}

	@Override
	public String getText()
	{
		return "";
	}

	private PanelTitleLabel iconComponent;
	private String stringMapCode;
	private ChoiceQuestion question;
}
