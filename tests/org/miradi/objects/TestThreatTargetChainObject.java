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

import org.miradi.diagram.ThreatTargetChainObject;
import org.miradi.main.TestCaseWithProject;

public class TestThreatTargetChainObject extends TestCaseWithProject
{
	public TestThreatTargetChainObject(String name)
	{
		super(name);
	}
	
	//FIXME this test is not complete since the class is no complete.
	public void testBasics() throws Exception
	{
		DiagramFactor strategy = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Strategy.getObjectType());
		
		DiagramFactor cause1 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		DiagramFactor cause2 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		DiagramFactor cause3 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		
		DiagramFactor target1 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Target.getObjectType());
		DiagramFactor target2 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Target.getObjectType());
		
		getProject().createDiagramLinkAndAddToDiagram(cause1, target1);		    // cause1 -> target1
		getProject().enableAsThreat(cause1.getWrappedORef());
		
		getProject().createDiagramLinkAndAddToDiagram(cause1, cause2); //cause1 -> cause2 -> target1
		getProject().createDiagramLinkAndAddToDiagram(cause2, target1);			// cause2 -> target2
		getProject().createDiagramLinkAndAddToDiagram(cause2, target2);
		getProject().enableAsThreat(cause2.getWrappedORef());
		
		getProject().createDiagramLinkAndAddToDiagram(cause3, strategy);
		getProject().createDiagramLinkAndAddToDiagram(strategy, target2);
		
		
		
		ThreatTargetChainObject chainObject = new ThreatTargetChainObject(getProject());
		HashSet<Factor> upstreamThreats1 = chainObject.getUpstreamThreatsFromTarget(target1.getWrappedFactor());
		assertEquals("wrong threat count?", 2, upstreamThreats1.size());
		assertTrue("wrong threat in list?", upstreamThreats1.contains(cause2.getWrappedFactor()));
		assertTrue("wrong threat in list?", upstreamThreats1.contains(cause1.getWrappedFactor()));
		
		HashSet<Factor> upstreamThreats3 = chainObject.getUpstreamThreatsFromTarget(target2.getWrappedFactor());
		assertEquals("wrong threat count?", 1, upstreamThreats3.size());
		assertTrue("wrong threat in list?", upstreamThreats3.contains(cause2.getWrappedFactor()));
		
		HashSet<Factor> upstreamTargets = chainObject.getDownstreamTargetsFromThreat(cause1.getWrappedFactor());
		assertEquals("wrong upstream target count?", 2, upstreamTargets.size());
	}
}
