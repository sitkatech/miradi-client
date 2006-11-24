/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Factor;

public class InsertDirectThreat extends InsertNode
{
	public FactorType getTypeToInsert()
	{
		return Factor.TYPE_CAUSE;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Direct Threat");
	}

	void linkToPreviouslySelectedNodes(ModelNodeId newlyInsertedId, DiagramFactor[] nodesToLinkTo) throws CommandFailedException
	{
		super.linkToPreviouslySelectedNodes(newlyInsertedId, nodesToLinkTo);
		Factor insertedNode = getProject().findNode(newlyInsertedId);
		if(!insertedNode.isDirectThreat())
			warnNotDirectThreat();
	}

	void notLinkingToAnyNodes() throws CommandFailedException
	{
		super.notLinkingToAnyNodes();
		warnNotDirectThreat();
	}

	void warnNotDirectThreat()
	{
		EAM.notifyDialog(EAM.text("Text|This will not be a Direct Threat until it is linked to a Target"));
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setIndirectFactorsVisible(true);
		getProject().getLayerManager().setDirectThreatsVisible(true);
	}
}

