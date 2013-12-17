/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.migrations;

import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Indicator;
import org.miradi.objects.Target;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.StatusQuestion;

public class TestMigrationTo6 extends AbstractTestMigration
{
	public TestMigrationTo6(String name)
	{
		super(name);
	}

	public void testWithoutIndicators() throws Exception
	{
		verifyThresholdsAfterMigration(0, "", "");
	}
	
	public void testWithIndicatorEmptyThresholds() throws Exception
	{	
		verifyThresholdsAfterMigration(1, "", "");
	}
	
	public void testWithIndicatorWithoutThresholds() throws Exception
	{	
		verifyThresholdsAfterMigration(1, "", null);
	}
	
	public void testWithIndicatorWithEscapedThreshold() throws Exception
	{
		verifyThresholdsAfterMigration(1, "5 &amp; &lt; &gt; &quot; &apos; 100", "5 &amp; &lt; &gt; &quot; &apos; 100");
	}
	
	public void testWithIndicatorWithUnescapedThreshold() throws Exception
	{
		verifyThresholdsAfterMigration(1, "1 &amp; &lt; &gt; &quot; &apos; 2", "1 & < > \" ' 2");
	}
	
	private void verifyThresholdsAfterMigration(int indicatorCount, String expectedValue, String actualValue) throws Exception
	{
		Target target = getProject().createTarget();
		for (int index = 0; index < indicatorCount; ++index)
		{
			Indicator indicator = getProject().createIndicator(target);
			if (actualValue != null)
				getProject().addPoorThresholdValue(indicator, actualValue);
		}
		
		ProjectForTesting migratedProject = migrateProject(new VersionRange(5, 5));
		ORefList migratedIndicatorRefs = migratedProject.getIndicatorPool().getORefList();
		assertEquals("Incorrect number of indicators?", indicatorCount, migratedIndicatorRefs.size());
		for(ORef migratedIndicatorRef : migratedIndicatorRefs)
		{
			Indicator migratedIndicator = Indicator.find(migratedProject, migratedIndicatorRef);
			verifyThresholdValue(migratedIndicator.getThresholdDetailsMap(), expectedValue);
			verifyThresholdValue(migratedIndicator.getThresholdsMap().getCodeToUserStringMap(), expectedValue);
		}
		
		verifyFullCircleMigrations(new VersionRange(6, 6));
	}

	public void verifyThresholdValue(CodeToUserStringMap thresholdMap, String expectedThresholdValue)
	{
		String actualThresholdValue = thresholdMap.getUserString(StatusQuestion.POOR);
		assertEquals("Incorrect threshold value after migration?", expectedThresholdValue, actualThresholdValue);
	}
	
	@Override
	protected int getFromVersion()
	{
		return 5;
	}
	
	@Override
	protected int getToVersion()
	{
		return 6;
	}
}
