/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;


public class ThreatRatingWizardSeverityStep extends ThreatRatingWizardSetValue
{
	public ThreatRatingWizardSeverityStep(ThreatRatingWizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse, "Severity");
	}
	
	public ThreatRatingWizardSeverityStep(ThreatRatingWizardPanel wizardToUse,  String critertion) throws Exception
	{
		super(wizardToUse, critertion);
	}
	
	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	String HTML_FILENAME = "ThreatRatingSeverity.html";
}
