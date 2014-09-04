/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

package org.miradi.project.threatrating;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.ProjectInterface;
import org.miradi.schemas.RatingCriterionSchema;
import org.miradi.schemas.ValueOptionSchema;
import org.miradi.utils.EnhancedJsonObject;

public class SimpleThreatFrameworkJson
{
	public static EnhancedJsonObject toJson(ProjectInterface project)
	{
		Collection<ThreatRatingBundle> allBundles = project.getSimpleThreatRatingBundles();
		ORefList valueOptionRefs = project.getAllRefsForType(ValueOptionSchema.getObjectType());
		ORefList ratingCriterionRefs = project.getAllRefsForType(RatingCriterionSchema.getObjectType());
		
		final IdList valueOptionIds = valueOptionRefs.convertToIdList(ValueOptionSchema.getObjectType());
		final IdList ratingCriterionIds = ratingCriterionRefs.convertToIdList(RatingCriterionSchema.getObjectType());
		return toJson(allBundles, valueOptionIds, ratingCriterionIds);
	}

	public static EnhancedJsonObject toJson(final Collection<ThreatRatingBundle> bundlesToUse,	final IdList valueOptionIds, final IdList criterionIds)
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		JSONArray bundleKeys = new JSONArray();
		for (ThreatRatingBundle bundle : bundlesToUse)
		{
			JSONObject pair = new JSONObject();
			pair.put(TAG_BUNDLE_THREAT_ID, bundle.getThreatId());
			pair.put(TAG_BUNDLE_TARGET_ID, bundle.getTargetId());
			bundleKeys.put(pair);
		}
		json.put(TAG_BUNDLE_KEYS, bundleKeys);
		json.put(TAG_VALUE_OPTION_IDS, valueOptionIds.toJson());
		json.put(TAG_CRITERION_IDS, criterionIds.toJson());
		return json;
	}
	
	public static final String TAG_BUNDLE_KEYS = "BundleKeys";
	public static final String TAG_VALUE_OPTION_IDS = "ValueOptionIds";
	public static final String TAG_CRITERION_IDS = "CriterionIds";
	public static final String TAG_BUNDLE_THREAT_ID = "BundleThreatId";
	public static final String TAG_BUNDLE_TARGET_ID = "BundleTargetId";
}
