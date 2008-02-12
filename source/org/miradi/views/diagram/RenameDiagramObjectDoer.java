/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.views.diagram;

import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.dialogs.diagram.DiagramTabsLabelPropertiesPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.DiagramObject;

abstract public class RenameDiagramObjectDoer extends DiagramPageDoer 
{	
	public void doIt() throws CommandFailedException 
	{
		if (!isAvailable())
			return;
		
		DiagramView diagramView = (DiagramView)getView();
		DiagramObject diagramObject = diagramView.getDiagramPanel().getDiagramObject();
		DiagramTabsLabelPropertiesPanel panel = new DiagramTabsLabelPropertiesPanel(getProject(), diagramObject.getRef());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}
}