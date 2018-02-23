/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objects.Objective;
import org.miradi.schemas.ObjectiveSchema;

public class ObjectivePropertiesForm extends FieldPanelSpec
{
	public ObjectivePropertiesForm()
	{
		int type = ObjectiveSchema.getObjectType();
		addLabelAndFieldsWithLabels(EAM.text("Objective"), type, new String[]{Objective.TAG_SHORT_LABEL, Objective.TAG_LABEL});
		addLabelAndField(type, Objective.TAG_FULL_TEXT);
		addLabelAndField(type, Objective.PSEUDO_TAG_FACTOR);
		addLabelAndField(type, Objective.PSEUDO_TAG_DIRECT_THREATS);
		addLabelAndField(type, Objective.PSEUDO_TAG_TARGETS);
		addLabelAndField(type, Objective.PSEUDO_TAG_RELEVANT_INDICATOR_REFS);
		addLabelAndField(type, Objective.PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS);
		addLabelAndFieldsWithLabels(EAM.text("Progress Percents"), type, new String[]{Objective.TAG_PROGRESS_PERCENT_REFS});
		addLabelAndField(type, Objective.TAG_COMMENTS);
		addLabelAndField(type, Objective.TAG_EVIDENCE_CONFIDENCE);
		addLabelAndField(type, Objective.TAG_EVIDENCE_NOTES);
		addMultipleTaxonomyWithEditButtonFields(ObjectiveSchema.getObjectType(), Objective.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
	}
}
