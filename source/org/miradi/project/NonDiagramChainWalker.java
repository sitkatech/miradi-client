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
package org.miradi.project;

import org.miradi.diagram.DiagramChainWalker;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;


public class NonDiagramChainWalker
{	
	public NonDiagramChainWalker(Project projectToUse)
	{
		project = projectToUse;
	}

	public FactorSet buildNormalChainAndGetFactors(Factor factor)
	{
		FactorSet factorsOnAllDiagrams = new FactorSet();
		DiagramChainWalker realWalker = new DiagramChainWalker();
		ORefList diagramFactorRefs = factor.findObjectsThatReferToUs(DiagramFactor.getObjectType());
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor df = DiagramFactor.find(getProject(), diagramFactorRefs.get(i));
			ORefList diagramRefs = df.findDiagramsThatReferToUs();
			if(diagramRefs.size() != 1)
				throw new RuntimeException("DF " + df.getRef() + " is in multiple diagrams: " + diagramRefs);
			
			DiagramObject diagram = DiagramObject.findDiagramObject(getProject(), diagramRefs.getFirstElement());
			FactorSet factorsOnThisDiagram = realWalker.buildNormalChainAndGetFactors(diagram, df);
			factorsOnAllDiagrams.attemptToAddAll(factorsOnThisDiagram);
		}
		
		return factorsOnAllDiagrams;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
