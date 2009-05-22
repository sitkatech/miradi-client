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
package org.miradi.dialogs.diagram;

import org.miradi.dialogfields.RadioButtonsField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.layout.TwoColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.questions.BudgetCostModeQuestion;

public class ForecastSubPanel extends ObjectDataInputPanel
{
	public ForecastSubPanel(MainWindow mainWindow, ORef initialRef)
	{
		super(mainWindow.getProject(), initialRef);
		
		setLayout(new TwoColumnGridLayout());
				
		BudgetCostModeQuestion question = new BudgetCostModeQuestion();
		RadioButtonsField modeField = createRadioButtonsField(initialRef.getObjectType(), BaseObject.TAG_BUDGET_COST_MODE, question);
		addRawField(modeField);		
		add(modeField.getComponent(question.findIndexByCode(question.ROLLUP_MODE_CODE)));
		add(modeField.getComponent(question.findIndexByCode(question.OVERRIDE_MODE_CODE)));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Cost/When/Who");
	}
}