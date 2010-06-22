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

import org.miradi.main.EAMTestCase;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.project.NonDiagramChainWalker;
import org.miradi.project.ProjectForTesting;

public class TestNonDiagramChainWalker extends EAMTestCase
{
	public TestNonDiagramChainWalker(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		walker = project.getObjectManager().getNonDiagramChainWalker();
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
		ORef targetRef = project.createObject(Target.getObjectType());
		Target target = Target.find(getProject(), targetRef);
		FactorSet nothingUpstreamYet = walker.buildUpstreamDownstreamChainAndGetFactors(target);
		assertEquals("Already something upstream?", 1, nothingUpstreamYet.size());

		ORef factorRef = project.createObject(Cause.getObjectType());
		Cause factor = Cause.find(getProject(), factorRef);
		CreateFactorLinkParameter extraInfo1 = new CreateFactorLinkParameter(factorRef, targetRef);
		project.createObject(FactorLink.getObjectType(), extraInfo1);
	
		ORef threatRef = project.createObject(Cause.getObjectType());
		Cause threat = Cause.find(getProject(), threatRef);
		threat.setData(Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE);
		CreateFactorLinkParameter extraInfo2 = new CreateFactorLinkParameter(threatRef, factorRef);
		project.createObject(FactorLink.getObjectType(), extraInfo2);

		ORefSet targetChain = walker.buildNormalChainAndGetFactorRefs(target);
		ORefSet factorChain = walker.buildNormalChainAndGetFactorRefs(factor);
		ORefSet threatChain = walker.buildNormalChainAndGetFactorRefs(threat);
		
		assertEquals("Target and factor chains not identical?", targetChain, factorChain);
		assertEquals("Target and threat chains not identical?", factorChain, threatChain);
	}
	
	public void testCacheClearing() throws Exception
	{
		ORef targetRef = project.createObject(Target.getObjectType());
		Target target = (Target)project.findObject(targetRef);
		FactorSet nothingUpstreamYet = walker.buildUpstreamDownstreamChainAndGetFactors(target);
		assertEquals("Already something upstream?", 1, nothingUpstreamYet.size());

		ORef threatRef = project.createObject(Cause.getObjectType());
		Cause threat = (Cause)project.findObject(threatRef);
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(threatRef, targetRef);
		ORef linkRef = project.createObject(FactorLink.getObjectType(), extraInfo);
		FactorLink link = (FactorLink)project.findObject(linkRef);
		
		FactorSet upstreamOfTarget = walker.buildUpstreamDownstreamChainAndGetFactors(target);
		assertEquals("Threat not upstream of target now?", 2, upstreamOfTarget.size());
		FactorSet downstreamOfThreat = walker.buildUpstreamDownstreamChainAndGetFactors(threat);
		assertEquals("Target not downstream of threat?", 2, downstreamOfThreat.size());
		
		project.deleteObject(link);
		FactorSet nothingUpstream = walker.buildUpstreamDownstreamChainAndGetFactors(target);
		assertEquals("Didn't reset upstream?", 1, nothingUpstream.size());
		FactorSet nothingDownstream = walker.buildNormalChainAndGetFactors(threat);
		assertEquals("Didn't reset downstream?", 1, nothingDownstream.size());
	}
	
	public ProjectForTesting getProject()
	{
		return project;
	}

	private ProjectForTesting project;
	private NonDiagramChainWalker walker;
}
