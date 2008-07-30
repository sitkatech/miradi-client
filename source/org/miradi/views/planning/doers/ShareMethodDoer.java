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
package org.miradi.views.planning.doers;

import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.dialogs.task.ShareableMethodPoolTablePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;

public class ShareMethodDoer extends AbstractShareDoer
{	
	@Override
	public boolean isAvailable()
	{
		return getSingleSelected(getParentType()) != null;
	}
	
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

	protected int getParentType()
	{
		return Indicator.getObjectType();
	}
	
	protected String getShareDialogTitle()
	{
		return EAM.text("Share Method");
	}

	protected ObjectPoolTablePanel createShareableObjectPoolTablePanel(ORef parentOfSharedObjectRefs)
	{
		return new ShareableMethodPoolTablePanel(getMainWindow(), parentOfSharedObjectRefs);
	}
}
