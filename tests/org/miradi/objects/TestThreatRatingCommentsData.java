/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ProjectForTesting;
import org.miradi.schemas.ThreatSimpleRatingDataSchema;
import org.miradi.schemas.ThreatStressRatingDataSchema;

public class TestThreatRatingCommentsData extends ObjectTestCase
{
	public TestThreatRatingCommentsData(String name)
	{
		super(name);
	}
	
	public void testSimpleRatingDataFields() throws Exception
	{
		verifyFields(ThreatSimpleRatingDataSchema.getObjectType());
	}
	
	public void testStressRatingDataFields() throws Exception
	{
		verifyFields(ThreatStressRatingDataSchema.getObjectType());
	}

	public void testFindComment() throws Exception
	{
		Cause cause = getProject().createCause();
		Target target = getProject().createTarget();

		assertTrue("project is not in simple threat rating mode?", getProject().isSimpleThreatRatingMode());
		getProject().populateSimpleThreatRatingCommentsData(cause.getRef(), target.getRef(), ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);

		AbstractThreatRatingData threatRatingData = AbstractThreatRatingData.findThreatRatingData(getProject(), cause.getRef(), target.getRef(), ObjectType.THREAT_SIMPLE_RATING_DATA);
		assertNotNull(threatRatingData);
		String simpleThreatRatingComments = threatRatingData.getComments();
		assertEquals("wrong simple based threat rating comment?", ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT, simpleThreatRatingComments);

		getProject().switchToStressBaseMode();
		assertTrue("project is not in stress based threat rating mode?", getProject().isStressBaseMode());
		getProject().populateStressThreatRatingCommentsData(cause.getRef(), target.getRef(), ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT);

		threatRatingData = AbstractThreatRatingData.findThreatRatingData(getProject(), cause.getRef(), target.getRef(), ObjectType.THREAT_STRESS_RATING_DATA);
		assertNotNull(threatRatingData);
		String stressBasedThreatRatingComments = threatRatingData.getComments();
		assertEquals("wrong stress based threat rating comment?", ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT, stressBasedThreatRatingComments);
	}

	public void testDeleteThreat() throws Exception
	{
		Cause cause = getProject().createCause();
		Target target = getProject().createTarget();

		getProject().switchToStressBaseMode();
		assertTrue("project is not in stress based threat rating mode?", getProject().isStressBaseMode());
		getProject().populateStressThreatRatingCommentsData(cause.getRef(), target.getRef(), ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT);

		AbstractThreatRatingData threatRatingData = AbstractThreatRatingData.findThreatRatingData(getProject(), cause.getRef(), target.getRef(), ObjectType.THREAT_STRESS_RATING_DATA);
		assertNotNull(threatRatingData);
		String stressBasedThreatRatingComments = threatRatingData.getComments();
		assertEquals("wrong stress based threat rating comment?", ProjectForTesting.STRESS_BASED_THREAT_RATING_COMMENT, stressBasedThreatRatingComments);

		getProject().executeCommands(cause.createCommandsToDeleteChildrenAndObject());

		threatRatingData = AbstractThreatRatingData.findThreatRatingData(getProject(), cause.getRef(), target.getRef(), ObjectType.THREAT_STRESS_RATING_DATA);
		assertNull(threatRatingData);
	}

	public void testDeleteTarget() throws Exception
	{
		Cause cause = getProject().createCause();
		Target target = getProject().createTarget();

		assertTrue("project is not in simple threat rating mode?", getProject().isSimpleThreatRatingMode());
		getProject().populateSimpleThreatRatingCommentsData(cause.getRef(), target.getRef(), ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT);

		AbstractThreatRatingData threatRatingData = AbstractThreatRatingData.findThreatRatingData(getProject(), cause.getRef(), target.getRef(), ObjectType.THREAT_SIMPLE_RATING_DATA);
		assertNotNull(threatRatingData);
		String simpleThreatRatingComments = threatRatingData.getComments();
		assertEquals("wrong simple based threat rating comment?", ProjectForTesting.SIMPLE_THREAT_RATING_COMMENT, simpleThreatRatingComments);

		getProject().executeCommands(target.createCommandsToDeleteChildrenAndObject());

		threatRatingData = AbstractThreatRatingData.findThreatRatingData(getProject(), cause.getRef(), target.getRef(), ObjectType.THREAT_SIMPLE_RATING_DATA);
		assertNull(threatRatingData);
	}
}
