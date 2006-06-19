/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

public class NonDraftInterventionSet extends ConceptualModelNodeSet
{
	public NonDraftInterventionSet(ConceptualModelNodeSet nodesToAttemptToAdd)
	{
		attemptToAddAll(nodesToAttemptToAdd);
	}

	public boolean isLegal(ConceptualModelNode node)
	{
		return (node.isIntervention() && !node.isStatusDraft());
	}
	

}
