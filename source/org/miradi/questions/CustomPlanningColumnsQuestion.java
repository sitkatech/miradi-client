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
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.utils.BiDirectionalHashMap;

public class CustomPlanningColumnsQuestion extends MultipleSelectStaticChoiceQuestion
{
	public CustomPlanningColumnsQuestion()
	{
		super(getColumnChoiceItems());
		
		fillInternalToReadableCodeMap();
	}

	private void fillInternalToReadableCodeMap()
	{
		internalToReadableCodeMap = new BiDirectionalHashMap();
		internalToReadableCodeMap.put(Strategy.PSEUDO_TAG_ACTIVITIES, READABLE_ACTIVITIES_CODE);
		internalToReadableCodeMap.put(Factor.PSEUDO_TAG_INDICATORS, READABLE_INDICATORS);
		internalToReadableCodeMap.put(Indicator.PSEUDO_TAG_FACTOR, READABLE_ASSOCIATED_FACTOR_CODE);
		internalToReadableCodeMap.put(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE, "LatestProjectReport");
		internalToReadableCodeMap.put(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS, "LatestProgressReport");
		internalToReadableCodeMap.put(META_ASSIGNED_WHO_TOTAL, READABLE_ASSIGNED_WHO_TOTAL_CODE);
		internalToReadableCodeMap.put(META_TIMEFRAME_TOTAL, READABLE_TIMEFRAME_TOTAL_CODE);
		internalToReadableCodeMap.put(Measurement.META_COLUMN_TAG, "Measurements");
		internalToReadableCodeMap.put(Indicator.META_COLUMN_TAG, "FutureStatuses");
		internalToReadableCodeMap.put(Desire.TAG_FULL_TEXT, "Details");
	}
	
	@Override
	public String convertToReadableCode(String internalCode)
	{
		if (internalToReadableCodeMap.containsKey(internalCode))
			return internalToReadableCodeMap.getValue(internalCode);
		
		return super.convertToReadableCode(internalCode);
	}

	@Override
	public String convertToInternalCode(String readableCode)
	{
		if (internalToReadableCodeMap.containsValue(readableCode))
			return internalToReadableCodeMap.getKey(readableCode);
		
		return super.convertToInternalCode(readableCode);
	}

	private static ChoiceItem[] getColumnChoiceItems()
	{
		return new ChoiceItem[] 
		{
				createChoiceItem(Target.TAG_SPECIES_LATIN_NAME),
				createChoiceItem(META_CURRENT_RATING),
				createChoiceItem(Indicator.PSEUDO_TAG_METHODS),
				createChoiceItem(Strategy.PSEUDO_TAG_ACTIVITIES),
				createChoiceItem(Factor.PSEUDO_TAG_INDICATORS),
				createChoiceItem(Indicator.PSEUDO_TAG_FACTOR),
				createChoiceItem(Indicator.TAG_PRIORITY),
				createChoiceItem(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE),
				createChoiceItem(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS),
				createChoiceItem(Factor.PSEUDO_TAG_TAXONOMY_CODE_VALUE),
				createChoiceItem(META_ASSIGNED_WHO_TOTAL, READABLE_ASSIGNED_WHO_TOTAL_CODE),
				createChoiceItem(META_TIMEFRAME_TOTAL),
				createChoiceItem(Measurement.META_COLUMN_TAG),
				createChoiceItem(Indicator.META_COLUMN_TAG),
				createChoiceItem(BaseObject.TAG_COMMENTS),
				createChoiceItem(Desire.TAG_FULL_TEXT),
				createChoiceItem(BaseObject.TAG_EVIDENCE_NOTES),
				createChoiceItem(BaseObject.TAG_EVIDENCE_CONFIDENCE),
		};
	}

	public static ChoiceItem createChoiceItem(String tag)
	{
		return new ChoiceItem(tag, EAM.fieldLabel(ObjectType.FAKE, tag));
	}

	public static ChoiceItem createChoiceItem(String tag, String customLabel)
	{
		return new ChoiceItem(tag, EAM.fieldLabel(ObjectType.FAKE, customLabel));
	}

	public final static String META_TIMEFRAME_TOTAL = BaseObject.PSEUDO_TAG_TIMEFRAME_TOTAL;
	public final static String META_ASSIGNED_WHO_TOTAL = "MetaAssignedWhoTotal";
	public final static String META_ASSIGNED_WHEN_TOTAL = BaseObject.PSEUDO_TAG_ASSIGNED_WHEN_TOTAL;
	public final static String META_CURRENT_RATING = "ThreatRating";

	private BiDirectionalHashMap internalToReadableCodeMap;
	private final static String READABLE_INDICATORS = "Indicators";
	private final static String READABLE_ACTIVITIES_CODE = "Activities";
	private final static String READABLE_ASSOCIATED_FACTOR_CODE = "AssociatedFactor";
	private final static String READABLE_ASSIGNED_WHO_TOTAL_CODE = "AssignedWhoTotal";
	private final static String READABLE_TIMEFRAME_TOTAL_CODE = "TimeframeTotal";
}
