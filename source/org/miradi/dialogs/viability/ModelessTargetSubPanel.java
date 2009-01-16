/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.HabitatAssociationQuestion;

public class ModelessTargetSubPanel extends ObjectDataInputPanel
{
	public ModelessTargetSubPanel(Project projectToUse)
	{
		super(projectToUse, Target.getObjectType());
		
		addField(createStringField(Target.TAG_CURRENT_STATUS_JUSTIFICATION));
		
		addField(createStringField(Target.getObjectType(), Target.TAG_SPECIES_LATIN_NAME));
		addField(createCodeListField(Target.getObjectType(), Target.TAG_HABITAT_ASSOCIATION, getProject().getQuestion(HabitatAssociationQuestion.class), 1));
		addField(createReadOnlyObjectList(Target.getObjectType(), Factor.PSEUDO_TAG_DIAGRAM_REFS));
		addField(createReadOnlyObjectList(Target.getObjectType(), Factor.PSEUDO_TAG_RESULTS_CHAIN_REFS));

		addField(createMultilineField(Target.getObjectType(), Factor.TAG_TEXT));
		addField(createMultilineField(Target.getObjectType(), Factor.TAG_COMMENT));

		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return null;
	}

}
