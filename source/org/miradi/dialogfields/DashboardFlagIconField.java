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
import org.miradi.icons.EmptyIcon;
import org.miradi.icons.IconManager;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.DashboardFlagsQuestion;
import org.miradi.utils.CodeList;

public class DashboardFlagIconField extends	AbstractDashboardClickableField
{
	public DashboardFlagIconField(Project projectToUse, ORef refToUse, String stringMapCodeToUse)
	{
		super(projectToUse, refToUse, Dashboard.TAG_FLAGS_MAP, stringMapCodeToUse);	
	}
	
	@Override
	protected void updateLabelComponent(PanelTitleLabel labelComponentToUse, String mapValue) throws Exception
	{
		labelComponentToUse.setIcon(new EmptyIcon());
		CodeList codeList = new CodeList(mapValue);
		if (codeList.contains(DashboardFlagsQuestion.NEEDS_ATTENTION_CODE))
		{
			labelComponentToUse.setIcon(IconManager.getNeedsAttentionIcon());
		}
	}
	
	@Override
	protected AbstractStringKeyMap createStringKeyMap(Dashboard dashboard) throws Exception
	{
		return dashboard.getFlagsMap();
	}
}
