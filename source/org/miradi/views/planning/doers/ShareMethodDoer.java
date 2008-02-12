/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.views.planning.doers;

import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.dialogs.task.ShareableMethodPoolTablePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;

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
