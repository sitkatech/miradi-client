/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.dialogs.diagram.DiagramPropertiesPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.DiagramObject;
import org.miradi.views.ObjectsDoer;

public class DiagramPropertiesShowDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		DiagramObject diagramObject = getDiagramView().getDiagramPanel().getDiagramObject();
		DiagramPropertiesPanel diagramPropertiesPanel = new DiagramPropertiesPanel(getProject(), diagramObject.getRef());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), diagramPropertiesPanel, diagramPropertiesPanel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}
}
