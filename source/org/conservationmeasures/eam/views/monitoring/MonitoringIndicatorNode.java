/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;
import org.conservationmeasures.eam.views.TreeTableNode;

public class MonitoringIndicatorNode extends MonitoringNode
{
	public MonitoringIndicatorNode(Project projectToUse, Indicator indicatorToUse)
	{
		project = projectToUse;
		indicator = indicatorToUse;
	}
	
	public BaseObject getObject()
	{
		return indicator;
	}

	public ORef getObjectReference()
	{
		return indicator.getRef();
	}
	
	public int getType()
	{
		return indicator.getType();
	}

	public String toString()
	{
		return indicator.toString();
	}

	public int getChildCount()
	{
		return 0;
	}

	public TreeTableNode getChild(int index)
	{
		return null;
	}

	public Object getValueAt(int column)
	{
		String tag = COLUMN_TAGS[column];
		String rawValue = project.getObjectData(getType(), getObjectReference().getObjectId(), tag);
		if(tag.equals(Indicator.TAG_PRIORITY))
			return new PriorityRatingQuestion(tag).findChoiceByCode(rawValue).getLabel();
		if(tag.equals(Indicator.TAG_STATUS))
			return new IndicatorStatusRatingQuestion(tag).findChoiceByCode(rawValue).getLabel();
		
		return rawValue;
	}

	ChainManager getChainManager()
	{
		return new ChainManager(project);
	}
	
	public void rebuild() throws Exception
	{
	}
	
	public static final String[] COLUMN_TAGS = {
		Indicator.TAG_LABEL, 
		Indicator.TAG_PRIORITY, 
		Indicator.TAG_STATUS, 
		Indicator.PSEUDO_TAG_METHODS, 
		Indicator.PSEUDO_TAG_TARGETS, 
		Indicator.PSEUDO_TAG_DIRECT_THREATS, 
	};
	
	Project project;
	Indicator indicator;
}
