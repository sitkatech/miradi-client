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

import java.text.ParseException;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;


abstract public class AsbtractDashboardClickableQuestionField extends AbstractDashboardClickableField
{
	public AsbtractDashboardClickableQuestionField(Project projectToUse, ORef refToUse, String tagToUse, String stringMapCodeToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, refToUse, tagToUse, stringMapCodeToUse);
		
		question = questionToUse;
	}
	
	@Override
	protected void updateLabelComponent(PanelTitleLabel labelComponentToUse, String mapValue) throws Exception
	{
		ChoiceItem progressChoiceItem = question.findChoiceByCode(mapValue);
		updateLabel(labelComponentToUse, progressChoiceItem);
	}
	
	@Override
	protected AbstractStringKeyMap createStringKeyMap(String stringCodeMapAsString) throws ParseException
	{
		return new StringChoiceMap(stringCodeMapAsString);
	}

	abstract protected void updateLabel(PanelTitleLabel componentToUpdate, ChoiceItem progressChoiceItem);

	private ChoiceQuestion question;
}
