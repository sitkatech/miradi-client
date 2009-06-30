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
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.base.WorkPlanColumnConfigurationEditorPanel;
import org.miradi.dialogs.planning.upperPanel.WorkPlanTreeTableModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.TableSettings;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.views.ObjectsDoer;

public class WorkPlanColumnsEditorDoer extends ObjectsDoer
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
			TableSettings workPlanTableSettings = TableSettings.findOrCreate(getProject(), WorkPlanTreeTableModel.UNIQUE_TREE_TABLE_IDENTIFIER);
			ChoiceQuestion columnConfigurationQuestion = getProject().getQuestion(WorkPlanColumnConfigurationQuestion.class);
			WorkPlanColumnConfigurationEditorPanel codeListPanel = new WorkPlanColumnConfigurationEditorPanel(getProject(), workPlanTableSettings.getRef(), TableSettings.TAG_TABLE_SETTINGS_MAP, columnConfigurationQuestion);
			ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), EAM.text("Budget Column Editor"));
			dialog.setScrollableMainPanel(codeListPanel);
			Utilities.centerDlg(dialog);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
