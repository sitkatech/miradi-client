/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.DiagramStrategyCell;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Factor;

public class InsertStrategyDoer extends InsertFactorDoer
{
	public FactorType getTypeToInsert()
	{
		return Factor.TYPE_STRATEGY;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Strategy");
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramStrategyCell.class, true);
	}
	
	
}
