package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.dialogs.ActivityPoolTablePanel;
import org.conservationmeasures.eam.dialogs.diagram.ActivityShareSelectionDiaglog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.views.diagram.LocationDoer;

public class ShareActivityDoer extends LocationDoer
{	
	public boolean isAvailable()
	{
		if (! isDiagramView())
			return false;
	
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		EAMGraphCell selected = getDiagramView().getDiagramPanel().getOnlySelectedCells()[0];
		if (! selected.isFactor())
			return;
		
		DiagramFactor diagramFactor = selected.getDiagramFactor();
		ActivityShareSelectionDiaglog list = new ActivityShareSelectionDiaglog(getMainWindow(), EAM.text("Share Activity"), new ActivityPoolTablePanel(getProject(), diagramFactor.getWrappedORef()));
		list.setVisible(true);	
	}
}
