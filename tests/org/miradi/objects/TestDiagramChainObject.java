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
package org.miradi.objects;

import java.util.HashSet;
import java.util.Vector;

import org.miradi.diagram.DiagramChainObject;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;

public class TestDiagramChainObject extends TestCaseWithProject
{
	public TestDiagramChainObject(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		DiagramFactor strategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		DiagramFactor causeLinkedTo = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor causeLinkedToFrom = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		
		getProject().createDiagramLinkAndAddToDiagram(strategy, causeLinkedTo);
		ORef diagramLinkRef = getProject().createDiagramLinkAndAddToDiagram(strategy, causeLinkedToFrom);
		DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);
		FactorLink factorLink = diagramLink.getWrappedFactorLink();
		factorLink.setData(FactorLink.TAG_BIDIRECTIONAL_LINK, FactorLink.BIDIRECTIONAL_LINK.toString());
		
		assertTrue("link is not bidirectional?", factorLink.isBidirectional());
		
		Vector<DiagramFactor> allDiagramFactors = new Vector();
		allDiagramFactors.add(strategy);
		allDiagramFactors.add(causeLinkedTo);
		allDiagramFactors.add(causeLinkedToFrom);
		
		verifyChain(strategy, allDiagramFactors);
		verifyChain(causeLinkedTo, allDiagramFactors);
		verifyChain(causeLinkedToFrom, allDiagramFactors);
	}

	private void verifyChain(DiagramFactor strartingDiagramFactor, Vector<DiagramFactor> allDiagramFactors)
	{
		DiagramChainObject chainObject = new DiagramChainObject();
		HashSet<DiagramFactor> diagramFactorsInChain = chainObject.buildNormalChainAndGetDiagramFactors(getProject().getTestingDiagramObject(), strartingDiagramFactor);
		assertEquals("wrong diagram factor in chain count?", 3, diagramFactorsInChain.size());
		
		for (int index = 0; index < allDiagramFactors.size(); ++index)
		{
			verifyDiagramFactorInChain(allDiagramFactors.get(index), diagramFactorsInChain);
		}
	}

	private void verifyDiagramFactorInChain(DiagramFactor strategy,	HashSet<DiagramFactor> diagramFactorsInChain)
	{
		assertTrue("diagramFactor not in chain?", diagramFactorsInChain.contains(strategy));
	}
}
