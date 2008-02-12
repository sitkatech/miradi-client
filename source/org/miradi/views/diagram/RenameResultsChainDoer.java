/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.objects.ViewData;

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