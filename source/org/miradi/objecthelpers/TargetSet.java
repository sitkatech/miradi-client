/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import org.miradi.objects.Factor;

public class TargetSet extends FactorSet
{
	public TargetSet()
	{
		super();
	}
	
	public TargetSet(FactorSet nodesToAttemptToAdd)
	{
		attemptToAddAll(nodesToAttemptToAdd);
	}

	public TargetSet(Factor[] nodesToAttemptToAdd)
	{
		attemptToAddAll(nodesToAttemptToAdd);
	}
	
	public boolean isLegal(Factor node)
	{
		return (node.isTarget());
	}
	

}
