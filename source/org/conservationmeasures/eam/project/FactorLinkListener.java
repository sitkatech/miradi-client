/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.FactorId;

public interface FactorLinkListener
{
	public void factorLinkWasCreated(FactorId fromId, FactorId toId);
	public void factorLinkWasDeleted(FactorId fromId, FactorId toId);
}
