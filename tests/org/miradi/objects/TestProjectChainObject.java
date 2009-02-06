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
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Target;
import org.miradi.project.ProjectChainObject;
import org.miradi.project.ProjectForTesting;

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
		ORef linkRef = project.createObject(FactorLink.getObjectType(), extraInfo);
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
