/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.ids.ModelNodeId;

public class CreateModelLinkageParameter extends CreateObjectParameter
{
	public CreateModelLinkageParameter(ModelNodeId from, ModelNodeId to)
	{
		fromId = from;
		toId = to;
	}

	public ModelNodeId getFromId()
	{
		return fromId;
	}
	
	public ModelNodeId getToId()
	{
		return toId;
	}
	
	ModelNodeId fromId;
	ModelNodeId toId;
}
