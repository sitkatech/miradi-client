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
		FactorLink factorLink = getProject().createAndPopulateDirectThreatLink();
		ORef threatRef = factorLink.getUpstreamThreatRef();
		ORef targetRef = factorLink.getDownstreamTargetRef();
		Target target = Target.find(getProject(), targetRef);
		ORefList stressRefs = target.getStressRefs();
		assertEquals("wrong target stress count?", 1, stressRefs.size());
		
		Cause threat = Cause.find(getProject(), threatRef);
		ORefList threatStressRatingReferrerRefs1 = threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		assertEquals("wrong threat stress rating count?", 1, threatStressRatingReferrerRefs1.size());

		ThreatStressRatingEnsurer threatStressRatingEnsurer = new ThreatStressRatingEnsurer(getProject());
		getProject().addCommandExecutedListener(threatStressRatingEnsurer);
		CommandDeleteObject deleteFactorLinkCommand = new CommandDeleteObject(factorLink);
		getProject().executeCommand(deleteFactorLinkCommand);
		
		ORefList threatStressRatingReferrerRefs2 = threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		assertEquals("threat stress rating was not removed as a result of factor link deletion?", 0, threatStressRatingReferrerRefs2.size());
		
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(threatRef, targetRef);
		CommandCreateObject createFactorlLinkCommand = new CommandCreateObject(FactorLink.getObjectType(), extraInfo);
		getProject().executeCommand(createFactorlLinkCommand);
		ORefList threatStressRatingReferrerRefs3 = threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		assertEquals("threat stress rating was not created as a result of factor link creation?", 1, threatStressRatingReferrerRefs3.size());
	}
}
