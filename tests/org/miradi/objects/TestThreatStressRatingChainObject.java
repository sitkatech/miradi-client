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

import org.miradi.diagram.ThreatStressRatingChainObject;
import org.miradi.main.TestCaseWithProject;

public class TestThreatStressRatingChainObject extends TestCaseWithProject
{
	public TestThreatStressRatingChainObject(String name)
	{
		super(name);
	}
	
	//FIXME this test is not complete since the class is no complete.
	public void testBasics() throws Exception
	{
		DiagramFactor cause1 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor cause2 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor cause3 = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		DiagramFactor strategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		DiagramFactor target1 = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor target2 = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
		getProject().createDiagramLinkAndAddToDiagram(cause1, target1);
		
		getProject().createDiagramLinkAndAddToDiagram(cause3, cause2);
		getProject().createDiagramLinkAndAddToDiagram(cause2, strategy);
		getProject().createDiagramLinkAndAddToDiagram(strategy, target1);
		getProject().createDiagramLinkAndAddToDiagram(strategy, target2);
		
		ThreatStressRatingChainObject chainObject = new ThreatStressRatingChainObject(getProject());
		HashSet<DiagramFactor> upstreamThreats1 = chainObject.upstreamThreatsFromTarget(getProject().getTestingDiagramObject(), target2);
		assertEquals("wrong threat count", 1, upstreamThreats1.size());
		assertTrue("wrong threat in list?", upstreamThreats1.contains(cause2));
		
		HashSet<DiagramFactor> upstreamThreats2 = chainObject.upstreamThreatsFromTarget(getProject().getTestingDiagramObject(), cause2);
		assertEquals("wrong threat count", 0, upstreamThreats2.size());
		assertTrue("wrong threat in list?", upstreamThreats2.contains(cause2));
	}
}
