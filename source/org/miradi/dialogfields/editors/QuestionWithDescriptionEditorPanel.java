/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields.editors;

import org.miradi.dialogs.base.OneFieldObjectDataInputPanel;
import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.dialogs.dashboard.SplitterPanelWithRightSideTextPanel;
import org.miradi.dialogs.dashboard.StaticLongDescriptionProvider;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.questions.ChoiceQuestion;

public class QuestionWithDescriptionEditorPanel extends SplitterPanelWithRightSideTextPanel
{
	public QuestionWithDescriptionEditorPanel(MainWindow mainWindowToUse, ChoiceQuestion questionToUse, OneFieldObjectDataInputPanel leftPanelEditorComponentToUse) throws Exception
	{
		super(mainWindowToUse, leftPanelEditorComponentToUse);		
	}		
		
	@Override
	protected AbstractLongDescriptionProvider getDefaultDescriptionProvider() throws Exception
	{
		return new StaticLongDescriptionProvider();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Edit..");
	}
}
