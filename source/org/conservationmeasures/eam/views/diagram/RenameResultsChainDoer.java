/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;

public class RenameResultsChainDoer extends RenameDiagramObjectDoer
{
	public boolean isInvalidSelection()
	{
		if (! getDiagramView().isResultsChainTab())
			return true;
		
		try
		{
			ORef currentResultsChainRef = getProject().getCurrentViewData().getCurrentResutlstChainRef();
			return currentResultsChainRef.isInvalid();			
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return true;
	}
}