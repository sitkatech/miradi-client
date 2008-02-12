/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;

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

