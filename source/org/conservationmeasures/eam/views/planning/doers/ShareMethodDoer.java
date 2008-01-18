/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTablePanel;
import org.conservationmeasures.eam.dialogs.task.ShareableMethodPoolTablePanel;
import org.conservationmeasures.eam.main.EAM;
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

	protected String getParentTaskIdsTag()
	{
		return Indicator.TAG_TASK_IDS;
	}

	protected ORef getParentRefOfShareableObjects()
	{
		return getSelectedRef();
	}
	
	protected String getShareDialogTitle()
	{
		return EAM.text("Share Method");
	}

	protected ObjectPoolTablePanel createShareableObjectPoolTablePanel(ORef parentOfSharedObjectRefs)
	{
		return new ShareableMethodPoolTablePanel(getProject(), parentOfSharedObjectRefs);
	}
}
