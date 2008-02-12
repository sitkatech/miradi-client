/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;




import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.views.ViewDoer;

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
