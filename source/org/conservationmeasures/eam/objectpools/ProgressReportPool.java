/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ProgressReport;
import org.conservationmeasures.eam.project.ObjectManager;

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
