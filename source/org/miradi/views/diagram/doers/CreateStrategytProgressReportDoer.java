/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import org.miradi.objects.ProgressReport;
import org.miradi.objects.Strategy;
import org.miradi.views.diagram.CreateAnnotationDoer;

public class CreateStrategytProgressReportDoer extends CreateAnnotationDoer
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
