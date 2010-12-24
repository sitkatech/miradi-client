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

import org.miradi.diagram.ChainWalker;
import org.miradi.main.MiradiTestCase;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.project.ProjectForTesting;

public class TestChainWalker extends MiradiTestCase
{
	public TestChainWalker(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		walker = project.getObjectManager().getDiagramChainWalker();
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}

	public void testThreatFactorTargetChain() throws Exception
	{
		DiagramFactor targetDiagramFactor = project.createAndAddFactorToDiagram(Target.getObjectType());
		Factor target = targetDiagramFactor.getWrappedFactor();
		FactorSet nothingUpstreamYet = walker.buildNormalChainAndGetFactors(target);
		assertEquals("Already something upstream?", 1, nothingUpstreamYet.size());

		DiagramFactor factorDiagramFactor = project.createAndAddFactorToDiagram(Cause.getObjectType());
		Factor factor = factorDiagramFactor.getWrappedFactor();
	
		DiagramFactor threatDiagramFactor = project.createAndAddFactorToDiagram(Cause.getObjectType());
		Factor threat = threatDiagramFactor.getWrappedFactor();
		threat.setData(Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE);

		project.createDiagramLinkAndAddToDiagram(threatDiagramFactor, factorDiagramFactor);
		project.createDiagramLinkAndAddToDiagram(factorDiagramFactor, targetDiagramFactor);

		ORefSet targetChain = walker.buildNormalChainAndGetFactors(target).getFactorRefs();
		ORefSet factorChain = walker.buildNormalChainAndGetFactors(factor).getFactorRefs();
		ORefSet threatChain = walker.buildNormalChainAndGetFactors(threat).getFactorRefs();
		
		assertEquals("Target and factor chains not identical?", targetChain, factorChain);
		assertEquals("Target and threat chains not identical?", factorChain, threatChain);
	}
	
	public void testCacheClearing() throws Exception
	{
		DiagramFactor targetDiagramFactor = project.createAndAddFactorToDiagram(Target.getObjectType());
		Factor target = targetDiagramFactor.getWrappedFactor();
		FactorSet nothingUpstreamYet = walker.buildNormalChainAndGetFactors(target);
		assertEquals("Already something upstream?", 1, nothingUpstreamYet.size());

		DiagramFactor threatDiagramFactor = project.createAndAddFactorToDiagram(Cause.getObjectType());
		Factor threat = threatDiagramFactor.getWrappedFactor();
		threat.setData(Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE);

		project.createDiagramLinkAndAddToDiagram(threatDiagramFactor, targetDiagramFactor);
		
		FactorSet upstreamOfTarget = walker.buildNormalChainAndGetFactors(target);
		assertEquals("Threat not upstream of target now?", 2, upstreamOfTarget.size());
		FactorSet downstreamOfThreat = walker.buildNormalChainAndGetFactors(threat);
		assertEquals("Target not downstream of threat?", 2, downstreamOfThreat.size());
		
		DiagramObject diagramObject = getProject().getTestingDiagramModel().getDiagramObject();
		diagramObject.setData(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, "");
		FactorSet nothingUpstream = walker.buildNormalChainAndGetFactors(target);
		assertEquals("Didn't reset upstream?", 1, nothingUpstream.size());
		FactorSet nothingDownstream = walker.buildNormalChainAndGetFactors(threat);
		assertEquals("Didn't reset downstream?", 1, nothingDownstream.size());
	}
	
	public ProjectForTesting getProject()
	{
		return project;
	}

	private ProjectForTesting project;
	private ChainWalker walker;
}
