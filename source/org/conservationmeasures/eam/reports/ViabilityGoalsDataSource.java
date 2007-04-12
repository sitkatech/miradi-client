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
import org.conservationmeasures.eam.project.Project;

public class ViabilityGoalsDataSource extends CommonDataSource
{
	public ViabilityGoalsDataSource(Indicator indicator)
	{
		super();
		project = indicator.getObjectManager().getProject();
		list = new ORefList(Indicator.getObjectType(), indicator.getGoalIds());
		rowCount = list.size();
	}

	public Object getFieldValue(JRField field)
	{
		return currentGoal.getData(field.getName());
	}

	public boolean next() throws JRException 
	{
		if (super.next())
		{
			currentGoal = (Goal)project.findObject(list.get(rowCount));
			return true;
		}
		return false;
	}

	ORefList list;
	Indicator currentIndicator;
	Goal currentGoal;
	Project project;
} 
