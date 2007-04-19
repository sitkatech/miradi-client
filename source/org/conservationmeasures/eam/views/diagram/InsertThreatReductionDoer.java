/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.DiagramThreatReductionResultCell;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class InsertThreatReductionDoer extends InsertFactorDoer
{
	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramThreatReductionResultCell.class, true);
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Threat Reduction Result");
	}

	public int getTypeToInsert()
	{
		return ObjectType.THREAT_REDUCTION_RESULT;
	}

}
