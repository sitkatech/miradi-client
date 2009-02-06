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
import org.miradi.objects.ProjectMetadata;

public class PlanningTabWorkPlanSubPanelForm extends FieldPanelSpec
{
	public PlanningTabWorkPlanSubPanelForm()
	{
		setHasBorder();
		setTranslatedTitle(EAM.text("Workplan"));

		int type = ProjectMetadata.getObjectType();
		
		addLabelAndFieldsWithLabels(EAM.text("Label|Project Dates"), type, 
				new String[] {ProjectMetadata.TAG_START_DATE, ProjectMetadata.TAG_EXPECTED_END_DATE});
		addLabelAndFieldsWithLabels(EAM.text("Label|Workplan Dates"), type, 
				new String[] {ProjectMetadata.TAG_WORKPLAN_START_DATE, ProjectMetadata.TAG_WORKPLAN_END_DATE});
		
		addLabelAndField(type, ProjectMetadata.TAG_WORKPLAN_TIME_UNIT);
		addLabelAndField(type, ProjectMetadata.TAG_FISCAL_YEAR_START);
		addLabelAndField(type, ProjectMetadata.TAG_PLANNING_COMMENTS);

	}
}
