/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.FactorId;

public interface LinkageListener
{
	public void linkageWasCreated(FactorId fromId, FactorId toId);
	public void linkageWasDeleted(FactorId fromId, FactorId toId);
}
