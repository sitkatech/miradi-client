/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.project;

import org.miradi.main.TestCaseWithProject;
import org.miradi.project.StressBasedThreatFormula;

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
				assertEquals(scopeSeverity[scopeIndex][severityIndex], formula.computeSeverityByScope(scope[scopeIndex], severity[severityIndex]));
			}
		}
	}
	
	public void testComputeContributionByIrreversibility()
	{
		int[] contribution    = {4, 3, 2, 1, 0};
		int[] irreversibility = {4, 3, 2, 1, 0};
		
		int[][] contributionIrreversibility = 
		{
			/*Ir        Contribution  */	
			/*re   	 	 4  3  2  1  0*/
			/*ve   4 */	{4, 3, 3, 1, 0},	
			/*rs   3 */	{4, 3, 2, 1, 0},
			/*ib   2 */	{3, 2, 2, 1, 0},
			/*il   1 */	{3, 2, 1, 1, 0},
			/*ity  0 */ {0, 0, 0, 0, 0},
		};
		
		StressBasedThreatFormula formula = new StressBasedThreatFormula();
		for (int contributionIndex = 0; contributionIndex < contribution.length; ++contributionIndex)
		{
			for (int irreversibilityIndex = 0; irreversibilityIndex < irreversibility.length; ++irreversibilityIndex)
			{
				int computedValue = formula.computeContributionByIrreversibility(contribution[contributionIndex], irreversibility[irreversibilityIndex]);
				int expectedValue = contributionIrreversibility[contributionIndex][irreversibilityIndex]; 
				assertEquals(expectedValue, computedValue);
			}
		}
	}
	
	public void testComputeThreatStressRating()
	{
		int[] source = {4, 3, 2, 1, 0};
		int[] stress = {4, 3, 2, 1, 0};
		
		int[][] threatStressRating = 
		{
		 /*        source     */	
	     /*s	 	 4  3  2  1  0*/
		 /*t   4*/	{4, 4, 3, 2, 0},
		 /*r   3*/	{3, 3, 2, 1, 0},
		 /*e   2*/	{2, 2, 1, 1, 0},
		 /*s   1*/	{1, 1, 1, 1, 0},
		 /*s   0*/	{0, 0, 0, 0, 0},	
		};
		
		StressBasedThreatFormula formula = new StressBasedThreatFormula();
		for (int sourceIndex = 0; sourceIndex < source.length; ++sourceIndex)
		{
			for (int stressIndex = 0; stressIndex < stress.length; ++stressIndex)
			{
				int computedValue = formula.computeThreatStressRating(source[sourceIndex], stress[stressIndex]);
				int expectedValue = threatStressRating[sourceIndex][stressIndex];
				assertEquals(expectedValue, computedValue);
			}
		}
	}
	
	public void testIsInvalidValue()
	{
		StressBasedThreatFormula formula = new StressBasedThreatFormula();
		assertTrue(formula.isInvalidValue(-1));
		assertTrue(formula.isInvalidValue(5));
		
		assertFalse(formula.isInvalidValue(0));
		assertFalse(formula.isInvalidValue(1));
		assertFalse(formula.isInvalidValue(2));
		assertFalse(formula.isInvalidValue(3));
		assertFalse(formula.isInvalidValue(4));
	}
	
	public void testGetHighestWithValue()
	{
		StressBasedThreatFormula formula = new StressBasedThreatFormula();
		assertEquals(4, formula.getHighestWithValue(0, 0, 0, 44));
		assertEquals(3, formula.getHighestWithValue(0, 0, 44, 0));
		assertEquals(2, formula.getHighestWithValue(0, 44, 0, 0));
		assertEquals(1, formula.getHighestWithValue(44, 0, 0, 0));
		assertEquals(0, formula.getHighestWithValue(0, 0, 0, 0));
		
		assertEquals(4, formula.getHighestWithValue(1, 1, 1, 1));
		assertEquals(3, formula.getHighestWithValue(1, 1, 1, 0));
		assertEquals(2, formula.getHighestWithValue(1, 1, 0, 0));
		assertEquals(1, formula.getHighestWithValue(1, 0, 0, 0));
		assertEquals(0, formula.getHighestWithValue(0, 0, 0, 0));
	}
}
