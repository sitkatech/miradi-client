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
package org.miradi.objecthelpers;

import java.util.Vector;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.views.diagram.LinkCreator;
import org.miradi.views.diagram.LinkDeletor;

public class TestThreatStressRatingEnsurer extends TestCaseWithProject
{
	public TestThreatStressRatingEnsurer(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		getProject().disableThreatStressRatingEnsurer();
		setupSampleData();
		getProject().enableThreatStressRatingEnsurer();
	}
	
	private void setupSampleData() throws Exception
	{
		createTarget();
		createThreat();
		createFactorLink();		
		assertTrue("threat is not a direct threat?", threat.isDirectThreat());
		assertEquals("wrong factor Link count?", 1, getProject().getFactorLinkPool().getORefList().size());
		
		createStress();
		
		getProject().createThreatStressRating(stress.getRef(), threat.getRef());
		ORefList threatStressRatingReferrerRefs1 = threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		assertEquals("wrong threat stress rating count?", 1, threatStressRatingReferrerRefs1.size());
		
		ORefList threatStressRatingReferringToStressRefs = stress.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		assertEquals("wrong threat stress rating referring to stress count?", 1, threatStressRatingReferringToStressRefs.size());

		ORefList threatStressRatingPoolRefs = getProject().getThreatStressRatingPool().getORefList();
		assertEquals("wrong threat stress rating pool size count?", 1, threatStressRatingPoolRefs.size());
	}

	public void testCreateAndDeleteStress() throws Exception
	{
		deleteStress();
		
		assertEquals("stress was not deleted?", 0, getProject().getStressPool().size());
		assertEquals("threat stress rating was not deleted?", 0, getProject().getThreatStressRatingPool().size());
		verifyThreatStressRatingReferrersToThreat(0);
		
		createStress();
		
		verifyThreatStressRatingReferrersToThreat(1);
	}

	public void testCreateAndDeleteFactorLink() throws Exception
	{
		LinkDeletor deletor = new LinkDeletor(getProject());
		deletor.deleteDiagramLinkAndOrphandFactorLink(new Vector<DiagramFactor>(), diagramLink);
		
		verifyThreatStressRatingReferrersToThreat(0);

		LinkCreator creator = new LinkCreator(getProject());
		creator.createFactorLinkAndAddToDiagramUsingCommands(getProject().getTestingDiagramObject(), threatDiagramFactor, targetDiagramFactor);
		
		verifyThreatStressRatingReferrersToThreat(1);
	}
	
	public void testChangeDirectThreatStatus() throws Exception
	{
		getProject().disableAsThreat(threat);
		verifyThreatStressRatingReferrersToThreat(0);
		
		getProject().enableAsThreat(threat);
		verifyThreatStressRatingReferrersToThreat(1);
	}

	private void verifyThreatStressRatingReferrersToThreat(int expected)
	{
		ORefList threatStressRatingReferrerRefs = threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		assertEquals("incorrect threat stress ratings referring to threat?", expected, threatStressRatingReferrerRefs.size());
	}
	
	private void createFactorLink() throws Exception
	{
		diagramLink = getProject().createDiagramLinkAndAddToDiagramModel(threatDiagramFactor, targetDiagramFactor);
	}

	private void createThreat() throws Exception
	{
		threatDiagramFactor = getProject().createAndAddFactorToDiagram(Cause.getObjectType());
		threat = (Cause) threatDiagramFactor.getWrappedFactor();
		getProject().enableAsThreat(threat);
	}

	private void createTarget() throws Exception
	{
		targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		target = (Target) targetDiagramFactor.getWrappedFactor();
	}
	
	private void createStress() throws Exception
	{
		stress = getProject().createAndPopulateStress();
		CommandSetObjectData addStressCommand = CommandSetObjectData.createAppendORefCommand(target, Target.TAG_STRESS_REFS, stress.getRef());
		getProject().executeCommand(addStressCommand);
		
		ORefList stressRefs = target.getStressRefs();
		assertEquals("wrong target stress count?", 1, stressRefs.size());
	}
	
	private void deleteStress() throws Exception
	{
		CommandSetObjectData removeStressCommand = CommandSetObjectData.createRemoveORefCommand(target, Target.TAG_STRESS_REFS, stress.getRef());
		getProject().executeCommand(removeStressCommand);
		
		getProject().executeCommandsWithoutTransaction(stress.createCommandsToDeleteChildrenAndObject());
	}
	
	private Cause threat;
	private Target target;
	private DiagramFactor threatDiagramFactor;
	private DiagramFactor targetDiagramFactor;
	private DiagramLink diagramLink;
	private Stress stress;
}
