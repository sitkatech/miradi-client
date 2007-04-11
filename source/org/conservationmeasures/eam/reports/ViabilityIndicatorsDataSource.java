/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;

public class ViabilityIndicatorsDataSource implements JRDataSource
{
	public ViabilityIndicatorsDataSource(KeyEcologicalAttribute kea)
	{
		project = kea.getObjectManager().getProject();
		list = new ORefList(Indicator.getObjectType(), kea.getIndicatorIds());
		count = list.size();
	}

	public Object getFieldValue(JRField field) throws JRException
	{
		return getData(field.getName());
	}

	public boolean next() throws JRException 
	{
		if (--count>=0)
		{
			currentIndicator = (Indicator)project.findObject(list.get(count));
			BaseId goalId = currentIndicator.getGoalIds().get(0);
			currentGoal = (Goal)project.findObject(Goal.getObjectType(),goalId);
			return true;
		}
		return false;
	}

	public String getData(String name)
	{
		return currentIndicator.getData(name);
	}
	
	int count;
	ORefList list;
	Indicator currentIndicator;
	Goal currentGoal;
	Project project;
} 
