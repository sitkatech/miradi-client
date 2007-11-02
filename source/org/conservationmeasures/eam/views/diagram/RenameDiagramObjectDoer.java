package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.base.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.diagram.DiagramTabsLabelPropertiesPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.DiagramObject;

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