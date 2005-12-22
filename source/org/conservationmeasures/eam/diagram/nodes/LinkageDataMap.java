/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.utils.DataMap;

public class LinkageDataMap extends DataMap
{
	public LinkageDataMap()
	{
		super();
	}

	public void setId(int id)
	{
		putInt(TAG_ID, id);
	}
	
	public int getId()
	{
		return getInt(TAG_ID);
	}

	public void setFromId(int fromId)
	{
		putInt(TAG_FROM_ID, fromId);
	}

	public int getFromId()
	{
		return getInt(TAG_FROM_ID);
	}

	public void setToId(int toId)
	{
		putInt(TAG_TO_ID, toId);
	}

	public int getToId()
	{
		return getInt(TAG_TO_ID);
	}

	public static final String TAG_ID = "Id";
	public static final String TAG_FROM_ID = "FromId";
	public static final String TAG_TO_ID = "ToId";

}
