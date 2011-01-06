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

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;


abstract public class AsbtractDashboardClickableQuestionField extends AbstractDashboardClickableField
{
	public AsbtractDashboardClickableQuestionField(Project projectToUse, ORef refToUse, String stringMapCodeToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, refToUse, tagToUse, stringMapCodeToUse);
		
		question = questionToUse;
	}
	
	@Override
	public void setText(String stringCodeMapAsString)
	{
		try
		{
			String code = getCode(stringCodeMapAsString, stringMapCode);
			ChoiceItem progressChoiceItem = question.findChoiceByCode(code);
			updateLabel(progressChoiceItem, iconComponent);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}

	protected String getCode(String stringCodeMapAsString, String stringMapCodeToUse) throws Exception
	{
		AbstractStringKeyMap map = new StringChoiceMap(stringCodeMapAsString);
		return map.get(stringMapCodeToUse);
	}

	abstract protected void updateLabel(ChoiceItem progressChoiceItem, PanelTitleLabel componentToUpdate);

	private ChoiceQuestion question;
}
