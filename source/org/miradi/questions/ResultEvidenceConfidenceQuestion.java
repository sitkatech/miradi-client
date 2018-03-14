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


public class ResultEvidenceConfidenceQuestion extends EvidenceConfidenceTypeProjectQuestion
{
    public ResultEvidenceConfidenceQuestion()
    {
        super(EAM.text("To track the confidence of the result evidence, use the following scale:"));
    }

    protected String getRoughGuessChoiceItemDescription()
    {
        return EAM.text("Result status based on estimates by project team members or other individuals without specialized knowledge; often an initial estimate to be improved over time.");
    }

    protected String getExpertKnowledgeChoiceItemDescription()
    {
        return EAM.text("Result status based on estimates by experienced observers who have the expertise to provide reliable information in the project context and/or by extrapolating from other similar project sites.");
    }

    protected String getRapidAssessmentChoiceItemDescription()
    {
        return EAM.text("Result status derived from data collected about this specific project site using relatively simple methods that typically provide either presence/absence, categorical, or other basic qualitative data.");
    }

    protected String getIntensiveAssessmentChoiceItemDescription()
    {
        return EAM.text("Result status derived from data collected about this specific project site using relatively systematic methods that typically provide detailed quantitative or qualitative data, and often include a formal estimate of statistical precision.");
    }
}
