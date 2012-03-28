/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.schemas;

import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Goal;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ViabilityModeQuestion;

abstract public class AbstractTargetSchema extends FactorSchema
{
	public AbstractTargetSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaChoice(AbstractTarget.TAG_TARGET_STATUS, getQuestion(StatusQuestion.class));
		createFieldSchemaChoice(AbstractTarget.TAG_VIABILITY_MODE, getQuestion(ViabilityModeQuestion.class));
		createFieldSchemaMultiLineUserText(AbstractTarget.TAG_CURRENT_STATUS_JUSTIFICATION);
		createFieldSchemaReflist(AbstractTarget.TAG_SUB_TARGET_REFS);
		createFieldSchemaIdList(AbstractTarget.TAG_GOAL_IDS, Goal.getObjectType());
		createFieldSchemaIdList(AbstractTarget.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, KeyEcologicalAttribute.getObjectType());

		createPseudoString(AbstractTarget.PSEUDO_TAG_TARGET_VIABILITY);
		createPseudoQuestionField(AbstractTarget.PSEUDO_TAG_TARGET_STATUS_VALUE);
		createPseudoQuestionField(AbstractTarget.PSEUDO_TAG_VIABILITY_MODE_VALUE);
	}
}
