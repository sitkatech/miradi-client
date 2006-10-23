/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.RatingData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class ConceptualModelIntervention extends ConceptualModelNode
{
	public ConceptualModelIntervention(BaseId idToUse)
	{
		super(idToUse, DiagramNode.TYPE_INTERVENTION);
		status = STATUS_REAL;
	}
	
	public ConceptualModelIntervention(ModelNodeId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, DiagramNode.TYPE_INTERVENTION, json);
		status = json.optString(TAG_STATUS, STATUS_REAL);
	}

	public boolean isIntervention()
	{
		return true;
	}
	
	public boolean canHaveObjectives()
	{
		return true;
	}
	
	public boolean isStatusReal()
	{
		return !isStatusDraft();
	}
	
	public boolean isStatusDraft()
	{
		return STATUS_DRAFT.equals(status);
	}
	
	public void insertActivityId(BaseId activityId, int insertAt)
	{
		activityIds.insertAt(activityId, insertAt);
	}
	
	public void removeActivityId(BaseId activityId)
	{
		activityIds.removeId(activityId);
	}
	
	public IdList getActivityIds()
	{
		return activityIds.getIdList();
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_STATUS.equals(fieldTag))
			return status;
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(TAG_STATUS.equals(fieldTag))
			status = dataValue;
		else
			super.setData(fieldTag, dataValue);
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		json.put(TAG_STATUS, status);
		return json;
	}
	
	void clear()
	{
		super.clear();
		activityIds = new IdListData();
		taxonomyCode = new StringData();
		impact = new RatingData();
		
		addField(TAG_ACTIVITY_IDS, activityIds);
		addField(TAG_TAXONOMY_CODE, taxonomyCode);
		addField(TAG_IMPACT, impact);
	}

	public static final String TAG_ACTIVITY_IDS = "ActivityIds";
	public static final String TAG_STATUS = "Status";
	public static final String STATUS_DRAFT = "Draft";
	public static final String STATUS_REAL = "Real";
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String TAG_IMPACT = "Impact";

	String status;
	IdListData activityIds;
	StringData taxonomyCode; 
	RatingData impact;
}
