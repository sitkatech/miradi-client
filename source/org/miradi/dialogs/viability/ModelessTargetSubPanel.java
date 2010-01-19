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
package org.miradi.dialogs.viability;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.HabitatAssociationQuestion;

public class ModelessTargetSubPanel extends ObjectDataInputPanel
{
	public ModelessTargetSubPanel(Project projectToUse, int targetType)
	{
		super(projectToUse, targetType);
		
		addField(createMultilineField(targetType, AbstractTarget.TAG_CURRENT_STATUS_JUSTIFICATION));
		
		if (Target.is(targetType))
		{
			addField(createStringField(Target.getObjectType(), Target.TAG_SPECIES_LATIN_NAME));
			addField(createEditableCodeListField(Target.getObjectType(), Target.TAG_HABITAT_ASSOCIATION, getProject().getQuestion(HabitatAssociationQuestion.class), 1));
		}
		
		addField(createReadOnlyObjectList(targetType, AbstractTarget.PSEUDO_TAG_CONCEPTUAL_DIAGRAM_REFS));
		addField(createReadOnlyObjectList(targetType, AbstractTarget.PSEUDO_TAG_RESULTS_CHAIN_REFS));

		addField(createMultilineField(targetType, AbstractTarget.TAG_TEXT));
		addField(createMultilineField(targetType, AbstractTarget.TAG_COMMENTS));

		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Details");
	}
}
