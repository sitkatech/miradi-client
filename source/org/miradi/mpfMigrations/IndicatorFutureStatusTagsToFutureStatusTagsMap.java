/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

package org.miradi.mpfMigrations;

import java.util.HashMap;
import java.util.Set;

import org.miradi.objects.Indicator;
import org.miradi.schemas.FutureStatusSchema;

public class IndicatorFutureStatusTagsToFutureStatusTagsMap extends HashMap<String, String>
{
	public IndicatorFutureStatusTagsToFutureStatusTagsMap()
	{
		fillMap();
	}

	private void fillMap()
	{
		put(Indicator.TAG_FUTURE_STATUS_COMMENTS, FutureStatusSchema.TAG_FUTURE_STATUS_COMMENTS);
		put(Indicator.TAG_FUTURE_STATUS_DATE, FutureStatusSchema.TAG_FUTURE_STATUS_DATE);
		put(Indicator.TAG_FUTURE_STATUS_DETAIL, FutureStatusSchema.TAG_FUTURE_STATUS_DETAIL);
		put(Indicator.TAG_FUTURE_STATUS_RATING, FutureStatusSchema.TAG_FUTURE_STATUS_RATING);
		put(Indicator.TAG_FUTURE_STATUS_SUMMARY, FutureStatusSchema.TAG_FUTURE_STATUS_SUMMARY);
	}
	
	public Set<String> getIndicatorFutureStatusTags()
	{
		return keySet();
	}
}
