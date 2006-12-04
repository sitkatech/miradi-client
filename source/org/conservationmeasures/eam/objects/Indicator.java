/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.RatingData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Indicator extends EAMBaseObject
{
	public Indicator(IndicatorId idToUse)
	{
		super(idToUse);
		clear();
	}

	public Indicator(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
	}
	
	public int getSubtaskCount()
	{
		return subtaskIds.size();
	}
	
	public BaseId getSubtaskId(int index)
	{
		return subtaskIds.get(index);
	}
	
	public IdList getSubtaskIdList()
	{
		return subtaskIds.getIdList().createClone();
	}
	
	public void addSubtaskId(BaseId subtaskId)
	{
		subtaskIds.add(subtaskId);
	}
	
	void clear()
	{
		super.clear();
		shortLabel = new StringData();
		method = new StringData();
		resourceIds = new IdListData();
		location = new StringData();
		priority = new RatingData();
		status = new RatingData();
		cost = new StringData();
		fundingSource = new StringData();
		when = new StringData();
		subtaskIds = new IdListData();

		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_METHOD, method);
		addField(TAG_RESOURCE_IDS, resourceIds);
		addField(TAG_LOCATION, location);
		addField(TAG_PRIORITY, priority);
		addField(TAG_STATUS, status);
		addField(TAG_COST, cost);
		addField(TAG_FUNDING_SOURCE, fundingSource);
		addField(TAG_WHEN, when);
		addField(TAG_SUBTASK_IDS, subtaskIds);
	}
	
	public int getType()
	{
		return ObjectType.INDICATOR;
	}

	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	public IdList getResourceIdList()
	{
		return resourceIds.getIdList();
	}

	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return shortLabel + "." + getLabel();
	}

	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_METHOD = "Method";
	public static final String TAG_RESOURCE_IDS = "ResourceIds";
	public static final String TAG_LOCATION = "Location";
	public static final String TAG_PRIORITY = "Priority";
	public static final String TAG_STATUS = "Status";
	public static final String TAG_FUNDING_SOURCE = "Funding Source";
	public static final String TAG_COST = "Cost";
	public static final String TAG_WHEN = "When";
	public final static String TAG_SUBTASK_IDS = "SubtaskIds";
	
	public static final String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
	public static final String PSEUDO_TAG_TARGETS = "PseudoTagTargets";
	public static final String PSEUDO_TAG_DIRECT_THREATS = "PseudoTagDirectThreats";
	public static final String PSEUDO_TAG_STRATEGIES = "PseudoTagStrategies";

	StringData shortLabel;
	StringData method;
	IdListData resourceIds;
	StringData location;
	RatingData priority;
	RatingData status;
	StringData cost;
	StringData fundingSource;
	StringData when;
	IdListData subtaskIds;
}
