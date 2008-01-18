/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;

public class InsertContributingFactorDoer extends InsertFactorDoer
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
		if(!insertedNode.isContributingFactor())
			warnNotContributingFactor();
	}

	void warnNotContributingFactor()
	{
		EAM.notifyDialog(EAM.text("Text|This is a Direct Threat because it is linked to a Target"));
	}
	
	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setContributingFactorsVisible(true);
		getProject().getLayerManager().setDirectThreatsVisible(true);
	}

}
