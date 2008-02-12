/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import org.miradi.objects.Factor;

public class DirectThreatSet extends FactorSet
{
	
	public DirectThreatSet()
	{
		super();
	}
	
	public DirectThreatSet(FactorSet nodesToAttemptToAdd)
	{
		attemptToAddAll(nodesToAttemptToAdd);
	}
	
	public DirectThreatSet(Factor[] nodesToAttemptToAdd)
	{
		attemptToAddAll(nodesToAttemptToAdd);
	}

	public boolean isLegal(Factor node)
	{
		return (node.isDirectThreat());
	}
	
	
}
