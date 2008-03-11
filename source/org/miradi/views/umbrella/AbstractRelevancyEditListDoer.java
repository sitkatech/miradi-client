/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Objective;


abstract public class AbstractRelevancyEditListDoer extends AbstractEditListDoer
{
	protected boolean isInvalidSelection()
	{	
		return getSelectionRef().isInvalid();
	}
	
	protected ORef getSelectionRef()
	{
		ORefList refList = getSelectedHierarchies()[0];
		ORef ref = refList.getRefForType(getTypeToUse());
		return ref;
	}
	
	protected int getTypeToUse()
	{
		return Objective.getObjectType();
	}
}
