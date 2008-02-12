/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.project;

import org.miradi.objecthelpers.ORef;
import org.miradi.questions.ChoiceItem;

abstract public class ThreatRatingFramework
{
	public ThreatRatingFramework(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public Project getProject()
	{
		return project;
	}

	abstract public ChoiceItem getThreatThreatRatingValue(ORef threatRef) throws Exception;
	
	protected Project project;
}
