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

package org.miradi.objects;

import org.miradi.ids.FactorId;
import org.miradi.project.ObjectManager;
import org.miradi.schemas.BaseObjectSchema;

abstract public class AbstractAnalyticalQuestion extends Factor
{
    public AbstractAnalyticalQuestion(ObjectManager objectManager, FactorId idToUse, final BaseObjectSchema schemaToUse)
    {
        super(objectManager, idToUse, schemaToUse);
    }

    @Override
    public int[] getTypesThatCanOwnUs()
    {
        return NO_OWNERS;
    }

    @Override
    public String getDetails()
    {
        return getData(TAG_DETAILS);
    }

    @Override
    public String toString()
    {
        return getLabel();
    }

	public static final String TAG_SHORT_LABEL = "ShortLabel";
    public final static String TAG_DETAILS = "Details";
    public static final String TAG_FUTURE_INFORMATION_NEEDS = "FutureInformationNeeds";

    public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
    public static final String TAG_INDICATOR_IDS = "IndicatorIds";
}
