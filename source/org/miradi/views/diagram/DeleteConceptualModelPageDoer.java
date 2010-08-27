/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.diagram;

import org.miradi.objects.ViewData;

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