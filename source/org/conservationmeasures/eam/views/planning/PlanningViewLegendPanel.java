/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import javax.swing.JLabel;

import org.conservationmeasures.eam.views.umbrella.LegendPanel;

import com.jhlabs.awt.BasicGridLayout;

public class PlanningViewLegendPanel extends LegendPanel
{
	public PlanningViewLegendPanel()
	{
		super(new BasicGridLayout(0, 1));
		add(new JLabel("legend construction area"));
	}
}
