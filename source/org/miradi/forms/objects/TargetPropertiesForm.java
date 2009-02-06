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
import org.miradi.icons.TargetIcon;
import org.miradi.main.EAM;
import org.miradi.objects.Target;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ViabilityModeQuestion;

public class TargetPropertiesForm extends FieldPanelSpec
{
	public TargetPropertiesForm()
	{
		int type = Target.getObjectType();
		addStandardNameRow(new TargetIcon(), EAM.text("Target"), type, new String[]{Target.TAG_SHORT_LABEL, Target.TAG_LABEL});
		addChoiceField(type, Target.TAG_VIABILITY_MODE, new ViabilityModeQuestion());
		addChoiceField(type, Target.TAG_TARGET_STATUS, new StatusQuestion());
		addLabelAndField(type, Target.TAG_CURRENT_STATUS_JUSTIFICATION);
		addLabelAndField(Target.getObjectType(), Target.TAG_SPECIES_LATIN_NAME);
		addLabelAndField(Target.getObjectType(), Target.TAG_HABITAT_ASSOCIATION);
		addLabelAndField(Target.getObjectType(), Target.TAG_TEXT);
		addLabelAndField(Target.getObjectType(), Target.TAG_COMMENT);
	}
}
