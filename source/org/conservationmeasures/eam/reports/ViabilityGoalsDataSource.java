/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Indicator;

public class ViabilityGoalsDataSource extends CommonDataSource
{
	public ViabilityGoalsDataSource(Indicator indicator)
	{
		super(indicator.getObjectManager().getProject());
		ORefList list = new ORefList(Indicator.getObjectType(), indicator.getGoalIds());
		setObjectList(list);
	}

	public Object getFieldValue(JRField field)
	{
		return getValue(field, getCurrentObject());
	}
} 
