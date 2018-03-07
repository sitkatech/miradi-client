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


public abstract class EvidenceConfidenceTypeProjectQuestion extends EvidenceConfidenceTypeQuestion
{
    public EvidenceConfidenceTypeProjectQuestion(String questionDescriptionToUse)
    {
        super(questionDescriptionToUse);
    }

    @Override
    protected ChoiceItem[] createChoices()
    {
        return new ChoiceItem[] {
                new ChoiceItemWithLongDescriptionProvider(NOT_SPECIFIED, EAM.text("Not Specified")),
                new ChoiceItemWithLongDescriptionProvider(ROUGH_GUESS_CODE, EAM.text("Rough Guess"), getRoughGuessChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(EXPERT_KNOWLEDGE_CODE, EAM.text("Expert Knowledge"), getExpertKnowledgeChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(RAPID_ASSESSMENT_CODE, EAM.text("Rapid Assessment"), getRapidAssessmentChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(INTENSIVE_ASSESSMENT_CODE, EAM.text("Intensive Assessment"), getIntensiveAssessmentChoiceItemDescription()),
        };
    }

    protected abstract String getRoughGuessChoiceItemDescription();

    protected abstract String getExpertKnowledgeChoiceItemDescription();

    protected abstract String getRapidAssessmentChoiceItemDescription();

    protected abstract String getIntensiveAssessmentChoiceItemDescription();

    public static final String NOT_SPECIFIED = "";
    public static final String ROUGH_GUESS_CODE = "RoughGuess";
    public static final String EXPERT_KNOWLEDGE_CODE = "ExpertKnowledge";
    public static final String RAPID_ASSESSMENT_CODE = "RapidAssessment";
    public static final String INTENSIVE_ASSESSMENT_CODE = "IntensiveAssessment";
}
