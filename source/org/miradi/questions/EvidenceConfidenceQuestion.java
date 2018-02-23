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
package org.miradi.questions;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.BaseObjectSchema;


public class EvidenceConfidenceQuestion extends StaticChoiceQuestionSortableByCode
{
    public EvidenceConfidenceQuestion()
    {
        super(EAM.text("To track the confidence of your evidence, use the following scale:"));
    }

    public static ChoiceQuestion getQuestion(BaseObjectSchema factorSchema)
    {
        if (factorSchema.getType() == ObjectType.STRATEGY)
            return StaticQuestionManager.getQuestion(StrategyEvidenceConfidenceQuestion.class);

        return StaticQuestionManager.getQuestion(EvidenceConfidenceQuestion.class);
    }

    @Override
    protected ChoiceItem[] createChoices()
    {
        return new ChoiceItem[] {
                new ChoiceItemWithLongDescriptionProvider(NOT_SPECIFIED_CODE, EAM.text("Not Specified")),
                new ChoiceItemWithLongDescriptionProvider(ROUGH_GUESS_CODE, EAM.text("Rough Guess"), getRoughGuessChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(EXPERT_KNOWLEDGE_CODE, EAM.text("Expert Knowledge"), getExpertKnowledgeChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(EXTERNAL_RESEARCH_CODE, EAM.text("External Research"), getExternalResearchChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(ONSITE_RESEARCH_CODE, EAM.text("Onsite / Project Research"), getOnsiteResearchChoiceItemDescription()),
        };
    }

    @Override
    public boolean hasAdditionalText()
    {
        return true;
    }

    protected String getRoughGuessChoiceItemDescription()
    {
        return EAM.text("Based on estimates by project team members or other individuals without specialized knowledge; often an initial estimate to be improved over time.");
    }

    protected String getExpertKnowledgeChoiceItemDescription()
    {
        return EAM.text("Based on estimates by experienced observers who have the expertise to provide reliable information in the project context and/or based on other similar project sites.");
    }

    protected String getExternalResearchChoiceItemDescription()
    {
        return EAM.text("Based on research at other comparable project sites, often obtained from published or unpublished scientific literature.");
    }

    protected String getOnsiteResearchChoiceItemDescription()
    {
        return EAM.text("Based on research at the project site including adaptive management by the project team.");
    }

    public static final String NOT_SPECIFIED_CODE = "";
    public static final String ROUGH_GUESS_CODE = "RoughGuess";
    public static final String EXPERT_KNOWLEDGE_CODE = "ExpertKnowledge";
    public static final String EXTERNAL_RESEARCH_CODE = "ExternalResearch";
    public static final String ONSITE_RESEARCH_CODE = "OnsiteResearch";
}
