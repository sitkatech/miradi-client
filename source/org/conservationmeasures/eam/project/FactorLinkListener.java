/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.FactorId;

public interface FactorLinkListener
{
	public void factorLinkWasCreated(FactorId fromId, FactorId toId);
	public void factorLinkWasDeleted(FactorId fromId, FactorId toId);
}
