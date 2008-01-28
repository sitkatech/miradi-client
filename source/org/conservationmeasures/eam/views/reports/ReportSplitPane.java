/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
