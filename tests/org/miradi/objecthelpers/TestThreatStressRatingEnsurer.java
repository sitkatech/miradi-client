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

import java.text.ParseException;

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;

//FIXME This test is under construction along with its matching class
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
		deleteObject(factorLink);
		
		verifyThreatStressRatingReferrersToThreat(0);
		
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(threat.getRef(), target.getRef());
		CommandCreateObject createFactorlLinkCommand = new CommandCreateObject(FactorLink.getObjectType(), extraInfo);
		getProject().executeCommand(createFactorlLinkCommand);
		
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
	
	private void deleteObject(BaseObject objectToDelet) throws CommandFailedException
	{
		CommandDeleteObject deleteFactorLinkCommand = new CommandDeleteObject(objectToDelet);
		getProject().executeCommand(deleteFactorLinkCommand);
	}
	
	private void createFactorLink() throws Exception
	{
		ORef factorLinkRef = getProject().createFactorLink(threat.getRef(), target.getRef());
		factorLink = FactorLink.find(getProject(), factorLinkRef);
	}

	private void createThreat() throws Exception
	{
		threat = getProject().createCause();
		getProject().enableAsThreat(threat);
	}

	private void createTarget() throws Exception
	{
		target = getProject().createTarget();
	}
	
	private void createStress() throws Exception
	{
		stress = getProject().createAndPopulateStress();
		CommandSetObjectData addStressCommand = CommandSetObjectData.createAppendORefCommand(target, Target.TAG_STRESS_REFS, stress.getRef());
		getProject().executeCommand(addStressCommand);
		
		ORefList stressRefs = target.getStressRefs();
		assertEquals("wrong target stress count?", 1, stressRefs.size());
	}
	
	private void deleteStress() throws ParseException, CommandFailedException
	{
		CommandSetObjectData removeStressCommand = CommandSetObjectData.createRemoveORefCommand(target, Target.TAG_STRESS_REFS, stress.getRef());
		getProject().executeCommand(removeStressCommand);
		
		getProject().executeCommandsWithoutTransaction(stress.createCommandsToClear());
		deleteObject(stress);
	}
	
	private Cause threat;
	private Target target;
	private FactorLink factorLink;
	private Stress stress;
}
