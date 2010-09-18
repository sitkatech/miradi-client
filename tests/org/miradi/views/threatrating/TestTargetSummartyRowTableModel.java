/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.threatrating;

import org.miradi.dialogs.threatrating.upperPanel.TargetSummaryRowTableModel;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.project.ProjectForTesting;

public class TestTargetSummartyRowTableModel extends TestCaseWithProject
{
	public TestTargetSummartyRowTableModel(String name)
	{
		super(name);
	}
	
	public void testModelFramework() throws Exception
	{
		assertTrue("is not in simple based threat rating?", getProject().isSimpleThreatRatingMode());
		
		verifyModelThreatRatingFramework();
		getProject().switchToStressBaseMode();
		verifyModelThreatRatingFramework();		
	}

	private void verifyModelThreatRatingFramework()
	{
		TargetSummaryRowTableModel model = new TargetSummaryRowTableModel(getProject());
		assertEquals("incorrect framework for target summary rating model?", model.getFramework(), getProject().getThreatRatingFramework());
	}
	
	public void testTargetSummaryRowValueForSimple() throws Exception
	{
		ORef targetRef = getProject().populateSimpleThreatRatingValues();
		TargetSummaryRowTableModel model = new TargetSummaryRowTableModel(getProject());
		Target target = Target.find(getObjectManager(), targetRef);
		int targetSummaryValue = model.calculateThreatSummaryRatingValue(target);
		int expectedTargetSummaryValue = getProject().getSimpleThreatRatingFramework().get2PrimeSummaryRatingValue(target);
		assertEquals("incorrect target summary simple based rating?", expectedTargetSummaryValue, targetSummaryValue);
	}
	
	public void testTargetSummaryRowValueForStressBasedMode() throws Exception
	{
		getProject().switchToStressBaseMode();
		
		DiagramLink threatTargetLink = getProject().createThreatTargetDiagramLink();
		getProject().createAndPopulateStress();
		Stress stress = getProject().createAndPopulateStress();
		ORef targetRef = ProjectForTesting.getDownstreamTargetRef(threatTargetLink);
		getProject().setObjectData(targetRef, Target.TAG_STRESS_REFS, new ORefList(stress).toString());
		getProject().createAndPopulateThreatStressRating(stress.getRef(), ProjectForTesting.getUpstreamThreatRef(threatTargetLink));
		TargetSummaryRowTableModel model = new TargetSummaryRowTableModel(getProject());
		Target target = Target.find(getProject(), targetRef);
		int targetSummaryValue = model.calculateThreatSummaryRatingValue(target);
		int expectedTargetSummaryCode = getProject().getStressBasedThreatRatingFramework().get2PrimeSummaryRatingValue(target);
		assertEquals("incorrect target summary rating?", expectedTargetSummaryCode, targetSummaryValue);
	}
}
