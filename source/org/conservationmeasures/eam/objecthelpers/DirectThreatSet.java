/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class DirectThreatSet extends ConceptualModelNodeSet
{
	public DirectThreatSet(ConceptualModelNodeSet nodesToAttemptToAdd)
	{
		attemptToAddAll(nodesToAttemptToAdd);
	}

	public boolean isLegal(ConceptualModelNode node)
	{
		return (node.isDirectThreat());
	}
	
	
}
