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

import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ThreatTargetVirtualLink;
import org.miradi.objects.Cause;
import org.miradi.objects.FactorLink;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.threatrating.StressBasedThreatRatingFramework;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;

public class TestStressBasedThreatRatingFramework extends TestCaseWithProject
{
	public TestStressBasedThreatRatingFramework(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		getProject().setMetadata(ProjectMetadata.TAG_THREAT_RATING_MODE, ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
	}

	public void testGetSummaryRating() throws Exception
	{
		Target target = getProject().createTarget();
		Cause threat = getProject().createCause();
		
		getProject().enableAsThreat(threat);
		createThreatFactorLink(threat, target);
	
		StressBasedThreatRatingFramework frameWork = new StressBasedThreatRatingFramework(getProject());
		assertEquals("wrong summary rating for target?", 3, frameWork.get2PrimeSummaryRatingValue(target));
	}

	public void testGetRollupRatingOfThreats() throws Exception
	{
		createFactorLinkWithThreatStressRating();
		createFactorLinkWithThreatStressRating();
		createFactorLinkWithThreatStressRating();
		createFactorLinkWithThreatStressRating();
		
		StressBasedThreatRatingFramework frameWork = new StressBasedThreatRatingFramework(getProject());
		assertEquals("wrong rollup rating of threats?", 3, frameWork.getRollupRatingOfThreats());
	}
	
	private void createThreatFactorLink(Cause cause, Target target) throws Exception
	{
		ORef threatLinkRef = getProject().createFactorLink(cause.getRef(), target.getRef());
		FactorLink factorLink = FactorLink.find(getProject(), threatLinkRef);
		
		Stress stress = getProject().createAndPopulateStress();
		ORefList stressRefs = new ORefList(stress);
		ORef targetRef = ProjectForTesting.getDownstreamTargetRef(factorLink);
		getProject().setObjectData(targetRef, Target.TAG_STRESS_REFS, stressRefs.toString());	
		
		populateWithThreatStressRating(factorLink, stress.getRef());
		populateWithThreatStressRating(factorLink, stress.getRef());
		populateWithThreatStressRating(factorLink, stress.getRef());
	}
	
	private void createFactorLinkWithThreatStressRating() throws Exception
	{
		ORef threatLinkRef = getProject().createThreatTargetLink();
		FactorLink factorLink = FactorLink.find(getProject(), threatLinkRef);
		
		Stress stress = getProject().createAndPopulateStress();
		ORef targetRef = ProjectForTesting.getDownstreamTargetRef(factorLink);
		ORefList stressRefs = new ORefList(stress);
		getProject().setObjectData(targetRef, Target.TAG_STRESS_REFS, stressRefs.toString());	
		getProject().populateDirectThreatLink(factorLink, stressRefs);
		
		populateWithThreatStressRating(factorLink, stress.getRef());
		populateWithThreatStressRating(factorLink, stress.getRef());
		populateWithThreatStressRating(factorLink, stress.getRef());
		populateWithThreatStressRating(factorLink, stress.getRef());
		
		ThreatTargetVirtualLink threatTargetVirtualLink = new ThreatTargetVirtualLink(getProject());
		assertEquals(4, threatTargetVirtualLink.calculateThreatRatingBundleValue(ProjectForTesting.getUpstreamThreatRef(factorLink), targetRef));
	}

	private void populateWithThreatStressRating(FactorLink factorLink, ORef stressRef) throws Exception
	{
		getProject().createAndPopulateThreatStressRating(stressRef, ProjectForTesting.getUpstreamThreatRef(factorLink));
	}
	
	public void testGetTargetMajorityRating() throws Exception
	{
		Stress stress = getProject().createAndPopulateStress();
		assertEquals("wrong stress rating?" , 3, stress.calculateStressRating());
	
		Cause threat = getProject().createCause();		
		getProject().enableAsThreat(threat);

		ThreatStressRating threatStressRating = getProject().createAndPopulateThreatStressRating(stress.getRef(), threat.getRef());
		assertEquals("wrong threat stress rating?" , 3, threatStressRating.calculateThreatRating());
		
		Target target = getProject().createTarget();
		target.setData(Target.TAG_STRESS_REFS, new ORefList(stress.getRef()).toString());
		
		getProject().createFactorLink(threat.getRef(), target.getRef());
		
		StressBasedThreatRatingFramework framework = getProject().getStressBasedThreatRatingFramework();
		int targetMajorityRating = framework.getTargetMajorityRating();
		assertEquals("wrong target majority rating?", 3, targetMajorityRating);
		
		Target emptyTarget = getProject().createTarget();
		Cause emptyThreat = getProject().createCause();
		getProject().createFactorLink(emptyThreat.getRef(), emptyTarget.getRef());
		int targetMajorityRatingWithTwoTargets = framework.getTargetMajorityRating();
		assertEquals("wrong target majority rating?", 3, targetMajorityRatingWithTwoTargets);
	}
	
	public void testGetThreatThreatRatingValue() throws Exception
	{
		Target target = getProject().createTarget();
		Cause threat = getProject().createCause();
		createThreatFactorLink(threat, target);
		
		getProject().enableAsThreat(threat);		
		StressBasedThreatRatingFramework framework = getProject().getStressBasedThreatRatingFramework();
		assertEquals("wrong threat threatRating value?", "3", framework.getThreatThreatRatingValue(threat.getRef()).getCode());
	}
}
