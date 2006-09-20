/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.objects.Indicator;

public class MonitoringIndicator extends MonitoringNode
{
	public MonitoringIndicator(Indicator indicatorToUse)
	{
		indicator = indicatorToUse;
	}

	public int getType()
	{
		return indicator.getType();
	}

	public String toString()
	{
		return indicator.getLabel();
	}

	public int getChildCount()
	{
		return 0;
	}

	public MonitoringNode getChild(int index)
	{
		return null;
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	Indicator indicator;
}
