/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objects.ViewData;

public class DeleteResultsChainDoer extends DeleteDiagramPageDoer
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
