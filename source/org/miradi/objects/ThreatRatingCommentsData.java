/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.StringMap;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.StringMapData;

public class ThreatRatingCommentsData extends BaseObject
{
	public ThreatRatingCommentsData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}

	public ThreatRatingCommentsData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
	}

	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.THREAT_RATING_COMMENTS_DATA;
	}
	
	public String findComment(ORef threatRef, ORef targetRef)
	{
		String threatTargetRefsAsKey = createKey(threatRef, targetRef);
		if (getProject().isSimpleThreatRatingMode())
			return getSimpleThreatRatingCommentsMap().get(threatTargetRefsAsKey);
		
		return getStressBasedThreatRatingCommentsMap().get(threatTargetRefsAsKey);
	}
	
	public static String createKey(ORef threatRef, ORef targetRef)
	{
		return threatRef.toString() + targetRef.toString();
	}
	
	public StringMap getThreatRatingCommentsMap()
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

	public StringMap getStressBasedThreatRatingCommentsMap()
	{
		return new StringMap(stressBasedThreatRatingCommentsMap.getStringMap());
	}

	public StringMap getSimpleThreatRatingCommentsMap()
	{
		return new StringMap(simpleThreatRatingCommentsMap.getStringMap());
	}
	
	public String getSimpleThreatRatingComment(String threatTargetKey)
	{
		return getSimpleThreatRatingCommentsMap().get(threatTargetKey);
	}
	
	public static ThreatRatingCommentsData find(ObjectManager objectManager, ORef threatRatingCommentsDataRef)
	{
		return (ThreatRatingCommentsData) objectManager.findObject(threatRatingCommentsDataRef);
	}
	
	public static ThreatRatingCommentsData find(Project project, ORef threatRatingCommentsDataRef)
	{
		return find(project.getObjectManager(), threatRatingCommentsDataRef);
	}

	void clear()
	{
		super.clear();
		
		simpleThreatRatingCommentsMap = new StringMapData(TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP);
		stressBasedThreatRatingCommentsMap = new StringMapData(TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP);
		
		addField(TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP, simpleThreatRatingCommentsMap);
		addField(TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP, stressBasedThreatRatingCommentsMap);
	}

	public static final String OBJECT_NAME = "ThreatRatingCommentsData";
	
	public static final String TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP = "SimpleThreatRatingCommentsMaps";
	public static final String TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP = "StressBasedThreatRatingCommentsMaps";
	
	private StringMapData simpleThreatRatingCommentsMap;
	private StringMapData stressBasedThreatRatingCommentsMap;
}
