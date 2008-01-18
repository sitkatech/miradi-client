/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objects.ViewData;

public class DeleteConceptualModelPageDoer extends DeleteDiagramPageDoer
{
	public boolean isAvailable()
	{
		boolean isSuperAvailable = super.isAvailable();
		if (isSuperAvailable)
			return hasMoreThanOnePage();
		
		return false;
	}

	public boolean isCorrectTab()
	{
		return ! getDiagramView().isResultsChainTab();
	}

	public String getDiagramObjectTag()
	{
		return ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF;
	}
	
	public boolean hasMoreThanOnePage()
	{
		return getProject().getConceptualModelDiagramPool().size() > 1;
	}
}