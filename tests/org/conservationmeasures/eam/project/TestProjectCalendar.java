/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.utils.DateRange;
import org.martus.util.TestCaseEnhanced;

public class TestProjectCalendar extends TestCaseEnhanced
{
	public TestProjectCalendar(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		ProjectCalendar pc = project.getProjectCalendar();
		try
		{
			project.getMetadata().setData(ProjectMetadata.TAG_START_DATE, "2006-01-01");
			project.getMetadata().setData(ProjectMetadata.TAG_EXPECTED_END_DATE, "2007-12-31");
			pc.rebuildProjectDateRanges();
			{
				Vector yearlyRanges = pc.getYearlyDateRanges();
				assertEquals(2, yearlyRanges.size());
				assertEquals("FY06", pc.getDateRangeName((DateRange)yearlyRanges.get(0)));
				assertEquals("FY07", pc.getDateRangeName((DateRange)yearlyRanges.get(1)));
			}

			project.getMetadata().setData(ProjectMetadata.TAG_FISCAL_YEAR_START, "7");
			pc.rebuildProjectDateRanges();
			{
				Vector yearlyRanges = pc.getYearlyDateRanges();
				assertEquals(3, yearlyRanges.size());
				assertEquals("FY06", pc.getDateRangeName((DateRange)yearlyRanges.get(0)));
				assertEquals("FY07", pc.getDateRangeName((DateRange)yearlyRanges.get(1)));
				assertEquals("FY08", pc.getDateRangeName((DateRange)yearlyRanges.get(2)));
			}

			project.getMetadata().setData(ProjectMetadata.TAG_START_DATE, "2006-01-02");
			project.getMetadata().setData(ProjectMetadata.TAG_EXPECTED_END_DATE, "2007-01-02");
			project.getMetadata().setData(ProjectMetadata.TAG_FISCAL_YEAR_START, "1");
			pc.rebuildProjectDateRanges();
			{
				Vector yearlyRanges = pc.getYearlyDateRanges();
				assertEquals(2, yearlyRanges.size());
				assertEquals("FY06", pc.getDateRangeName((DateRange)yearlyRanges.get(0)));
				assertEquals("FY07", pc.getDateRangeName((DateRange)yearlyRanges.get(1)));
			}
		}
		finally
		{
			project.close();
		}
	}
}
