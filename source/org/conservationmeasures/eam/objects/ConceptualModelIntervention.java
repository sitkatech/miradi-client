/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.project.RatingValueSet;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class ConceptualModelIntervention extends ConceptualModelNode
{
	public ConceptualModelIntervention(BaseId idToUse)
	{
		super(idToUse, DiagramNode.TYPE_INTERVENTION);
		status = STATUS_REAL;
		activityIds = new IdListData();
		ratings = new RatingValueSet();
	}
	
	public ConceptualModelIntervention(ModelNodeId idToUse, EnhancedJsonObject json) throws ParseException
	{
		super(idToUse, DiagramNode.TYPE_INTERVENTION, json);
		status = json.optString(TAG_STATUS, STATUS_REAL);
		activityIds = new IdListData(json.optString(TAG_ACTIVITY_IDS));
		ratings = new RatingValueSet(json.optJson(TAG_RATING_VALUE_SET));
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
	
	public void setRatingValueId(BaseId criterionId, BaseId valueId)
	{
		ratings.setValueId(criterionId, valueId);
	}
	
	public BaseId getRatingValueId(BaseId criterionId, BaseId defaultValueId)
	{
		return ratings.getValueId(criterionId, defaultValueId);
	}

	public String getData(String fieldTag)
	{
		if(TAG_ACTIVITY_IDS.equals(fieldTag))
			return activityIds.get();
		else if(TAG_STATUS.equals(fieldTag))
			return status;
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(TAG_ACTIVITY_IDS.equals(fieldTag))
			activityIds.set(dataValue);
		else if(TAG_STATUS.equals(fieldTag))
			status = dataValue;
		else if(TAG_RATING_VALUE_SET.equals(fieldTag))
			ratings.fillFrom(dataValue);
		else
			super.setData(fieldTag, dataValue);
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = createBaseJsonObject(NodeTypeIntervention.INTERVENTION_TYPE);
		json.put(TAG_STATUS, status);
		json.put(TAG_ACTIVITY_IDS, activityIds.toString());
		json.put(TAG_RATING_VALUE_SET, ratings.toJson());
		return json;
	}
	
	public static final String TAG_ACTIVITY_IDS = "ActivityIds";
	public static final String TAG_STATUS = "Status";
	public static final String TAG_RATING_VALUE_SET = "RatingValueSet";
	public static final String STATUS_DRAFT = "Draft";
	public static final String STATUS_REAL = "Real";

	String status;
	IdListData activityIds;
	RatingValueSet ratings;
}
