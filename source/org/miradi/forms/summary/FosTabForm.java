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
import org.miradi.objects.FosProjectData;
import org.miradi.views.summary.FOSSummaryPanel;

public class FosTabForm extends FieldPanelSpec
{
	public FosTabForm()
	{
		setHasBorder();
		setTranslatedTitle(FOSSummaryPanel.PANEL_DESCRIPTION);

		addLabelAndField(FosProjectData.getObjectType(), FosProjectData.TAG_TRAINING_TYPE);
		addLabelAndField(FosProjectData.getObjectType(), FosProjectData.TAG_TRAINING_DATES);
		addLabelAndField(FosProjectData.getObjectType(), FosProjectData.TAG_TRAINERS);
		addLabelAndField(FosProjectData.getObjectType(), FosProjectData.TAG_COACHES);		
	}
}
