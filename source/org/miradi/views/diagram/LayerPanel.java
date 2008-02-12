/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.dialogs.base.ModelessDialogPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.views.umbrella.LegendPanel;

public class LayerPanel extends ModelessDialogPanel
{
	public LayerPanel(MainWindow mainWindowToUse)
	{
		add(createLegendPanel(mainWindowToUse));
	}

	private LegendPanel createLegendPanel(MainWindow mainWindowToUse)
	{
		if (mainWindowToUse.getDiagramView().isResultsChainTab())
			return new ResultsChainDiagramLegendPanel(mainWindowToUse);

		return new ConceptualModelDiagramLegendPanel(mainWindowToUse);
	}

	public BaseObject getObject()
	{
		return null;
	}

	public String getPanelDescription()
	{
		return EAM.text("Layers");
	}
}
