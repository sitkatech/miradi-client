/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.ProgressReport;
import org.conservationmeasures.eam.views.diagram.CreateAnnotationDoer;

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
