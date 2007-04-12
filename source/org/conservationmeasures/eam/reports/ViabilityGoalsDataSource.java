/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;

public class ViabilityGoalsDataSource extends CommonDataSource
{
	public ViabilityGoalsDataSource(Indicator indicator)
	{
		super(indicator.getObjectManager().getProject());
		list = new ORefList(Indicator.getObjectType(), indicator.getGoalIds());
		setRowCount(list.size());
	}

	public Object getFieldValue(JRField field)
	{
		return currentGoal.getData(field.getName());
	}

	public boolean next() throws JRException 
	{
		if (super.next())
		{
			currentGoal = (Goal)project.findObject(list.get(getCurrentRow()));
			return true;
		}
		return false;
	}

	ORefList list;
	Indicator currentIndicator;
	Goal currentGoal;
} 
