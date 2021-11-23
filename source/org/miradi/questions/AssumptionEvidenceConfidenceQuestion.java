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
package org.miradi.questions;

import org.miradi.main.EAM;

public class AssumptionEvidenceConfidenceQuestion extends EvidenceConfidenceTypeQuestion
{
    public AssumptionEvidenceConfidenceQuestion()
    {
        super(EAM.text("To track the confidence of your evidence, use the following scale:"));
    }

    @Override
    protected ChoiceItem[] createChoices()
    {
        return new ChoiceItem[] {
                new ChoiceItemWithLongDescriptionProvider(NOT_SPECIFIED_CODE, EAM.text("Not Specified")),
                new ChoiceItemWithLongDescriptionProvider(NO_MORE_INFO_NEEDED_CODE, EAM.text("No More Info Needed"), getNoMoreInfoNeededChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(VERY_CONFIDENT_CODE, EAM.text("Very Confident"), getVeryConfidentChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(CONFIDENT_CODE, EAM.text("Confident, But..."), getConfidentChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(NEED_MORE_INFO_CODE, EAM.text("Need More Info"), getNeedMoreInfoChoiceItemDescription()),
                new ChoiceItemWithLongDescriptionProvider(NOT_CONFIDENT_CODE, EAM.text("Not Confident"), getNotConfidentChoiceItemDescription()),
        };
    }

    protected String getNoMoreInfoNeededChoiceItemDescription()
    {
        return EAM.text("Proceed with pathway & little or no monitoring.");
    }

    protected String getVeryConfidentChoiceItemDescription()
    {
        return EAM.text("Proceed with pathway & verification monitoring.");
    }

    protected String getConfidentChoiceItemDescription()
    {
        return EAM.text("Proceed with pathway & basic monitoring.");
    }

    protected String getNeedMoreInfoChoiceItemDescription()
    {
        return EAM.text("Do research or proceed with pathway & full monitoring.");
    }

    protected String getNotConfidentChoiceItemDescription()
    {
        return EAM.text("Consider alternative pathways.");
    }

    public static final String NOT_SPECIFIED_CODE = "";
    public static final String NO_MORE_INFO_NEEDED_CODE = "NoMoreInfoNeeded";
    public static final String VERY_CONFIDENT_CODE = "VeryConfident";
    public static final String CONFIDENT_CODE = "Confident";
    public static final String NEED_MORE_INFO_CODE = "NeedMoreInfo";
    public static final String NOT_CONFIDENT_CODE = "NotConfident";
}
