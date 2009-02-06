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
package org.miradi.forms.summary;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.main.EAM;
import org.miradi.objects.RareProjectData;
import org.miradi.views.summary.RareTrackingSummarySubPanel;

public class RareTabTrackingSubPanelForm extends FieldPanelSpec
{
	public RareTabTrackingSubPanelForm()
	{
		setHasBorder();
		setTranslatedTitle(RareTrackingSummarySubPanel.PANEL_DESCRIPTION);
		
		addLeftRightConstants(PROJECT_NUMBER_CONSTANT, SEE_PROJECT_TAB_CONSTANT);
		addLabelAndField(RareProjectData.getObjectType(), RareProjectData.TAG_COHORT);
		addLeftRightConstants(COUNTRY_CONSTANT, SEE_LOCATION_TAB_CONSTANT);
	}
	
	public static final String SEE_PROJECT_TAB_CONSTANT = EAM.text("(See Project tab)");
	public static final String PROJECT_NUMBER_CONSTANT = EAM.text("Project Number");
	public static final String COUNTRY_CONSTANT = EAM.text("Country"); 
	public static final String SEE_LOCATION_TAB_CONSTANT = EAM.text("(See Location tab)"); 
}
