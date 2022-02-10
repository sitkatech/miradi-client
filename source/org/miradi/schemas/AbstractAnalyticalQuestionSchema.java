/*
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

package org.miradi.schemas;

import org.miradi.objects.AbstractAnalyticalQuestion;
import org.miradi.objects.BaseObject;
import org.miradi.questions.EvidenceConfidenceTypeQuestion;

abstract public class AbstractAnalyticalQuestionSchema extends FactorSchema
{
    public AbstractAnalyticalQuestionSchema()
    {
        super();
    }

    @Override
    protected void fillFieldSchemas()
    {
        super.fillFieldSchemas();

		createFieldSchemaMultiLineUserText(BaseObject.TAG_EVIDENCE_NOTES);
		createFieldSchemaChoice(BaseObject.TAG_EVIDENCE_CONFIDENCE, EvidenceConfidenceTypeQuestion.getQuestionClass(getType()));

        createFieldSchemaMultiLineUserText(AbstractAnalyticalQuestion.TAG_IMPLICATIONS);
        createFieldSchemaMultiLineUserText(AbstractAnalyticalQuestion.TAG_FUTURE_INFORMATION_NEEDS);
        createFieldSchemaRelevancyOverrideSet(AbstractAnalyticalQuestion.TAG_DIAGRAM_FACTOR_IDS);
        createFieldSchemaRelevancyOverrideSet(AbstractAnalyticalQuestion.TAG_INDICATOR_IDS);
		createPseudoFieldSchemaRefList(AbstractAnalyticalQuestion.PSEUDO_TAG_RELEVANT_INDICATOR_REFS);

        createTaxonomyClassificationSchemaField();
    }
}
