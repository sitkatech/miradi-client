/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.main.MainWindow;

public class LayerPanel extends DisposablePanel
{
	public LayerPanel(MainWindow mainWindowToUse)
	{
		add(new DiagramLegendPanel(mainWindowToUse));
	}
}
