/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.CreateThreatStressRatingParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.project.ObjectManager;

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
