/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.ProgressReport;
import org.miradi.views.diagram.DeleteAnnotationDoer;

public class DeleteIndicatorProgressReportDoer extends DeleteAnnotationDoer
{
	protected BaseObject getParent(BaseObject annotationToDelete)
	{
		return getSingleSelected(Indicator.getObjectType());  
	}

	public String getAnnotationIdListTag()
	{
		return Indicator.TAG_PROGRESS_REPORT_REFS;
	}

	public int getAnnotationType()
	{
		return ProgressReport.getObjectType();
	}

	public String[] getDialogText()
	{
		return new String[] { EAM.text("Are you sure you want to delete this Progress Report?"),};
	}
}
