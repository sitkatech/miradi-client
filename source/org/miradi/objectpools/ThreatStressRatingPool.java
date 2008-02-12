/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.ObjectManager;

public class ThreatStressRatingPool extends EAMNormalObjectPool
{
	public ThreatStressRatingPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.THREAT_STRESS_RATING);
	}
	
	public ThreatStressRating find(BaseId id)
	{
		return (ThreatStressRating)findObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new ThreatStressRating(objectManager, actualId, (CreateThreatStressRatingParameter) extraInfo);
	}
}
