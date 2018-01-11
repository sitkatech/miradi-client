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

package org.miradi.migrations;

import java.util.HashMap;
import java.util.Set;

public class IndicatorFutureStatusTagsToFutureStatusTagsMap extends HashMap<String, String>
{
	public IndicatorFutureStatusTagsToFutureStatusTagsMap()
	{
		fillMap();
	}

	private void fillMap()
	{
		put(TAG_INDICATOR_FUTURE_STATUS_COMMENTS, TAG_FUTURE_STATUS_COMMENTS);
		put(TAG_INDICATOR_FUTURE_STATUS_DATE, TAG_FUTURE_STATUS_DATE);
		put(TAG_INDICATOR_FUTURE_STATUS_DETAIL, TAG_FUTURE_STATUS_DETAIL);
		put(TAG_INDICATOR_FUTURE_STATUS_RATING, TAG_FUTURE_STATUS_RATING);
		put(TAG_INDICATOR_FUTURE_STATUS_SUMMARY, TAG_FUTURE_STATUS_SUMMARY);
	}
	
	public Set<String> getIndicatorFutureStatusTags()
	{
		return keySet();
	}
	
	public static final String TAG_INDICATOR_FUTURE_STATUS_RATING  = "FutureStatusRating";
	public static final String TAG_INDICATOR_FUTURE_STATUS_DATE = "FutureStatusDate";
	public static final String TAG_INDICATOR_FUTURE_STATUS_SUMMARY = "FutureStatusSummary";
	public static final String TAG_INDICATOR_FUTURE_STATUS_DETAIL = "FutureStatusDetail";
	public static final String TAG_INDICATOR_FUTURE_STATUS_COMMENTS = "FutureStatusComment";
	
	private static final String TAG_FUTURE_STATUS_RATING  = "Rating";
	private static final String TAG_FUTURE_STATUS_DATE = "Date";
	private static final String TAG_FUTURE_STATUS_SUMMARY = "Summary";
	private static final String TAG_FUTURE_STATUS_DETAIL = "Detail";
	private static final String TAG_FUTURE_STATUS_COMMENTS = "Comment";
}
