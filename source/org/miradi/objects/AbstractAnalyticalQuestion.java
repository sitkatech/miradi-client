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

package org.miradi.objects;

import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.project.ObjectManager;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.IndicatorSchema;

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
    public String toString()
    {
        return getLabel();
    }

	@Override
	public boolean canDirectlyOwnIndicators()
	{
		return false;
	}

	@Override
	protected RelevancyOverrideSet getIndicatorRelevancyOverrideSet()
	{
		return getRawRelevancyOverrideData(TAG_INDICATOR_IDS);
	}

	@Override
	public boolean isRelevancyOverrideSet(String tag)
	{
		if (tag.equals(AbstractAnalyticalQuestion.TAG_INDICATOR_IDS))
			return true;

		return false;
	}

	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
			return getRelevantIndicatorRefsAsString();

		return super.getPseudoData(fieldTag);
	}

	protected String getRelevantIndicatorRefsAsString()
	{
		ORefList refList;
		try
		{
			refList = getRelevantIndicatorRefList();
			return refList.toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	@Override
	public IdList getOnlyDirectIndicatorIds()
	{
		return new IdList(IndicatorSchema.getObjectType());
	}

	public static final String TAG_SHORT_LABEL = "ShortLabel";
    public static final String TAG_IMPLICATIONS = "Implications";
    public static final String TAG_FUTURE_INFORMATION_NEEDS = "FutureInformationNeeds";

    public static final String TAG_INDICATOR_IDS = "IndicatorIds";

	public static final String PSEUDO_TAG_RELEVANT_INDICATOR_REFS = "PseudoRelevantIndicatorRefs";
}
