/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objecthelpers;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;

public class TestThreatTargetVirtualLinkHelper extends TestCaseWithProject
{
	public TestThreatTargetVirtualLinkHelper(String name)
	{
		super(name);
	}
	
	public void testGetDownstreamTargetWithoutStresses() throws Exception
	{
		DiagramFactor threat = createThreatDiagramFactor();
		DiagramFactor targetWithoutStresses = createTargetDiagramFactor();
		getProject().createDiagramFactorLink(threat, targetWithoutStresses);
		
		verifyDownstreamTargetCount((Cause) threat.getWrappedFactor(), 0);
	}

	public void testGetDownstreamTargetWithStress() throws Exception
	{
		DiagramFactor threat = createThreatDiagramFactor();
		DiagramFactor targetWithStresses = createTargetWithStresses();
		getProject().createDiagramFactorLinkAndAddToDiagram(threat, targetWithStresses);
		
		verifyDownstreamTargetCount((Cause) threat.getWrappedFactor(), 1);
	}

	public void testGetDownstreamTargets() throws Exception
	{
		DiagramFactor threat = createThreatDiagramFactor();
		DiagramFactor strategy = getProject().createDiagramFactorAndAddToDiagram(Strategy.getObjectType());
		DiagramFactor targetWithoutStresses = createTargetDiagramFactor();
		DiagramFactor targetWithStresses = createTargetWithStresses();
		
		getProject().createDiagramLinkAndAddToDiagram(threat, strategy);
		getProject().createDiagramLinkAndAddToDiagram(strategy, targetWithStresses);
		getProject().createDiagramLinkAndAddToDiagram(strategy, targetWithoutStresses);
		
		verifyDownstreamTargetCount((Cause) threat.getWrappedFactor(), 1);
	}

	private void verifyDownstreamTargetCount(Cause threat, int expectedDownstreamTargetCount)
	{
		ThreatTargetVirtualLinkHelper helper = new ThreatTargetVirtualLinkHelper(getProject());
		assertEquals("incorrect downstream target count?", expectedDownstreamTargetCount, helper.getDownstreamTargetsVisTSR(threat).size());
	}
	
	private DiagramFactor createTargetWithStresses() throws Exception
	{
		DiagramFactor targetWithStresses = createTargetDiagramFactor();
		getProject().populateTarget((Target) targetWithStresses.getWrappedFactor());

		return targetWithStresses;
	}
	
	private DiagramFactor createThreatDiagramFactor() throws Exception
	{
		DiagramFactor threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		getProject().enableAsThreat((Cause) threatDiagramFactor.getWrappedFactor());

		return threatDiagramFactor;
 	}
	
	private DiagramFactor createTargetDiagramFactor() throws Exception
	{
		return getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
	}
}
