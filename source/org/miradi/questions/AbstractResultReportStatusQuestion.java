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

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

import java.awt.*;

abstract public class AbstractResultReportStatusQuestion extends StaticChoiceQuestionSortableByNaturalOrder
{
    public AbstractResultReportStatusQuestion()
    {
        super();
    }

    @Override
    protected ChoiceItem[] createChoices()
    {
        return new ChoiceItem[] {
                new ChoiceItem(NOT_SPECIFIED, getNotSpecifiedLabel(), Color.WHITE),
                new ChoiceItemWithDynamicColor(NOT_KNOWN_CODE, getNotKnownLabel(), AppPreferences.TAG_COLOR_NOT_KNOWN),
                new ChoiceItemWithDynamicColor(NOT_YET_CODE, getNotYetLabel(), AppPreferences.TAG_COLOR_PLANNED),
                new ChoiceItemWithDynamicColor(NOT_ACHIEVED_CODE, getNotAchievedLabel(), AppPreferences.TAG_COLOR_ALERT),
                new ChoiceItemWithDynamicColor(PARTIALLY_ACHIEVED_CODE, getPartiallyAchievedLabel(), AppPreferences.TAG_COLOR_CAUTION),
                new ChoiceItemWithDynamicColor(ON_TRACK_CODE, getOnTrackLabel(), AppPreferences.TAG_COLOR_OK),
                new ChoiceItemWithDynamicColor(ACHIEVED_CODE, getAchievedLabel(), AppPreferences.TAG_COLOR_GREAT),
                new ChoiceItemWithDynamicColor(NO_LONGER_RELEVANT_CODE, getNoLongerRelevantLabel(), AppPreferences.TAG_COLOR_ABANDONED),
        };
    }

    protected String getNotSpecifiedLabel()
    {
        return EAM.text("Not Specified");
    }

    abstract protected String getNotKnownLabel();

    abstract protected String getNotYetLabel();

    abstract protected String getNotAchievedLabel();

    abstract protected String getPartiallyAchievedLabel();

    abstract protected String getOnTrackLabel();

    abstract protected String getAchievedLabel();

    abstract protected String getNoLongerRelevantLabel();

    public static final String NOT_SPECIFIED = "";
    public static final String NOT_KNOWN_CODE = "NotKnown";
    public static final String NOT_YET_CODE = "NotYet";
    public static final String NOT_ACHIEVED_CODE = "NotAchieved";
    public static final String PARTIALLY_ACHIEVED_CODE = "MinorIssues";
    public static final String ON_TRACK_CODE = "OnTrack";
    public static final String ACHIEVED_CODE = "Achieved";
    public static final String NO_LONGER_RELEVANT_CODE = "NoLongerRelevant";
}
