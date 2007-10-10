package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;


abstract public class AbstractTreeNodeCreateTaskDoer extends AbstractTreeNodeDoer
{
	protected boolean canOwnTask(BaseObject object)
	{
		if(object.getType() == Task.getObjectType())
			return true;
		
		return false;
	}
}
