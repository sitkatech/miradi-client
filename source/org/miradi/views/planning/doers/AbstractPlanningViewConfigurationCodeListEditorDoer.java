/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.CodeListEditorPanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ViewData;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractPlanningViewConfigurationCodeListEditorDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		return true;
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			ORef currentViewDataRef = getProject().getCurrentViewData().getRef();
			ViewData viewData = ViewData.find(getProject(), currentViewDataRef);
			ORef planningConfigurationRef = viewData.getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);

			ChoiceQuestion configurationQuestion = getProject().getQuestion(getConfigurationQuestion());
			CodeListEditorPanel codeListPanel = new CodeListEditorPanel(getProject(), planningConfigurationRef, getConfigurationTag(), configurationQuestion, getGridColumnCount());
			ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), EAM.text("Selection Dialog"));
			dialog.setScrollableMainPanel(codeListPanel);
			Utilities.centerDlg(dialog);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	abstract protected int getGridColumnCount();

	abstract protected String getConfigurationTag();

	abstract protected Class getConfigurationQuestion();
}
