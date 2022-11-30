/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
        Class questionClass = getQuestionClass(objectType);
        return StaticQuestionManager.getQuestion(questionClass);
    }

    public static Class getQuestionClass(int objectType)
    {
        if (objectType == ObjectType.STRATEGY)
            return StrategyEvidenceConfidenceQuestion.class;

        if (objectType == ObjectType.INDICATOR)
            return ViabilityRatingEvidenceConfidence.class;

        if (objectType == ObjectType.MEASUREMENT)
            return MeasurementEvidenceConfidenceQuestion.class;

        if (objectType == ObjectType.GOAL)
            return DesireEvidenceConfidenceQuestion.class;

        if (objectType == ObjectType.OBJECTIVE)
            return DesireEvidenceConfidenceQuestion.class;

        if (objectType == ObjectType.THREAT_SIMPLE_RATING_DATA)
            return ThreatRatingEvidenceConfidenceQuestion.class;

        if (objectType == ObjectType.THREAT_STRESS_RATING_DATA)
            return ThreatRatingEvidenceConfidenceQuestion.class;

        if (objectType == ObjectType.ANALYTICAL_QUESTION)
            return AnalyticalQuestionEvidenceConfidenceTypeQuestion.class;

        if (objectType == ObjectType.SUB_ASSUMPTION)
            return SubAssumptionEvidenceConfidenceTypeQuestion.class;

        throw new RuntimeException("Need to implement getQuestionClass for object type: " + objectType);
    }

    public static ChoiceQuestion getQuestion(BaseObjectSchema factorSchema)
    {
        return getQuestion(factorSchema.getType());
    }
}
