/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import javax.swing.JComponent;

import org.miradi.dialogfields.editors.WhenEditorComponent;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ReadonlyPanelWithPopupEditor;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ProjectResourceQuestion;
import org.miradi.utils.DateRange;

public class WhenEditorField extends ObjectDataField implements ReadonlyPanelAndPopupEditorProvider
{
	public WhenEditorField(MainWindow mainWindowToUse, ORef refToUse)
	{
		super(mainWindowToUse.getProject(), refToUse);
		
		readonlyPanelWithPopupEditor = new ReadonlyPanelWithPopupEditor(this, EAM.text("Select Project Resources"), new ProjectResourceQuestion(getProject()));
	}

	@Override
	public JComponent getComponent()
	{
		return readonlyPanelWithPopupEditor;
	}
	
	@Override
	public String getTag()
	{
		return "";
	}

	@Override
	public void updateFromObject()
	{
		readonlyPanelWithPopupEditor.setEnabled(false);
		if (!isValidObject())
			return;
		
		try
		{
			BaseObject baseObject = BaseObject.find(getProject(), getORef());
			TimePeriodCostsMap totalTimePeriodCostsMap = getProject().getTimePeriodCostsMapsCache().getTotalTimePeriodCostsMap(baseObject);
			DateRange projectStartEndDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
			DateRange rolledUpResourceAssignmentsDateRange = totalTimePeriodCostsMap.getRolledUpDateRange(projectStartEndDateRange);
			String rolledUpResourceAssignmentsWhen = getProject().getProjectCalendar().convertToSafeString(rolledUpResourceAssignmentsDateRange);
			readonlyPanelWithPopupEditor.setText(rolledUpResourceAssignmentsWhen);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	@Override
	public void saveIfNeeded()
	{
	}

	public DisposablePanel createEditorPanel() throws Exception
	{
		return new WhenEditorComponent(getProject().getProjectCalendar(), BaseObject.find(getProject(), getORef()));
	}

	public AbstractReadonlyChoiceComponent createReadOnlyComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		//FIXME urgent,  this needs to be a single line readonly component, since the value is a date
		return new ReadonlySingleChoiceComponent(questionToUse);
	}
	
	private ReadonlyPanelWithPopupEditor readonlyPanelWithPopupEditor;
}
