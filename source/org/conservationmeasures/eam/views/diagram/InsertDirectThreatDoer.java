/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Factor;

public class InsertDirectThreatDoer extends InsertFactorDoer
{
	public FactorType getTypeToInsert()
	{
		return Factor.TYPE_CAUSE;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Factor");
	}

	void linkToPreviouslySelectedFactors(FactorId newlyInsertedId, FactorCell[] factorsToLinkTo) throws CommandFailedException
	{
		super.linkToPreviouslySelectedFactors(newlyInsertedId, factorsToLinkTo);
		Factor insertedNode = getProject().findNode(newlyInsertedId);
		if(!insertedNode.isDirectThreat())
			warnNotDirectThreat();
	}

	void notLinkingToAnyFactors() throws CommandFailedException
	{
		super.notLinkingToAnyFactors();
		warnNotDirectThreat();
	}

	void warnNotDirectThreat()
	{
		EAM.notifyDialog(EAM.text("Text|This will not be a Direct Threat until it is linked to a Target"));
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setContributingFactorsVisible(true);
		getProject().getLayerManager().setDirectThreatsVisible(true);
	}
}

