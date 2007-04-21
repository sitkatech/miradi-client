package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.ResultChainTabLabelPropertiesPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.views.ViewDoer;

public class UpdateTabLabelDoer extends ViewDoer 
{
	public UpdateTabLabelDoer()
	{
		super();
	}
	
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (!isDiagramView())
			return false;
		
		return getMainWindow().getDiagramView().isResultsChainTab();
	}
	
	public void doIt() throws CommandFailedException 
	{
		DiagramView diagramView = (DiagramView)getView();
		DiagramObject diagramObject = diagramView.getDiagramPanel().getDiagramObject();
		ResultChainTabLabelPropertiesPanel panel = new ResultChainTabLabelPropertiesPanel(getProject(), diagramObject.getId());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}
}

