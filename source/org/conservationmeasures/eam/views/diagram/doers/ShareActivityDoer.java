package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.dialogs.ObjectPoolTablePanel;
import org.conservationmeasures.eam.dialogs.ShareableActivityPoolTablePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.views.planning.doers.AbstractShareDoer;

public class ShareActivityDoer extends AbstractShareDoer
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
		
		appendSelectedObjectAsShared(getParentOfShareableObjects(), Strategy.TAG_ACTIVITY_IDS);
	}

	protected String getShareDialogTitle()
	{
		return EAM.text("Share Activity");
	}

	protected ObjectPoolTablePanel getShareableObjectPoolTablePanel(ORef parentOfSharedObjectRefs)
	{
		return new ShareableActivityPoolTablePanel(getProject(), parentOfSharedObjectRefs);
	}

	protected ORef getParentOfShareableObjects()
	{
		EAMGraphCell selected = getDiagramView().getDiagramPanel().getOnlySelectedCells()[0];
		if (! selected.isFactor())
			return ORef.INVALID;
		
		return selected.getDiagramFactor().getWrappedORef();
	}
}
