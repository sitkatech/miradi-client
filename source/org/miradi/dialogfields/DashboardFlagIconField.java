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

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.EmptyIcon;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringCodeListMap;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.DashboardFlagsQuestion;
import org.miradi.utils.CodeList;

public class DashboardFlagIconField extends	AsbtractDashboardClickableQuestionField
{
	public DashboardFlagIconField(Project projectToUse, ORef refToUse, String stringMapCodeToUse)
	{
		super(projectToUse, refToUse, stringMapCodeToUse, Dashboard.TAG_NEEDS_ATTENTION_MAP, projectToUse.getQuestion(DashboardFlagsQuestion.class));
	}
	
	@Override
	protected String getCode(String stringCodeMapAsString, String stringMapCodeToUse) throws Exception
	{
		AbstractStringKeyMap map = new StringCodeListMap(stringCodeMapAsString);
		CodeList codes = new CodeList(map.get(stringMapCode));
		if (codes.hasData())
			return codes.firstElement();
		
		return "";
	}

	@Override
	protected void updateLabel(ChoiceItem progressChoiceItem, PanelTitleLabel componentToUpdate)
	{
		componentToUpdate.setIcon(new EmptyIcon());
		if (progressChoiceItem != null)
		{
			Icon icon = progressChoiceItem.getIcon();
			componentToUpdate.setIcon(icon);
		}
	}
}
