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
package org.miradi.forms.objects;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.main.EAM;
import org.miradi.objects.Desire;
import org.miradi.objects.Goal;
import org.miradi.objects.Objective;

public class ObjectivePropertiesForm extends FieldPanelSpec
{
	public ObjectivePropertiesForm()
	{
		int type = Objective.getObjectType();
		addLabelAndFieldsWithLabels(EAM.text("Objective"), type, new String[]{Objective.TAG_SHORT_LABEL, Objective.TAG_LABEL});
		addLabelAndField(type, Desire.TAG_FULL_TEXT);
		addLabelAndField(type, Objective.PSEUDO_TAG_FACTOR);
		addLabelAndField(type, Objective.PSEUDO_TAG_DIRECT_THREATS);
		addLabelAndField(type, Objective.PSEUDO_TAG_TARGETS);
		addLabelAndField(type, Objective.PSEUDO_TAG_RELEVANT_INDICATOR_REFS);
		addLabelAndField(type, Objective.PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS);
		addLabelAndFieldsWithLabels(EAM.text("Progress Percents"), type, new String[]{Objective.TAG_PROGRESS_PERCENT_REFS});
		addLabelAndField(type, Goal.TAG_COMMENTS);
	}
}
