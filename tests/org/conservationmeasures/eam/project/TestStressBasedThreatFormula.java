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
		int[] scope = {4, 3, 2, 1, 0};
		int[] severity = {4, 3, 2, 1, 0};
		int[][] scopeSeverity = 
		{
		/*s      	  s c o p e   */
		/*e 		 4  3  2  1  0*/
		/*v  4*/	{4, 3, 2, 1, 0},
		/*e	 3*/	{3, 3, 2, 1, 0},
		/*r	 2*/	{2, 2, 2, 1, 0},
		/*i	 1*/	{1, 1, 1, 1, 0},
		/*ty 0*/	{0, 0, 0, 0, 0},
		};
		
		StressBasedThreatFormula formula = new StressBasedThreatFormula();
		for (int scopeIndex = 0; scopeIndex < scope.length; ++scopeIndex)
		{
			for (int severityIndex = 0; severityIndex < severity.length; ++severityIndex)
			{
				assertEquals(scopeSeverity[scopeIndex][severityIndex], formula.computeSevertyByScope(scope[scopeIndex], severity[severityIndex]));
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
