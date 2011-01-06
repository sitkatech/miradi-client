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

import javax.swing.Icon;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.EmptyIcon;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringCodeListMap;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DashboardFlagsQuestion;
import org.miradi.utils.CodeList;

public class DashboardFlagIconField extends	AbstractDashboardClickableField
{
	public DashboardFlagIconField(Project projectToUse, ORef refToUse, String stringMapCodeToUse)
	{
		super(projectToUse, refToUse, Dashboard.TAG_NEEDS_ATTENTION_MAP, stringMapCodeToUse);
		
		question = getProject().getQuestion(DashboardFlagsQuestion.class);
	}
	
	@Override
	protected void updateLabelComponent(PanelTitleLabel labelComponentToUse, String mapValue) throws Exception
	{
		labelComponentToUse.setIcon(new EmptyIcon());
		CodeList codeList = new CodeList(mapValue);
		if (codeList.size() > 0)
		{
			String firstCode = codeList.firstElement();
			ChoiceItem progressChoiceItem = question.findChoiceByCode(firstCode);
			Icon icon = progressChoiceItem.getIcon();
			labelComponentToUse.setIcon(icon);
		}
	}
	
	@Override
	protected AbstractStringKeyMap createStringKeyMap(String stringCodeMapAsString) throws ParseException
	{
		return new StringCodeListMap(stringCodeMapAsString);
	}
	
	private ChoiceQuestion question;
}
