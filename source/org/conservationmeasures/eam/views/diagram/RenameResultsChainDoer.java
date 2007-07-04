/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objects.ViewData;

public class RenameResultsChainDoer extends RenameDiagramObjectDoer
{
	public boolean isCorrectTab()
	{
		return getDiagramView().isResultsChainTab();
	}
	
	public String getDiagramObjectTag()
	{
		return ViewData.TAG_CURRENT_RESULTS_CHAIN_REF;
	}
}