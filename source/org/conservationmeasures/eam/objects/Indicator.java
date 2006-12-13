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
	
	public int getTaskCount()
	{
		return taskIds.size();
	}
	
	public BaseId getTaskId(int index)
	{
		return taskIds.get(index);
	}
	
	public IdList getTaskIdList()
	{
		return taskIds.getIdList().createClone();
	}
	
	public void addTaskId(BaseId taskId)
	{
		taskIds.add(taskId);
	}
	
	void clear()
	{
		super.clear();
		shortLabel = new StringData();
		priority = new RatingData();
		status = new RatingData();
		taskIds = new IdListData();

		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_PRIORITY, priority);
		addField(TAG_STATUS, status);
		addField(TAG_TASK_IDS, taskIds);
	}
	
	public int getType()
	{
		return ObjectType.INDICATOR;
	}

	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return shortLabel + "." + getLabel();
	}

	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_PRIORITY = "Priority";
	public static final String TAG_STATUS = "Status";
	public final static String TAG_TASK_IDS = "TaskIds";
	
	public static final String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
	public static final String PSEUDO_TAG_TARGETS = "PseudoTagTargets";
	public static final String PSEUDO_TAG_DIRECT_THREATS = "PseudoTagDirectThreats";
	public static final String PSEUDO_TAG_STRATEGIES = "PseudoTagStrategies";
	
	public static final String OBJECT_NAME = "Indicator";

	StringData shortLabel;
	RatingData priority;
	RatingData status;
	IdListData taskIds;
}
