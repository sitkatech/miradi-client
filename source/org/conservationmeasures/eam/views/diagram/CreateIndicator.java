/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;

public class CreateIndicator  extends CreateAnnotationDoer
{
	public int getAnnotationType()
	{
		return ObjectType.INDICATOR;
	}
	
	public String getAnnotationListTag()
	{
		return Factor.TAG_INDICATOR_IDS;
	}
}