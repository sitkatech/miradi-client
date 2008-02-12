/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.ProgressReport;
import org.miradi.views.diagram.CreateAnnotationDoer;

public class CreateIndicatortProgressReportDoer extends CreateAnnotationDoer
{
	public BaseObject getSelectedParent()
	{
		if (getPicker() == null)
			return null;
		
		ORefList selectionRefs = getPicker().getSelectedHierarchies()[0];
		ORef indicatorRef = selectionRefs.getRefForType(Indicator.getObjectType());
		
		return Indicator.find(getProject(), indicatorRef);
	}
	
	public String getAnnotationListTag()
	{
		return Indicator.TAG_PROGRESS_REPORT_REFS;
	}

	public int getAnnotationType()
	{
		return ProgressReport.getObjectType();
	}
}
