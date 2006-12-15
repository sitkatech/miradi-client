/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.main.MainWindow;

public class LayerPanel extends DisposablePanel implements ActionListener
{
	public LayerPanel(MainWindow mainWindowToUse)
	{
		add(new DiagramLegendPanel(mainWindowToUse));
	}

	public void actionPerformed(ActionEvent e)
	{
	}
}
