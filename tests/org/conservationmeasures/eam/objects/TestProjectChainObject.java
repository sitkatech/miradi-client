package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.ProjectChainObject;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestProjectChainObject extends EAMTestCase
{
	public TestProjectChainObject(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}

	public void testCacheClearing() throws Exception
	{
		ProjectChainObject builder = project.getObjectManager().getProjectChainBuilder();
		ORef targetRef = project.createObject(Target.getObjectType());
		Target target = (Target)project.findObject(targetRef);
		FactorSet nothingUpstreamYet = builder.buildUpstreamChainAndGetFactors(target);
		assertEquals("Already something upstream?", 1, nothingUpstreamYet.size());

		ORef threatRef = project.createObject(Cause.getObjectType());
		Cause threat = (Cause)project.findObject(threatRef);
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(threatRef, targetRef);
		ORef linkRef = project.createObjectAndReturnRef(FactorLink.getObjectType(), extraInfo);
		FactorLink link = (FactorLink)project.findObject(linkRef);
		
		FactorSet upstreamOfTarget = builder.buildUpstreamChainAndGetFactors(target);
		assertEquals("Threat not upstream of target now?", 2, upstreamOfTarget.size());
		FactorSet downstreamOfThreat = builder.buildUpstreamDownstreamChainAndGetFactors(threat);
		assertEquals("Target not downstream of threat?", 2, downstreamOfThreat.size());
		
		project.deleteObject(link);
		FactorSet nothingUpstream = builder.buildUpstreamChainAndGetFactors(target);
		assertEquals("Didn't reset upstream?", 1, nothingUpstream.size());
		FactorSet nothingDownstream = builder.buildNormalChainAndGetFactors(threat);
		assertEquals("Didn't reset downstream?", 1, nothingDownstream.size());
	}
	
	ProjectForTesting project;
}
