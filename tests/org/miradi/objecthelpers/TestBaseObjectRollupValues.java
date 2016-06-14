/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.objecthelpers;

import org.martus.util.MultiCalendar;
import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.Indicator;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.OptionalDouble;

public class TestBaseObjectRollupValues extends TestCaseWithProject
{
	public TestBaseObjectRollupValues(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		year2009Q1 = new DateUnit("2009Q1");
		year2009 = getProject().createDateUnit(2009);
	}
	
	public void testDoNotOverrideExpensesWithZero() throws Exception
	{
		getProject().setProjectStartDate(MultiCalendar.createFromGregorianYearMonthDay(2009, 1, 1));
		getProject().setProjectEndDate(MultiCalendar.createFromGregorianYearMonthDay(2009, 12, 31));

		Strategy strategy = getProject().createStrategy();
		getProject().addExpenseAssignment(strategy, year2009, 3);
		
		Task activity = getProject().createTask(strategy);
		getProject().addExpenseAssignment(activity, year2009, 0);

		TimePeriodCosts strategyTimePeriodCosts = strategy.calculateTimePeriodCostsForAssignments(year2009);
		OptionalDouble total = strategyTimePeriodCosts.getTotalExpense();
		assertEquals("Super value should not be overridden by 0 value?", 3.0, total.getValue());
	}
	
	public void testDoNotOverrideWorkUnitsWithZero() throws Exception
	{
		getProject().setProjectStartDate(MultiCalendar.createFromGregorianYearMonthDay(2009, 1, 1));
		getProject().setProjectEndDate(MultiCalendar.createFromGregorianYearMonthDay(2009, 12, 31));

		Strategy strategy = getProject().createStrategy();
		getProject().addResourceAssignment(strategy, 3, year2009);
		
		Task activity = getProject().createTask(strategy);
		getProject().addResourceAssignment(activity, 0, year2009);
		
		TimePeriodCosts strategyTimePeriodCosts = strategy.calculateTimePeriodCostsForAssignments(year2009);
		OptionalDouble total = strategyTimePeriodCosts.getTotalWorkUnits();
		assertEquals("Super value should not be overridden by 0 value?", 3.0, total.getValue());
	}
	
	public void testRollupResourceAssignmentsWithNoQuantities() throws Exception
	{
		ResourceAssignment assignment = getProject().createResourceAssignment();
		getProject().addAccountingCode(assignment);
		assignment.setData(ResourceAssignment.TAG_DATEUNIT_DETAILS, new DateUnitEffortList().toString());

		Task activityWithResourceAssignment = getProject().createActivity();
		IdList currentAssignmentIdList = activityWithResourceAssignment.getSafeIdListData(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
		currentAssignmentIdList.add(assignment.getId());
		activityWithResourceAssignment.setData(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, currentAssignmentIdList.toString());
		
		assertNotEquals("Ignored the resource assignment who?", 0, activityWithResourceAssignment.getTotalTimePeriodCostsMapForAssignments().size());
	}
	
	public void testRollupExpenseAssignmentsWithNoQuantities() throws Exception
	{
		Task activityWithExpenseAssignment = getProject().createActivity();
		ExpenseAssignment expenseAssignment = getProject().addExpenseAssignment(activityWithExpenseAssignment, new DateUnitEffortList());
		ORef accountingCodeRef = getProject().createAccountingCode().getRef();
		expenseAssignment.setData(ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, accountingCodeRef.toString());
		assertNotEquals("Ignored the expense assignment who?", 0, activityWithExpenseAssignment.getTotalTimePeriodCostsMapForAssignments().size());
	}
	
	public void testRollupExpenseAssignmentsWithNoAssignments() throws Exception
	{
		Task activityWithNoAssignments = getProject().createActivity();
		assertEquals("Had a who?", 0, activityWithNoAssignments.getTotalTimePeriodCostsMapForAssignments().size());
	}
	
	private DateUnit year2009Q1;
	private DateUnit year2009;
}
