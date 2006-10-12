/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class TargetSet extends ConceptualModelNodeSet
{
	public TargetSet(ConceptualModelNodeSet nodesToAttemptToAdd)
	{
		attemptToAddAll(nodesToAttemptToAdd);
	}

	public boolean isLegal(ConceptualModelNode node)
	{
		return (node.isTarget());
	}
	

}
