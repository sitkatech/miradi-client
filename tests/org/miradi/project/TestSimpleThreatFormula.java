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
package org.miradi.project;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAMTestCase;
import org.miradi.project.threatrating.SimpleThreatFormula;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class TestSimpleThreatFormula extends EAMTestCase
{

	public TestSimpleThreatFormula(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		
		project = new ProjectForTesting(getName());
		framework = project.getSimpleThreatRatingFramework();
		
		formula = new SimpleThreatFormula(framework);
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		project = null;
		super.tearDown();
	}
	
	public void testWithOldModel()
	{
		String BAD_LABEL = "Non-Existant Label";
		assertNull("found non-existant criterion?", framework.findCriterionByLabel(BAD_LABEL));
	}
	
	public void testComputeMagnitude()
	{
		verifyMagnitudeCalculation(4, 4, 4);
		verifyMagnitudeCalculation(4, 3, 3);
		verifyMagnitudeCalculation(4, 2, 2);
		verifyMagnitudeCalculation(4, 1, 1);
		verifyMagnitudeCalculation(3, 4, 3);
		verifyMagnitudeCalculation(3, 3, 3);
		verifyMagnitudeCalculation(3, 2, 2);
		verifyMagnitudeCalculation(3, 1, 1);
		verifyMagnitudeCalculation(2, 4, 2);
		verifyMagnitudeCalculation(2, 3, 2);
		verifyMagnitudeCalculation(2, 2, 2);
		verifyMagnitudeCalculation(2, 1, 1);
		verifyMagnitudeCalculation(1, 4, 1);
		verifyMagnitudeCalculation(1, 3, 1);
		verifyMagnitudeCalculation(1, 2, 1);
		verifyMagnitudeCalculation(1, 1, 1);
		
		verifyMagnitudeCalculation(0, 4, 0);
		verifyMagnitudeCalculation(0, 3, 0);
		verifyMagnitudeCalculation(0, 2, 0);
		verifyMagnitudeCalculation(0, 1, 0);
		
		verifyMagnitudeCalculation(1, 0, 0);
		verifyMagnitudeCalculation(2, 0, 0);
		verifyMagnitudeCalculation(3, 0, 0);
		verifyMagnitudeCalculation(4, 0, 0);
	}
	
	public void testInvalidScopeAndSeverity()
	{
		try
		{
			formula.computeMagnitude(0, 1);
		}
		catch (RuntimeException IgnoreExpected)
		{
		}
		try
		{
			formula.computeMagnitude(1, 0);
		}
		catch (RuntimeException IgnoreExpected)
		{
		}
	}
	
	public void verifyMagnitudeCalculation(int scope, int severity, int magnitude)
	{
		String label = "wrong magnitude for " + scope + " " + severity;
		assertEquals(label, magnitude, formula.computeMagnitude(scope, severity));
	}
	
	public void testComputeSeriousness()
	{
		verifySeriousnessCalculation(4, 4, 4);
		verifySeriousnessCalculation(4, 3, 4);
		verifySeriousnessCalculation(4, 2, 4);
		verifySeriousnessCalculation(4, 1, 3);
		verifySeriousnessCalculation(3, 4, 4);
		verifySeriousnessCalculation(3, 3, 3);
		verifySeriousnessCalculation(3, 2, 3);
		verifySeriousnessCalculation(3, 1, 2);
		verifySeriousnessCalculation(2, 4, 3);
		verifySeriousnessCalculation(2, 3, 2);
		verifySeriousnessCalculation(2, 2, 2);
		verifySeriousnessCalculation(2, 1, 1);
		verifySeriousnessCalculation(1, 4, 2);
		verifySeriousnessCalculation(1, 3, 1);
		verifySeriousnessCalculation(1, 2, 1);
		verifySeriousnessCalculation(1, 1, 1);
		
		verifySeriousnessCalculation(0, 4, 0);
		verifySeriousnessCalculation(0, 3, 0);
		verifySeriousnessCalculation(0, 2, 0);
		verifySeriousnessCalculation(0, 1, 0);
		
		verifySeriousnessCalculation(1, 0, 0);
		verifySeriousnessCalculation(2, 0, 0);
		verifySeriousnessCalculation(3, 0, 0);
		verifySeriousnessCalculation(4, 0, 0);

	}
	
	public void verifySeriousnessCalculation(int magnitude, int irreversibility, int seriousness)
	{
		String label = "wrong seriousness for " + magnitude + " " + irreversibility;
		assertEquals(label, seriousness, formula.computeSeriousness(magnitude, irreversibility));
	}

	public void testInvalidMagnitudeAndSeriousness()
	{
		try
		{
			formula.computeSeriousness(0, 1);
		}
		catch (RuntimeException IgnoreExpected)
		{
		}
		try
		{
			formula.computeSeriousness(1, 0);
		}
		catch (RuntimeException IgnoreExpected)
		{
		}
	}
	
	public void testComputeBundleValue()
	{
		verifyComputeBundleValue(0, 0, 0, 0);
		verifyComputeBundleValue(1, 1, 1, 1);
		verifyComputeBundleValue(2, 2, 2, 2);
		verifyComputeBundleValue(3, 3, 3, 3);
		verifyComputeBundleValue(4, 4, 4, 4);
	}
	
	public void verifyComputeBundleValue(int scope, int severity, int irreversibility, int bundleValue)
	{
		String label = "wrong bundle value for " + scope + " " + severity + " " 
												 + irreversibility + " " + bundleValue;
		assertEquals(label, formula.computeBundleValue(scope, severity, irreversibility), bundleValue);
		
	}
	
	public void testGetBundleValues() throws Exception
	{		
		BaseId scopeId = framework.findCriterionByLabel("Scope").getId();
		BaseId severityId = framework.findCriterionByLabel("Severity").getId();
		BaseId urgencyId = framework.findCriterionByLabel("Irreversibility").getId();
		
		BaseId veryHighId = framework.findValueOptionByNumericValue(4).getId();
		BaseId highId = framework.findValueOptionByNumericValue(3).getId();
		BaseId none = framework.findValueOptionByNumericValue(0).getId();
		
		ThreatRatingBundle bundle = new ThreatRatingBundle(new FactorId(1), new FactorId(2), none);
		
		assertEquals("empty bundle value not zero? ", 0, formula.computeBundleValue(bundle));

		bundle.setValueId(scopeId, veryHighId);
		bundle.setValueId(severityId, veryHighId);
		assertEquals("bundle missing value not zero?", 0, formula.computeBundleValue(bundle));
		bundle.setValueId(urgencyId, highId);
		
		assertEquals("right bundle value? ", 4, formula.computeBundleValue(bundle));
	}

	public void testGetSummaryOfBundles()
	{
		int[] bundles;
		
		bundles = new int[] {};
		assertEquals(0, formula.getSummaryOfBundlesWithTwoPrimeRule(bundles));
		
		bundles = new int[] {0, 0, 0,};
		assertEquals(0, formula.getSummaryOfBundlesWithTwoPrimeRule(bundles));

		bundles = new int[] {1, 1, 1, 1, 1, 1, 1,};
		assertEquals(1, formula.getSummaryOfBundlesWithTwoPrimeRule(bundles));
		
		bundles = new int[] {1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1,};
		assertEquals(2, formula.getSummaryOfBundlesWithTwoPrimeRule(bundles));

		bundles = new int[] {2, 2, 2, 2, 2, 2, 2, 2, 2, 2,};
		assertEquals(3, formula.getSummaryOfBundlesWithTwoPrimeRule(bundles));
		
		bundles = new int[] {3, 3, 3, 3, 3, 3,};
		assertEquals(4, formula.getSummaryOfBundlesWithTwoPrimeRule(bundles));		
		
		bundles = new int[] {2, 3, 1, 4, 2,};
		assertEquals(3, formula.getSummaryOfBundlesWithTwoPrimeRule(bundles));
		
		bundles = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3,};
		assertEquals(3, formula.getSummaryOfBundlesWithTwoPrimeRule(bundles));

		bundles = new int[] {4, 4,};
		assertEquals(4, formula.getSummaryOfBundlesWithTwoPrimeRule(bundles));
	}
	
	public void testGetHighestValue()
	{
		int targetRatings[];
		
		targetRatings = new int[] {0, 0, 0, 0};
		assertEquals(0, formula.getHighestValue(targetRatings));
		
		targetRatings = new int[] {0, 1, 1, 1};
		assertEquals(1, formula.getHighestValue(targetRatings));
		
		targetRatings = new int[] {0, 4, 2, 3, 3};
		assertEquals(4, formula.getHighestValue(targetRatings));
	}

	public void testGetMajority()
	{		
		int allTargets[];
		
		allTargets = new int[] {3, 3, 1};
		assertEquals(3, formula.getMajority(allTargets));

		allTargets = new int[] {3, 3, 3, 1};
		assertEquals(3, formula.getMajority(allTargets));
		
		allTargets = new int[] {2, 2, 2, 3, 3, 4, 4};
		assertEquals(3, formula.getMajority(allTargets));
		
		allTargets = new int[] {2, 2, 4, 4, 4, 4};
		assertEquals(4, formula.getMajority(allTargets));
		
		allTargets = new int[] {1, 1, 2, 2};
		assertEquals(1, formula.getMajority(allTargets));
		
		allTargets = new int[] {1};
		assertEquals(1, formula.getMajority(allTargets));

		try
		{
			allTargets = new int[] {33};
			assertEquals(1, formula.getMajority(allTargets));
		}
		catch (RuntimeException IgnoreExpected)
		{
		}

	}
	
	private ProjectForTesting project;
	private SimpleThreatRatingFramework framework;
	private SimpleThreatFormula formula;
}
