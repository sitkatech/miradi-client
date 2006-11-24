/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.ids.DiagramLinkageId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.utils.DataMap;

public class FactorLinkDataMap extends DataMap
{
	public FactorLinkDataMap()
	{
		super();
	}

	public void setId(DiagramLinkageId id)
	{
		putId(TAG_ID, id);
	}
	
	public DiagramLinkageId getId()
	{
		return new DiagramLinkageId(getId(TAG_ID).asInt());
	}

	public void setFromId(DiagramNodeId fromId)
	{
		putInt(TAG_FROM_ID, fromId.asInt());
	}

	public DiagramNodeId getFromId()
	{
		return new DiagramNodeId(getId(TAG_FROM_ID).asInt());
	}

	public void setToId(DiagramNodeId toId)
	{
		putInt(TAG_TO_ID, toId.asInt());
	}

	public DiagramNodeId getToId()
	{
		return new DiagramNodeId(getId(TAG_TO_ID).asInt());
	}

	public static final String TAG_ID = "Id";
	public static final String TAG_FROM_ID = "FromId";
	public static final String TAG_TO_ID = "ToId";

}
