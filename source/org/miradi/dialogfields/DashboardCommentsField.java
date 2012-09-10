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
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;

public class DashboardCommentsField extends AbstractDashboardClickableField
{
	public DashboardCommentsField(Project projectToUse, ORef refToUse,String stringMapCodeToUse)
	{
		super(projectToUse, refToUse, stringMapCodeToUse);
	}

	@Override
	protected AbstractStringKeyMap createStringKeyMap(Dashboard dashboard) throws Exception
	{
		return dashboard.getCommentsMap();
	}

	@Override
	protected void updateLabelComponent(PanelTitleLabel labelComponentToUse, String mapValue) throws Exception
	{
		String firstLine = mapValue;
		int newlineAt = mapValue.indexOf("\n");
		if (newlineAt >= 0)
			firstLine = mapValue.substring(0, newlineAt) + "...";
		
		labelComponentToUse.setText(firstLine);
	}
	
	public boolean hasComments() throws Exception
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		Dashboard dashboard = Dashboard.find(getProject(), dashboardRef);
		AbstractStringKeyMap commentsMap = createStringKeyMap(dashboard);
		String comment = commentsMap.get(stringMapCode);
		return comment.length() > 0;
	}
}
