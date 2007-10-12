package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.dialogs.ObjectPoolTablePanel;
import org.conservationmeasures.eam.dialogs.ShareableMethodPoolTablePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conserva
tionmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;

public class ShareMethodDoer extends AbstractShareDoer
{	
	protected boolean canOwnTask(BaseObject object)
	{
		if(object.getType() == Indicator.getObjectType())
			return true;
		
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		ORef selectedRef = getSelectedRef();
		appendSelectedObjectAsShared(selectedRef, Indicator.TAG_TASK_IDS);
	}
	
	protected String getShareDialogTitle()
	{
		return EAM.text("Share Method");
	}

	protected ObjectPoolTablePanel getShareableObjectPoolTablePanel(ORef parentOfSharedObjectRefs)
	{
		return new ShareableMethodPoolTablePanel(getProject(), parentOfSharedObjectRefs);
	}
}
