/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.utils.DataMap;

public class LinkageDataMap extends DataMap
{
	public LinkageDataMap()
	{
		super();
	}

	public void setId(ModelLinkageId id)
	{
		putId(TAG_ID, id);
	}
	
	public ModelLinkageId getId()
	{
		return new ModelLinkageId(getId(TAG_ID).asInt());
	}

	public void setFromId(int fromId)
	{
		putInt(TAG_FROM_ID, fromId);
	}

	public ModelNodeId getFromId()
	{
		return new ModelNodeId(getId(TAG_FROM_ID).asInt());
	}

	public void setToId(int toId)
	{
		putInt(TAG_TO_ID, toId);
	}

	public ModelNodeId getToId()
	{
		return new ModelNodeId(getId(TAG_TO_ID).asInt());
	}

	public static final String TAG_ID = "Id";
	public static final String TAG_FROM_ID = "FromId";
	public static final String TAG_TO_ID = "ToId";

}
