/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;

public class InsertDirectThreatDoer extends InsertFactorDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
				
		return !getDiagramView().isResultsChainTab();
	}
	
	public int getTypeToInsert()
	{
		return ObjectType.CAUSE;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Factor");
	}

	protected void linkToPreviouslySelectedFactors(DiagramFactor newlyInserted, FactorCell[] factorsToLinkTo) throws Exception
	{
		super.linkToPreviouslySelectedFactors(newlyInserted, factorsToLinkTo);
		Factor insertedNode = getProject().findNode(newlyInserted.getWrappedId());
		if(!insertedNode.isDirectThreat())
			warnNotDirectThreat();
	}

	protected void notLinkingToAnyFactors() throws CommandFailedException
	{
		super.notLinkingToAnyFactors();
		warnNotDirectThreat();
	}

	private void warnNotDirectThreat()
	{
		EAM.notifyDialog(EAM.text("Text|This will not be a Direct Threat until it is linked to a Target"));
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setContributingFactorsVisible(true);
		getProject().getLayerManager().setDirectThreatsVisible(true);
	}
}

