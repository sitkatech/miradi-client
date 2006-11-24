/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.utils.DataMap;

public class FactorLinkDataMap extends DataMap
{
	public FactorLinkDataMap()
	{
		super();
	}

	public void setId(DiagramFactorLinkId id)
	{
		putId(TAG_ID, id);
	}
	
	public DiagramFactorLinkId getId()
	{
		return new DiagramFactorLinkId(getId(TAG_ID).asInt());
	}

	public void setFromId(DiagramFactorId fromId)
	{
		putInt(TAG_FROM_ID, fromId.asInt());
	}

	public DiagramFactorId getFromId()
	{
		return new DiagramFactorId(getId(TAG_FROM_ID).asInt());
	}

	public void setToId(DiagramFactorId toId)
	{
		putInt(TAG_TO_ID, toId.asInt());
	}

	public DiagramFactorId getToId()
	{
		return new DiagramFactorId(getId(TAG_TO_ID).asInt());
	}

	public static final String TAG_ID = "Id";
	public static final String TAG_FROM_ID = "FromId";
	public static final String TAG_TO_ID = "ToId";

}
