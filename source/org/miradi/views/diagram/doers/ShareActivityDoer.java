/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.views.diagram.doers;

import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.dialogs.activity.ShareableActivityPoolTablePanel;
import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;
import org.miradi.views.planning.doers.AbstractShareDoer;

public class ShareActivityDoer extends AbstractShareDoer
{	
	public boolean isAvailable()
	{
		if (! isInDiagram())
			return false;
	
		return true;
	}

	protected String getParentTaskIdsTag()
	{
		return Strategy.TAG_ACTIVITY_IDS;
	}

	protected String getShareDialogTitle()
	{
		return EAM.text("Share Activity");
	}

	protected ObjectPoolTablePanel createShareableObjectPoolTablePanel(ORef parentOfSharedObjectRefs)
	{
		return new ShareableActivityPoolTablePanel(getProject(), parentOfSharedObjectRefs);
	}

	protected ORef getParentRefOfShareableObjects()
	{
		EAMGraphCell selected = getDiagramView().getDiagramPanel().getOnlySelectedCells()[0];
		if (! selected.isFactor())
			return ORef.INVALID;
		
		return selected.getDiagramFactor().getWrappedORef();
	}
}
