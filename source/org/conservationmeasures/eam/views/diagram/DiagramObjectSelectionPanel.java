/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

abstract public class DiagramObjectSelectionPanel extends JComponent implements ListSelectionListener
{
	public DiagramObjectSelectionPanel()
	{
	}
	
	public void valueChanged(ListSelectionEvent event)
	{
	}
}
