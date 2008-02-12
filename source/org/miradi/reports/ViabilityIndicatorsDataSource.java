/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.reports;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;

public class ViabilityIndicatorsDataSource extends CommonDataSource
{
	public ViabilityIndicatorsDataSource(KeyEcologicalAttribute kea)
	{
		super(kea.getProject());
		ORefList list = new ORefList(Indicator.getObjectType(), kea.getIndicatorIds());
		setObjectList(list);
	}
} 
