/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.ids.ObjectiveId;
import org.miradi.objectdata.ORefListData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;


public class Objective extends Desire
{
	public Objective(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, new ObjectiveId(id.asInt()));
	}
	
	public Objective(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new ObjectiveId(idAsInt), json);
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
		return ObjectType.OBJECTIVE;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_PROGRESS_PERCENT_REFS))
			return ProgressPercent.getObjectType();
				
		return super.getAnnotationType(tag);
	}
	
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_PROGRESS_PERCENT_REFS))
			return true;
				
		return super.isRefList(tag);
	}

	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE))
			return getLatestProgressPercentComplete();
		
		if (fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS))
			return getLatestProgressPercentDetails();
		
		return super.getPseudoData(fieldTag);
	}
	
	
	private String getLatestProgressPercentDetails()
	{
		ProgressPercent latestProgressPercent = getLatestProgressPercent();
		if(latestProgressPercent == null)
			return "";
		return latestProgressPercent.getData(ProgressPercent.TAG_PERCENT_COMPLETE_NOTES);
	}

	private String getLatestProgressPercentComplete()
	{
		ProgressPercent latestProgressPercent = getLatestProgressPercent();
		if(latestProgressPercent == null)
			return "";
		return latestProgressPercent.getData(ProgressPercent.TAG_PERCENT_COMPLETE);
	}
	
	private ProgressPercent getLatestProgressPercent()
	{
		return (ProgressPercent) getLatestObject(getObjectManager(), progressPercentRefs.getORefList(), ProgressPercent.TAG_DATE);
	}

	public static Objective find(ObjectManager objectManager, ORef objectiveRef)
	{
		return (Objective) objectManager.findObject(objectiveRef);
	}
	
	public static Objective find(Project project, ORef objectiveRef)
	{
		return find(project.getObjectManager(), objectiveRef);
	}
	
	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(int nodeType)
	{
		return nodeType == getObjectType();
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}

	public void clear()
	{
		super.clear();
		progressPercentRefs = new ORefListData(TAG_PROGRESS_PERCENT_REFS);
		
		latestProgressPercentComplete = new PseudoStringData(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE);
		latestProgressPercentDetails = new PseudoStringData(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS);
		
		addField(TAG_PROGRESS_PERCENT_REFS, progressPercentRefs);
		
		addField(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE, latestProgressPercentComplete);
		addField(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS, latestProgressPercentDetails);
	}
	
	public static final String OBJECT_NAME = "Objective";
	
	public static final String TAG_PROGRESS_PERCENT_REFS = "ProgressPrecentRefs";
	
	public static final String PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE = "PseudoLatestProgressPercentComplete";
	public static final String PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS = "PseudoLatestProgressPercentDetails";
	
	private ORefListData progressPercentRefs;
	
	private PseudoStringData latestProgressPercentComplete;
	private PseudoStringData latestProgressPercentDetails;
}
