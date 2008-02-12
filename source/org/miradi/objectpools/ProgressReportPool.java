/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProgressReport;
import org.miradi.project.ObjectManager;

public class ProgressReportPool extends EAMNormalObjectPool
{
	public ProgressReportPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.PROGRESS_REPORT);
	}
	
	public void put(ProgressReport progressReport)
	{
		put(progressReport.getId(), progressReport);
	}
	
	public ProgressReport find(BaseId id)
	{
		return (ProgressReport) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new ProgressReport(objectManager, actualId);
	}
}
