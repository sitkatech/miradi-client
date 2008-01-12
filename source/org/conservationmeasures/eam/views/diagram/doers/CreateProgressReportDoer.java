/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.objects.ProgressReport;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.views.diagram.CreateAnnotationDoer;

public class CreateProgressReportDoer extends CreateAnnotationDoer
{
	public String getAnnotationListTag()
	{
		return Strategy.TAG_PROGRESS_REPORT_REFS;
	}

	public int getAnnotationType()
	{
		return ProgressReport.getObjectType();
	}
}
