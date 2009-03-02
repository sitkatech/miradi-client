/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.diagram;

import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.views.umbrella.ThreatStressRatingCreator;

public class InsertDirectThreatDoer extends InsertFactorDoer
{
	@Override
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

	@Override
	protected void linkToPreviouslySelectedFactors(DiagramFactor newlyInserted, FactorCell[] factorsToLinkTo) throws Exception
	{
		super.linkToPreviouslySelectedFactors(newlyInserted, factorsToLinkTo);
		Factor insertedNode = Factor.findFactor(getProject(), newlyInserted.getWrappedORef());
		if(!insertedNode.isDirectThreat())
			warnNotDirectThreat();
	}

	@Override
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
		getCurrentLayerManager().setContributingFactorsVisible(true);
		getCurrentLayerManager().setDirectThreatsVisible(true);
	}
	
	@Override
	protected void doExtraWork(DiagramFactor newlyInsertedDiagramFactor) throws Exception
	{
		ThreatStressRatingCreator creator = new ThreatStressRatingCreator(getProject());
		Factor threat = newlyInsertedDiagramFactor.getWrappedFactor();
		ORefList factorLinkReferrerRefs = threat.findObjectsThatReferToUs(FactorLink.getObjectType());
		for (int index = 0; index < factorLinkReferrerRefs.size(); ++index)
		{
			FactorLink factorLink = FactorLink.find(getProject(), factorLinkReferrerRefs.get(index));
			ORef targetRef = factorLink.getSafeDownstreamTargetRef();
			creator.createAndAddThreatStressRating(newlyInsertedDiagramFactor.getWrappedORef(), targetRef);
		}
	}
}

