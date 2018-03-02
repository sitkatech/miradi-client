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


import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.BaseObjectSchema;

public abstract class EvidenceConfidenceTypeQuestion extends StaticChoiceQuestionSortableByCode
{
    public EvidenceConfidenceTypeQuestion(String questionDescriptionToUse)
    {
        super(questionDescriptionToUse);
    }

    @Override
    public boolean hasAdditionalText()
    {
        return true;
    }

    public static ChoiceQuestion getQuestion(int objectType)
    {
        if (objectType == ObjectType.STRATEGY)
            return StaticQuestionManager.getQuestion(StrategyEvidenceConfidenceQuestion.class);

        if (objectType == ObjectType.INDICATOR)
            return StaticQuestionManager.getQuestion(ViabilityRatingEvidenceConfidence.class);

        if (objectType == ObjectType.GOAL)
            return StaticQuestionManager.getQuestion(DesireEvidenceConfidenceQuestion.class);

        if (objectType == ObjectType.OBJECTIVE)
            return StaticQuestionManager.getQuestion(DesireEvidenceConfidenceQuestion.class);

        if (objectType == ObjectType.THREAT_REDUCTION_RESULT)
            return StaticQuestionManager.getQuestion(ResultEvidenceConfidenceQuestion.class);

        if (objectType == ObjectType.INTERMEDIATE_RESULT)
            return StaticQuestionManager.getQuestion(ResultEvidenceConfidenceQuestion.class);

        if (objectType == ObjectType.BIOPHYSICAL_RESULT)
            return StaticQuestionManager.getQuestion(ResultEvidenceConfidenceQuestion.class);

        throw new RuntimeException("Need to implement getQuestion for object type: " + objectType);
    }

    public static ChoiceQuestion getQuestion(BaseObjectSchema factorSchema)
    {
        return getQuestion(factorSchema.getType());
    }
}
