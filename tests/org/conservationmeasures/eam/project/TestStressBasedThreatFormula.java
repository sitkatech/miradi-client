/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.main.TestCaseWithProject;

public class TestStressBasedThreatFormula extends TestCaseWithProject
{
	public TestStressBasedThreatFormula(String name)
	{
		super(name);
	}
	
	public void testComputeSevertyByScope()
	{
		int[] scope = {4, 3, 2, 1};
		int[] severity = {4, 3, 2, 1};
		int[][] scopeSeverity = 
		{
 		/*		 4  3  2 1 */
		/*4*/	{4, 3, 2, 1},
		/*3*/	{3, 3, 2, 1},
		/*2*/	{2, 2, 2, 1},
		/*1*/	{1, 1, 1, 1},
		};
		
		StressBasedThreatFormula formula = new StressBasedThreatFormula();
		for (int scopeI = 0; scopeI < scope.length; ++scopeI)
		{
			for (int severityI = 0; severityI < severity.length; ++severityI)
			{
				assertEquals(scopeSeverity[scopeI][severityI], formula.computeSevertyByScope(scope[scopeI], severity[severityI]));
			}
		}
		
		try
		{
			formula.computeSevertyByScope(-1, 0);
			formula.computeSevertyByScope(5, 0);
			
			formula.computeSevertyByScope(0, -1);
			formula.computeSevertyByScope(0, 5);
			fail();
		}
		catch(Exception e)
		{
		}
	}
}
