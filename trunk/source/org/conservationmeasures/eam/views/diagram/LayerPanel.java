/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.BaseObject;

public class LayerPanel extends ModelessDialogPanel
{
	public LayerPanel(MainWindow mainWindowToUse)
	{
		add(createLegendPanel(mainWindowToUse));
	}

	private DiagramLegendPanel createLegendPanel(MainWindow mainWindowToUse)
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
