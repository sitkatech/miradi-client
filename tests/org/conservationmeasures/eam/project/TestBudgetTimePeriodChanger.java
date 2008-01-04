/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.main.TestCaseWithProject;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.questions.BudgetCostUnitQuestion;
import org.conservationmeasures.eam.questions.BudgetTimePeriodQuestion;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.DateRangeEffort;
import org.martus.util.MultiCalendar;

public class TestBudgetTimePeriodChanger extends TestCaseWithProject
{
	public TestBudgetTimePeriodChanger(String name)
	{
		super(name);
	}

	public void testConvertQuarterlyToYearly() throws Exception
	{
		getProject().getMetadata().setData(ProjectMetadata.TAG_WORKPLAN_TIME_UNIT, BudgetTimePeriodQuestion.BUDGET_BY_QUARTER_CODE);
		getProject().getMetadata().setData(ProjectMetadata.TAG_FISCAL_YEAR_START, "7");
		getProjectCalendar().clearDateRanges();

		Assignment a1 = createAssignment();
		
		Assignment a2 = createAssignment();
		createAndAddDateRangeEffort(a2, "2007-01-01", 1.0);
		
		Assignment a3 = createAssignment();
		createAndAddDateRangeEffort(a3, "2007-01-01", 2.0);
		createAndAddDateRangeEffort(a3, "2007-03-01", 3.0);
		createAndAddDateRangeEffort(a3, "2007-09-01", 4.0);
		
		BudgetTimePeriodChanger.convertQuarterlyToYearly(getProject());
		
		{
			DateRange fiscalYear2007 = getProjectCalendar().createYear("2006-07-01");
			DateRange fiscalYear2008 = getProjectCalendar().createYear("2007-07-01");
			
			assertEquals(0, a1.getDateRangeEffortList().size());
			
			DateRangeEffortList list2 = a2.getDateRangeEffortList();
			assertEquals(1, list2.size());
			DateRangeEffort list2Effort = list2.get(0);
			assertEquals(1.0, list2Effort.getUnitQuantity());
			assertEquals(fiscalYear2007, list2Effort.getDateRange());
			
			DateRangeEffortList list3 = a3.getDateRangeEffortList();
			assertEquals(2, list3.size());
			
			DateRangeEffort list3Year1 = list3.get(0);
			assertEquals(5.0, list3Year1.getUnitQuantity());
			assertEquals(fiscalYear2007, list3Year1.getDateRange());
			
			DateRangeEffort list3Year2 = list3.get(1);
			assertEquals(4.0, list3Year2.getUnitQuantity());
			assertEquals(fiscalYear2008, list3Year2.getDateRange());
		}
		
		BudgetTimePeriodChanger.convertYearlyToQuarterly(getProject());

		{
			DateRange fiscalQ12007 = getProjectCalendar().createQuarter("2006-07-01");
			DateRange fiscalQ12008 = getProjectCalendar().createQuarter("2007-07-01");
			
			assertEquals(0, a1.getDateRangeEffortList().size());
			
			DateRangeEffortList list2 = a2.getDateRangeEffortList();
			assertEquals(1, list2.size());
			DateRangeEffort list2Effort = list2.get(0);
			assertEquals(1.0, list2Effort.getUnitQuantity());
			assertEquals(fiscalQ12007, list2Effort.getDateRange());
			
			DateRangeEffortList list3 = a3.getDateRangeEffortList();
			assertEquals(2, list3.size());
			
			DateRangeEffort list3Year1 = list3.get(0);
			assertEquals(5.0, list3Year1.getUnitQuantity());
			assertEquals(fiscalQ12007, list3Year1.getDateRange());
			
			DateRangeEffort list3Year2 = list3.get(1);
			assertEquals(4.0, list3Year2.getUnitQuantity());
			assertEquals(fiscalQ12008, list3Year2.getDateRange());

		}
	}

	private void createAndAddDateRangeEffort(Assignment assignment, String startDate, double effort) throws Exception
	{
		DateRangeEffortList drel = assignment.getDateRangeEffortList();
		drel.add(createQuarterlyEffort(startDate, effort));
		assignment.setData(Assignment.TAG_DATERANGE_EFFORTS, drel.toString());
	}
	
	private DateRangeEffort createQuarterlyEffort(String startDate, double units) throws Exception
	{
		MultiCalendar start = MultiCalendar.createFromIsoDateString(startDate);
		DateRange dateRange = getProjectCalendar().createQuarter(start);
		String costUnitCode = BudgetCostUnitQuestion.DAYS_CODE;
		DateRangeEffort dre = new DateRangeEffort(costUnitCode, units, dateRange);
		return dre;
	}

	private ProjectCalendar getProjectCalendar()
	{
		return getProject().getProjectCalendar();
	}

	private Assignment createAssignment() throws Exception
	{
		return Assignment.find(getProject(), getProject().createObject(Assignment.getObjectType()));
	}
}
