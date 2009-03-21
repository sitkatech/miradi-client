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
import org.miradi.objectdata.BooleanData;

public class TestThreatTargetChainObject extends TestCaseWithProject
{
	public TestThreatTargetChainObject(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		verifyThreatThreatTarget();
		verifyThreatTargetTarget();
		verifyThreatCauseTarget();
		veriftThreat1TargetThreat2Target();
		verifyThreatTarget1ThreatTarget2();		
	}

	private void verifyThreatThreatTarget() throws Exception
	{
		DiagramFactor threat1 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		getProject().enableAsThreat(threat1.getWrappedORef());
				
		DiagramFactor threat2 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		getProject().enableAsThreat(threat2.getWrappedORef());
		
		DiagramFactor target = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Target.getObjectType());
		
		//threat1 -> threat2 -> target
		getProject().createDiagramLinkAndAddToDiagram(threat1, threat2); 
		getProject().createDiagramLinkAndAddToDiagram(threat2, target);			
		
		ThreatTargetChainObject chainObject = new ThreatTargetChainObject(getProject());
		verifySingleUpstreamThreat(chainObject, threat2.getWrappedFactor(), target.getWrappedFactor());
		verifySingleDownstreamTarget(chainObject, threat1.getWrappedFactor(), target.getWrappedFactor());
		verifySingleDownstreamTarget(chainObject, threat2.getWrappedFactor(), target.getWrappedFactor());
	}
	
	private void verifyThreatTargetTarget() throws Exception
	{
		DiagramFactor threat1 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		getProject().enableAsThreat(threat1.getWrappedORef());
				
		DiagramFactor target1 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Target.getObjectType());
		DiagramFactor target2 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Target.getObjectType());
		
		//threat1 -> target1 -> target2
		getProject().createDiagramLinkAndAddToDiagram(threat1, target1); 
		getProject().createDiagramLinkAndAddToDiagram(target1, target2);			
		
		ThreatTargetChainObject chainObject = new ThreatTargetChainObject(getProject());
		verifySingleUpstreamThreat(chainObject, threat1.getWrappedFactor(), target1.getWrappedFactor());	
		verifyDoubleDownstreamTargets(chainObject, threat1.getWrappedFactor(), target1.getWrappedFactor(), target2.getWrappedFactor());
	}
	
	private void verifyThreatCauseTarget() throws Exception
	{
		//threat1 -> cause -> target
		verifyThreatCauseTargetWithOptionalBidi(BooleanData.BOOLEAN_FALSE);
		
		//threat1 <-> cause <-> target
		verifyThreatCauseTargetWithOptionalBidi(BooleanData.BOOLEAN_TRUE);
	}
	
	private void veriftThreat1TargetThreat2Target() throws Exception
	{
		DiagramFactor threat1 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		getProject().enableAsThreat(threat1.getWrappedORef());
				
		DiagramFactor threat2 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		getProject().enableAsThreat(threat2.getWrappedORef());
		
		DiagramFactor target = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Target.getObjectType());
		
		//threat1 -> target
		//threat2 -> target
		getProject().createDiagramLinkAndAddToDiagram(threat1, target); 
		getProject().createDiagramLinkAndAddToDiagram(threat2, target);			
		
		ThreatTargetChainObject chainObject = new ThreatTargetChainObject(getProject());
		verifyDoubleUpstreamThreats(chainObject, threat1.getWrappedFactor(), threat2.getWrappedFactor(), target.getWrappedFactor());	
		verifySingleDownstreamTarget(chainObject, threat1.getWrappedFactor(), target.getWrappedFactor());	
		verifySingleDownstreamTarget(chainObject, threat2.getWrappedFactor(), target.getWrappedFactor());
	}
	
	private void verifyThreatTarget1ThreatTarget2() throws Exception
	{
		DiagramFactor threat1 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		getProject().enableAsThreat(threat1.getWrappedORef());
				
		DiagramFactor target1 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Target.getObjectType());
		DiagramFactor target2 = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Target.getObjectType());
		
		//threat1 -> target1
		//threat1 -> target2
		getProject().createDiagramLinkAndAddToDiagram(threat1, target1); 
		getProject().createDiagramLinkAndAddToDiagram(threat1, target2);			
		
		ThreatTargetChainObject chainObject = new ThreatTargetChainObject(getProject());
		verifySingleUpstreamThreat(chainObject, threat1.getWrappedFactor(), target1.getWrappedFactor());	
		verifySingleUpstreamThreat(chainObject, threat1.getWrappedFactor(), target2.getWrappedFactor());
		verifyDoubleDownstreamTargets(chainObject, threat1.getWrappedFactor(), target1.getWrappedFactor(), target2.getWrappedFactor());	
	}

	private void verifyThreatCauseTargetWithOptionalBidi(String isBidirectionalTag) throws Exception
	{
		DiagramFactor threat = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		getProject().enableAsThreat(threat.getWrappedORef());
				
		DiagramFactor cause = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Cause.getObjectType());
		DiagramFactor target = getProject().createDiagramFactorWithWrappedRefLabelAndAddToDiagram(Target.getObjectType());
		
		getProject().createDiagramLinkAndAddToDiagram(threat, cause, isBidirectionalTag);
		getProject().createDiagramLinkAndAddToDiagram(cause, target, isBidirectionalTag);			
		
		ThreatTargetChainObject chainObject = new ThreatTargetChainObject(getProject());
		verifySingleUpstreamThreat(chainObject, threat.getWrappedFactor(), target.getWrappedFactor());
		verifySingleDownstreamTarget(chainObject, threat.getWrappedFactor(), target.getWrappedFactor());
	}

	private void verifySingleDownstreamTarget(ThreatTargetChainObject chainObject, Factor threat, Factor target)
	{
		HashSet<Factor> downStreamTargetsFromThreat1 = chainObject.getDownstreamTargetsFromThreat(threat);
		assertEquals("wrong target count?", 1, downStreamTargetsFromThreat1.size());
		assertTrue("wrong threat in list?", downStreamTargetsFromThreat1.contains(target));
	}
	
	private void verifyDoubleDownstreamTargets(ThreatTargetChainObject chainObject, Factor threat, Factor target1, Factor target2)
	{
		HashSet<Factor> downStreamTargetsFromThreat1 = chainObject.getDownstreamTargetsFromThreat(threat);
		assertEquals("wrong target count?", 2, downStreamTargetsFromThreat1.size());
		assertTrue("wrong threat in list?", downStreamTargetsFromThreat1.contains(target1));
		assertTrue("wrong threat in list?", downStreamTargetsFromThreat1.contains(target2));
	}
		
	private void verifySingleUpstreamThreat(ThreatTargetChainObject chainObject, Factor threat, Factor target)
	{
		HashSet<Factor> upstreamThreats = chainObject.getUpstreamThreatsFromTarget(target);
		assertEquals("wrong threat count?", 1, upstreamThreats.size());
		assertTrue("wrong threat in list?", upstreamThreats.contains(threat));
	}
	
	private void verifyDoubleUpstreamThreats(ThreatTargetChainObject chainObject, Factor threat1, Factor threat2, Factor target)
	{
		HashSet<Factor> upstreamThreats = chainObject.getUpstreamThreatsFromTarget(target);
		assertEquals("wrong threat count?", 2, upstreamThreats.size());
		assertTrue("wrong threat in list?", upstreamThreats.contains(threat1));
		assertTrue("wrong threat in list?", upstreamThreats.contains(threat2));
	}
}
