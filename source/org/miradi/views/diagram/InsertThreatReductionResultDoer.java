/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.diagram.cells.DiagramThreatReductionResultCell;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;

public class InsertThreatReductionResultDoer extends InsertFactorDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
				
		return getDiagramView().isResultsChainTab();
	}
	
	public int getTypeToInsert()
	{
		return ObjectType.THREAT_REDUCTION_RESULT;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Threat Reduction Result");
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramThreatReductionResultCell.class, true);
	}
}
