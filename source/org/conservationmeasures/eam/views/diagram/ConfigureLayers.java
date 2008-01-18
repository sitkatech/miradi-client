/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;




import org.conservationmeasures.eam.dialogs.base.ModelessDialogWithClose;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.ViewDoer;

public class ConfigureLayers extends ViewDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		MainWindow window = getMainWindow();
		LayerPanel layerPanel = new LayerPanel(window);
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(window, layerPanel, EAM.text("Title|View Layers"));
		getView().showFloatingPropertiesDialog(dlg);
	}

}
