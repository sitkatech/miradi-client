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
import org.miradi.objects.Desire;
import org.miradi.objects.Goal;
import org.miradi.schemas.GoalSchema;

public class GoalPropertiesForm extends FieldPanelSpec
{
	public GoalPropertiesForm()
	{
		addLabelAndFieldsWithLabels(EAM.text("Goal"), GoalSchema.getObjectType(), new String[]{Goal.TAG_SHORT_LABEL, Goal.TAG_LABEL});
		addLabelAndField(GoalSchema.getObjectType(), Goal.TAG_FULL_TEXT);
		
		addLabelAndField(GoalSchema.getObjectType(), Goal.PSEUDO_TAG_FACTOR);
		addLabelAndField(GoalSchema.getObjectType(), Goal.PSEUDO_TAG_DIRECT_THREATS);
		addLabelAndField(GoalSchema.getObjectType(), Goal.TAG_COMMENTS);
		addLabelAndField(GoalSchema.getObjectType(), Goal.TAG_EVIDENCE_NOTES);
		addMultipleTaxonomyWithEditButtonFields(GoalSchema.getObjectType(), Goal.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
	}
}
