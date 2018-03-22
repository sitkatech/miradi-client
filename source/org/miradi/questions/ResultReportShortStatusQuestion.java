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

public class ResultReportShortStatusQuestion extends AbstractResultReportStatusQuestion
{
    @Override
    protected String getNotKnownLabel()
    {
        return EAM.text("Not Known");
    }

    @Override
    protected String getNotYetLabel()
    {
        return EAM.text("Not Yet");
    }

    @Override
    protected String getNotAchievedLabel()
    {
        return EAM.text("Not Achieved");
    }

    @Override
    protected String getPartiallyAchievedLabel()
    {
        return EAM.text("Partially Achieved");
    }

    @Override
    protected String getOnTrackLabel()
    {
        return EAM.text("On-Track");
    }

    @Override
    protected String getAchievedLabel()
    {
        return EAM.text("Achieved");
    }

    @Override
    protected String getNoLongerRelevantLabel()
    {
        return EAM.text("No Longer Relevant");
    }
}
