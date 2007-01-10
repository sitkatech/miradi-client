/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;

public class LayerPanel extends ModelessDialogPanel
{
	public LayerPanel(MainWindow mainWindowToUse)
	{
		add(new DiagramLegendPanel(mainWindowToUse));
	}

	public EAMObject getObject()
	{
		return null;
	}

	public String getPanelDescription()
	{
		return EAM.text("Layers");
	}
}
