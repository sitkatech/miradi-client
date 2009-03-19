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

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.main.TestCaseWithProject;
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
	
	public void testBasics() throws Exception
	{
		Target target = getProject().createTarget();
		Cause threat = getProject().createCause();
		ORef factorLinkRef = getProject().createFactorLink(threat.getRef(), target.getRef());
		FactorLink factorLink = FactorLink.find(getProject(), factorLinkRef);
		assertTrue("threat is not a direct threat?", threat.isDirectThreat());
		assertEquals("wrong factor Link count?", 1, getProject().getFactorLinkPool().getORefList().size());
		
		ORef createdStressRef = getProject().createAndPopulateStress().getRef();
		getProject().setObjectData(target.getRef(), Target.TAG_STRESS_REFS, new ORefList(createdStressRef).toString());
		ORefList stressRefs = target.getStressRefs();
		assertEquals("wrong target stress count?", 1, stressRefs.size());
		
		ORef stressRef = stressRefs.getRefForType(Stress.getObjectType());
		getProject().createThreatStressRating(stressRef, threat.getRef());
		ORefList threatStressRatingReferrerRefs1 = threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		assertEquals("wrong threat stress rating count?", 1, threatStressRatingReferrerRefs1.size());
		
		Stress stress = Stress.find(getProject(), stressRef);
		ORefList threatStressRatingReferringToStressRefs = stress.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		assertEquals("wrong threat stress rating referring to stress count?", 1, threatStressRatingReferringToStressRefs.size());

		ORefList threatStressRatingPoolRefs = getProject().getThreatStressRatingPool().getORefList();
		assertEquals("wrong threat stress rating pool size count?", 1, threatStressRatingPoolRefs.size());
		
		ThreatStressRatingEnsurer threatStressRatingEnsurer = new ThreatStressRatingEnsurer(getProject());
		getProject().addCommandExecutedListener(threatStressRatingEnsurer);
		CommandDeleteObject deleteFactorLinkCommand = new CommandDeleteObject(factorLink);
		getProject().executeCommand(deleteFactorLinkCommand);
		
		ORefList threatStressRatingReferrerRefs2 = threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		assertEquals("threat stress rating was not removed as a result of factor link deletion?", 0, threatStressRatingReferrerRefs2.size());
		
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(threat.getRef(), target.getRef());
		CommandCreateObject createFactorlLinkCommand = new CommandCreateObject(FactorLink.getObjectType(), extraInfo);
		getProject().executeCommand(createFactorlLinkCommand);
		ORefList threatStressRatingReferrerRefs3 = threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		assertEquals("threat stress rating was not created as a result of factor link creation?", 1, threatStressRatingReferrerRefs3.size());
	}
}
