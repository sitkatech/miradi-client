package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestTNCThreatFormula extends EAMTestCase
{

	public TestTNCThreatFormula(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		
		framework = new ThreatRatingFramework(new IdAssigner());
		framework.createDefaultObjects();
		
		formula = new TNCThreatFormula(framework);
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
		verifySeriousnessCalculation(4, 2, 3);
		verifySeriousnessCalculation(4, 1, 2);
		verifySeriousnessCalculation(3, 4, 3);
		verifySeriousnessCalculation(3, 3, 3);
		verifySeriousnessCalculation(3, 2, 2);
		verifySeriousnessCalculation(3, 1, 1);
		verifySeriousnessCalculation(2, 4, 2);
		verifySeriousnessCalculation(2, 3, 2);
		verifySeriousnessCalculation(2, 2, 1);
		verifySeriousnessCalculation(2, 1, 1);
		verifySeriousnessCalculation(1, 4, 1);
		verifySeriousnessCalculation(1, 3, 1);
		verifySeriousnessCalculation(1, 2, 1);
		verifySeriousnessCalculation(1, 1, 1);		
	}
	
	public void verifySeriousnessCalculation(int magnitude, int urgency, int seriousness)
	{
		String label = "wrong seriousness for " + magnitude + " " + urgency;
		assertEquals(label, seriousness, formula.computeSeriousness(magnitude, urgency));
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
		verifyComputeBundleValue(3, 1, 2, 1);
		verifyComputeBundleValue(3, 3, 2, 2);
		verifyComputeBundleValue(4, 3, 4, 3);
		verifyComputeBundleValue(4, 4, 4, 4);
	}
	
	public void verifyComputeBundleValue(int scope, int severity, int urgency, int bundleValue)
	{
		String label = "wrong bundle value for " + scope + " " + severity + " " 
												 + urgency + " " + bundleValue;
		assertEquals(label, formula.computeBundleValue(scope, severity, urgency), bundleValue);
		
	}
	
	public void testGetBundleValues()
	{		
		int scopeId = framework.findCriterionByLabel("Scope").getId();
		int severityId = framework.findCriterionByLabel("Severity").getId();
		int urgencyId = framework.findCriterionByLabel("Custom1").getId();
		
		int veryHighId = framework.findValueOptionByNumericValue(4).getId();
		int highId = framework.findValueOptionByNumericValue(3).getId();
		
		ThreatRatingBundle bundle = new ThreatRatingBundle(1, 2);
		
		bundle.setValueId(scopeId, veryHighId);
		bundle.setValueId(severityId, veryHighId);
		bundle.setValueId(urgencyId, highId);
		
		assertEquals("right bundle value? ", 4, formula.computeBundleValue(bundle));
	}
	
	ThreatRatingFramework framework;
	TNCThreatFormula formula;
}
