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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;

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
		Factor insertedNode = Factor.findFactor(getProject(), newlyInserted.getWrappedORef());
		if(!insertedNode.isContributingFactor())
			warnNotContributingFactor();
	}

	private void warnNotContributingFactor()
	{
		EAM.notifyDialog(EAM.text("Text|This is a Direct Threat because it is linked to a Target"));
	}
	
	public void forceVisibleInLayerManager()
	{
		getCurrentLayerManager().setContributingFactorsVisible(true);
		getCurrentLayerManager().setDirectThreatsVisible(true);
	}

}
