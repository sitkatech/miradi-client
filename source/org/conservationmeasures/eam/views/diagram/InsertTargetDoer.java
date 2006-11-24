/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.DiagramTarget;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Factor;

public class InsertTargetDoer extends InsertFactorDoer
{
	public FactorType getTypeToInsert()
	{
		return Factor.TYPE_TARGET;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Target");
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramTarget.class, true);
	}
}
