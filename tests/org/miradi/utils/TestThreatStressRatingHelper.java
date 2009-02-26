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
package org.miradi.utils;

import java.util.Vector;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objectpools.ThreatStressRatingPool;
import org.miradi.objects.FactorLink;
import org.miradi.objects.ThreatStressRating;

public class TestThreatStressRatingHelper extends TestCaseWithProject
{
	public TestThreatStressRatingHelper(String name)
	{
		super(name);
	}
	
	public void testGetRelatedThreatStressRatings() throws Exception 
	{
		FactorLink threatLink1 = getProject().createAndPopulateDirectThreatLink();
		ThreatStressRatingPool threatStressRatingPool = getProject().getThreatStressRatingPool();
		assertEquals("wrong TSR count?", 2, threatStressRatingPool.getORefList().size());
		
		ThreatStressRatingHelper helper = new ThreatStressRatingHelper(getProject());
		Vector<ThreatStressRating> relatedThreatStressRatings = helper.getRelatedThreatStressRatings(threatLink1.getFromFactorRef(), threatLink1.getToFactorRef());
		assertEquals("wrong related TSR count", 1, relatedThreatStressRatings.size());
	}
}
