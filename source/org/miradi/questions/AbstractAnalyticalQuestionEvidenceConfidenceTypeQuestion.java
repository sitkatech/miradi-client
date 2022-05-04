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

import org.miradi.main.EAM;


public abstract class AbstractAnalyticalQuestionEvidenceConfidenceTypeQuestion extends EvidenceConfidenceTypeQuestion
{
    public AbstractAnalyticalQuestionEvidenceConfidenceTypeQuestion()
    {
        super(EAM.text("To track the confidence of your evidence, use the following scale:"));
    }

    @Override
    protected ChoiceItem[] createChoices()
    {
        return new ChoiceItem[] {
                new ChoiceItemWithLongDescriptionProvider(NOT_SPECIFIED, EAM.text("Not Specified")),
                new ChoiceItemWithLongDescriptionProvider(VERY_CONFIDENT_CODE, EAM.text("Very Confident"), getVeryConfidentChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(CONFIDENT_CODE, EAM.text("Confident"), getConfidentChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(NEED_MORE_INFO_CODE, EAM.text("Need More Info"), getNeedMoreInfoChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(NOT_CONFIDENT_CODE, EAM.text("Not Confident"), getNotConfidentChoiceItemDescription()),
        };
    }

    protected abstract String getNotConfidentChoiceItemDescription();

    protected abstract String getNeedMoreInfoChoiceItemDescription();

    protected abstract String getConfidentChoiceItemDescription();

    protected abstract String getVeryConfidentChoiceItemDescription();

    public static final String NOT_SPECIFIED = "";
    public static final String NOT_CONFIDENT_CODE = "NotConfident";
    public static final String NEED_MORE_INFO_CODE = "NeedMoreInfo";
    public static final String CONFIDENT_CODE = "Confident";
    public static final String VERY_CONFIDENT_CODE = "VeryConfident";


}
