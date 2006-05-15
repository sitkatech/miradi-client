/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.views.ViewDoer;

public class NormalDiagramMode extends ViewDoer
{
	public boolean isAvailable()
	{
		try
		{
			ViewData viewData = getProject().getViewData(getView().cardName());
			String currentViewMode = viewData.getData(ViewData.TAG_CURRENT_MODE);
			if(DiagramView.MODE_DEFAULT.equals(currentViewMode))
				return false;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}

		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		try
		{
			int viewId = getCurrentViewId();
			getProject().executeCommand(new CommandSetObjectData(ObjectType.VIEW_DATA, viewId, 
					ViewData.TAG_CURRENT_MODE, DiagramView.MODE_DEFAULT));
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private int getCurrentViewId() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		return viewData.getId();
	}
}
