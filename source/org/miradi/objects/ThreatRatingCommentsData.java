/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.ThreatRatingCommentsDataSchema;
import org.miradi.utils.EnhancedJsonObject;

public class ThreatRatingCommentsData extends BaseObject
{
	public ThreatRatingCommentsData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, new ThreatRatingCommentsDataSchema());
	}

	public ThreatRatingCommentsData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject, new ThreatRatingCommentsDataSchema());
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public String findComment(ORef threatRef, ORef targetRef)
	{
		String threatTargetRefsAsKey = createKey(threatRef, targetRef);
		if (getProject().isSimpleThreatRatingMode())
			return getSimpleThreatRatingCommentsMap().getUserString(threatTargetRefsAsKey);
		
		return getStressBasedThreatRatingCommentsMap().getUserString(threatTargetRefsAsKey);
	}
	
	public static String createKey(ORef threatRef, ORef targetRef)
	{
		return threatRef.toString() + targetRef.toString();
	}
	
	public CodeToUserStringMap getThreatRatingCommentsMap()
	{
		if (getProject().isSimpleThreatRatingMode())
			return getSimpleThreatRatingCommentsMap();
		
		return getStressBasedThreatRatingCommentsMap();
	}
	
	public String getThreatRatingCommentsMapTag()
	{
		if (getProject().isSimpleThreatRatingMode())
			return TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP;
		
		return TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP;
	}
	
	public CodeToUserStringMap getThreatRatingCommentsMap(String tag)
	{
		if (tag.equals(TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP))
			return getSimpleThreatRatingCommentsMap();
		
		if (tag.equals(TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP))
			return getStressBasedThreatRatingCommentsMap();
		
		throw new RuntimeException("Trying to get threat rating comments map with unknown tag:" + tag);
	}

	public CodeToUserStringMap getStressBasedThreatRatingCommentsMap()
	{
		return getCodeToUserStringMapData(TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP);
	}

	public CodeToUserStringMap getSimpleThreatRatingCommentsMap()
	{
		return getCodeToUserStringMapData(TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP);
	}
	
	public static ThreatRatingCommentsData find(ObjectManager objectManager, ORef threatRatingCommentsDataRef)
	{
		return (ThreatRatingCommentsData) objectManager.findObject(threatRatingCommentsDataRef);
	}
	
	public static ThreatRatingCommentsData find(Project project, ORef threatRatingCommentsDataRef)
	{
		return find(project.getObjectManager(), threatRatingCommentsDataRef);
	}
	
	public static boolean is(BaseObject object)
	{
		return is(object.getRef());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == ThreatRatingCommentsDataSchema.getObjectType();
	}

	public static final String TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP = "SimpleThreatRatingCommentsMap";
	public static final String TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP = "StressBasedThreatRatingCommentsMap";
}
