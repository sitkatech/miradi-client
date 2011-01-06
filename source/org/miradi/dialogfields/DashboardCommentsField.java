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

import java.awt.Dimension;
import java.text.ParseException;

import javax.swing.JComponent;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringStringMap;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;

public class DashboardCommentsField extends AbstractDashboardClickableField
{
	public DashboardCommentsField(Project projectToUse, ORef refToUse,String stringMapCodeToUse)
	{
		super(projectToUse, refToUse, Dashboard.TAG_USER_COMMENTS_MAP, stringMapCodeToUse);
	}

	@Override
	protected void setComponentPreferredSize(JComponent component)
	{
		Dimension preferredSize = component.getPreferredSize();
		final int ZERO_ENSURES_NARROWER_THAN_PARENT = 0;
		component.setPreferredSize(new Dimension(ZERO_ENSURES_NARROWER_THAN_PARENT, preferredSize.height));
	}

	@Override
	protected AbstractStringKeyMap createStringKeyMap(String stringCodeMapAsString) throws ParseException
	{
		return new StringStringMap(stringCodeMapAsString);
	}

	@Override
	protected void updateLabelComponent(PanelTitleLabel labelComponentToUse, String mapValue)
	{
		String[] splitValues = mapValue.split("\n");
		String firstLine = splitValues[0];
		firstLine  += "...";
		labelComponentToUse.setText(firstLine);
	}
}
