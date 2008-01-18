/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.objects.Factor;

public class NonDraftStrategySet extends FactorSet
{
	public NonDraftStrategySet()
	{
		super();
	}
	
	public NonDraftStrategySet(FactorSet nodesToAttemptToAdd)
	{
		attemptToAddAll(nodesToAttemptToAdd);
	}
	
	public NonDraftStrategySet(Factor[] nodesToAttemptToAdd)
	{
		attemptToAddAll(nodesToAttemptToAdd);
	}

	public boolean isLegal(Factor node)
	{
		return (node.isStrategy() && !node.isStatusDraft());
	}
	

}
