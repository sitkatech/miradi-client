/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.DiagramIntermediateResultCell;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class InsertIntermediateResultDoer extends InsertFactorDoer
{
	
	public boolean isAvailable()
	{
		if (!getProject().isOpen()) 
			return false;
		return getDiagramView().isResultsChainTab();
	}
	
	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramIntermediateResultCell.class, true);
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Intermediate Result");
	}

	public int getTypeToInsert()
	{
		return ObjectType.INTERMEDIATE_RESULT;
	}

}
