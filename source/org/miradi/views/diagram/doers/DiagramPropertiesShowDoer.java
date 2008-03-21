/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
