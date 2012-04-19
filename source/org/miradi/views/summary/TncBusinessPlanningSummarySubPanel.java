/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.TncProjectData;
import org.miradi.project.Project;
import org.miradi.schemas.TncProjectDataSchema;

public class TncBusinessPlanningSummarySubPanel extends ObjectDataInputPanel
{
	public TncBusinessPlanningSummarySubPanel(Project projectToUse, ORef[] refs) throws Exception
	{
		super(projectToUse, refs);
		
		addField(createMultilineField(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_MAKING_THE_CASE));
		addField(createMultilineField(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_RISKS));
		addField(createMultilineField(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_CAPACITY_AND_FUNDING));
	}

	@Override
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("Label|Business Planning");
}
