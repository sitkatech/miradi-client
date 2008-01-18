/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.conservationmeasures.eam.views.planning.RowManager;


abstract public class AbstractTreeNodeCreateTaskDoer extends AbstractTreeNodeDoer
{
	public boolean isAvailable()
	{
		try
		{
			BaseObject selected = getSingleSelectedObject();
			if(selected == null)
				return false;
			if(!canOwnTask(selected))
				return false;
			if(!childTaskWouldBeVisible(selected.getType()))
				return false;
			
			return true;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
	
	protected boolean canOwnTask(BaseObject object)
	{
		if(object.getType() == Task.getObjectType())
			return true;
		
		return false;
	}

	private boolean childTaskWouldBeVisible(int parentType) throws Exception
	{
		ViewData viewData = getProject().getViewData(PlanningView.getViewName());
		CodeList visibleRowCodes = RowManager.getVisibleRowCodes(viewData);
		return (visibleRowCodes.contains(Task.getChildTaskTypeCode(parentType)));
	}
}
