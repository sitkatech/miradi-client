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

public class ResultReportLongStatusQuestion extends AbstractResultReportStatusQuestion
{
    @Override
    protected String getNotKnownLabel()
    {
        return EAM.text("Not Known - Lack of sufficient info to assess result status");
    }

    @Override
    protected String getNotYetLabel()
    {
        return EAM.text("Not Yet - Too early to expect desired result");
    }

    @Override
    protected String getNotAchievedLabel()
    {
        return EAM.text("Not Achieved - Available info shows desired result has failed to be achieved and/or undesired result produced");
    }

    @Override
    protected String getPartiallyAchievedLabel()
    {
        return EAM.text("Partially Achieved - Desired result has been only partially achieved and/or mixture of success and failure");
    }

    @Override
    protected String getOnTrackLabel()
    {
        return EAM.text("On-Track - Desired result likely to be successfully achieved");
    }

    @Override
    protected String getAchievedLabel()
    {
        return EAM.text("Achieved - Desired result has been successfully achieved");
    }

    @Override
    protected String getNoLongerRelevantLabel()
    {
        return EAM.text("No Longer Relevant - Result is now assumed to be not achievable and/or not necessary");
    }
}
