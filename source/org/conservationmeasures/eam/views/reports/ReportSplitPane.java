/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

import javax.swing.JLabel;
import javax.swing.JSplitPane;

public class ReportSplitPane extends JSplitPane
{
	public ReportSplitPane()
	{
		setLeftComponent(new JLabel("left"));
		setRightComponent(new JLabel("right"));
	}
}
