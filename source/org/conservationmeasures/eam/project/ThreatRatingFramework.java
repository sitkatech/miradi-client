/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

public class ThreatRatingFramework
{
	public ThreatRatingFramework(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public Project getProject()
	{
		return project;
	}
			
	protected Project project;
}
