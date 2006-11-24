/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.DiagramStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Factor;

public class InsertIntervention extends InsertNode
{
	public FactorType getTypeToInsert()
	{
		return Factor.TYPE_INTERVENTION;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Strategy");
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramStrategy.class, true);
	}
	
	
}
