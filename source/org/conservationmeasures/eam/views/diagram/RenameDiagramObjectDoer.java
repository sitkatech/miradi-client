package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.DiagramTabsLabelPropertiesPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.views.ViewDoer;

public class RenameDiagramObjectDoer extends ViewDoer 
{
	//FIXME nima sub classe need to chech to see if there is a selection
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (!isDiagramView())
			return false;
		
		return true;
	}
	
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