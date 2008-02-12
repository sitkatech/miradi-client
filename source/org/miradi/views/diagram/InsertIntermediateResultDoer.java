/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.diagram.cells.DiagramIntermediateResultCell;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;

public class InsertIntermediateResultDoer extends InsertFactorDoer
{
	
	public boolean isAvailable()
	{
		if (!super.isAvailable())
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
