/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.ModelNodeId;

public interface LinkageListener
{
	public void linkageWasCreated(ModelNodeId fromId, ModelNodeId toId);
	public void linkageWasDeleted(ModelNodeId fromId, ModelNodeId toId);
}
