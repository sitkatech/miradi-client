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


public class AnalyticalQuestionEvidenceConfidenceTypeQuestion extends AbstractAnalyticalQuestionEvidenceConfidenceTypeQuestion
{
    public AnalyticalQuestionEvidenceConfidenceTypeQuestion()
    {
        super();
    }

    @Override
    protected String getNotConfidentChoiceItemDescription()
    {
        return EAM.text(" Existing evidence refutes underlying assumption(s); consider alternatives .");
    }

    @Override
    protected String getNeedMoreInfoChoiceItemDescription()
    {
        return EAM.text("Existing evidence insufficient and/or mixed support for underlying assumption(s); find additional evidence, conduct research, and/or cautiously proceed with full monitoring.");
    }

    @Override
    protected String getConfidentChoiceItemDescription()
    {
        return EAM.text("Existing evidence supports underlying assumption(s); proceed with monitoring focusing on evidence gaps.");
    }

    @Override
    protected String getVeryConfidentChoiceItemDescription()
    {
        return EAM.text("Existing evidence strongly supports underlying assumption(s); proceed with verification monitoring.");
    }
}
