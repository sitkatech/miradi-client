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


public class StrategyEvidenceConfidenceQuestion extends EvidenceConfidenceTypeExternalQuestion
{
    public StrategyEvidenceConfidenceQuestion()
    {
        super();
    }

    @Override
    protected String getRoughGuessChoiceItemDescription()
    {
        return EAM.text("Strategy is recommended by project team members or other individuals without specialized knowledge.");
    }

    @Override
    protected String getExpertKnowledgeChoiceItemDescription()
    {
        return EAM.text("Strategy is recommended by experienced observers who have the expertise to provide reliable information in the project context and/or based on other similar project sites.");
    }

    @Override
    protected String getExternalResearchChoiceItemDescription()
    {
        return EAM.text("Strategy is recommended based on research at other comparable project sites, often obtained from published or unpublished scientific literature.");
    }

    @Override
    protected String getOnsiteResearchChoiceItemDescription()
    {
        return EAM.text("Strategy is recommended based on research at the project site including adaptive management by the project team.");
    }
}
