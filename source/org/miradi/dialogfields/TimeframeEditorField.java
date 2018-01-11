/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.dialogfields.editors.TimeframeEditorComponent;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Timeframe;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateUnitEffortList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeframeEditorField extends ObjectDataField
{
	public TimeframeEditorField(MainWindow mainWindow, ORef refToUse)
	{
		super(mainWindow.getProject(), refToUse);
	}

	private boolean hasDataChanged()
	{
		try
		{
			CodeList whenStartEndCodes = timeframeEditor.getStartEndCodes();
			DateUnitEffortList editorDateUnitEffortList = TimeframeEditorComponent.createDateUnitEffortList(whenStartEndCodes);
			DateUnitEffortList timeframeDateUnitEffortList = new DateUnitEffortList();

			BaseObject baseObject = BaseObject.find(getProject(), getORef());

			ORefList timeframeRefs = baseObject.getTimeframeRefs();
			if (!timeframeRefs.isEmpty())
			{
				ORef timeframeRef = baseObject.getTimeframeRefs().getFirstElement();
				Timeframe timeframe = Timeframe.find(getProject(), timeframeRef);
				timeframeDateUnitEffortList = timeframe.getDateUnitEffortList();
			}

			return !editorDateUnitEffortList.equals(timeframeDateUnitEffortList);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	@Override
	protected boolean shouldBeEditable()
	{
		if (!isValidObject())
			return false;

		BaseObject baseObject = BaseObject.find(getProject(), getORef());
		return baseObject.isTimeframeEditable();
	}

	@Override
	public void updateFromObject()
	{
		updateEditableState();

		ignoreEditorActions = true;

		ORefList planningObjectRefs = new ORefList();
		if (isValidObject())
		{
			BaseObject baseObject = BaseObject.find(getProject(), getORef());
			planningObjectRefs = baseObject.getTimeframeRefs();
		}
		timeframeEditor.setPlanningObjectRefs(planningObjectRefs);

		ignoreEditorActions = false;
	}

	@Override
	public JComponent getComponent()
	{
		if (timeframeEditor == null)
			createEditorPanel();

		return timeframeEditor;
	}

	@Override
	public void saveIfNeeded()
	{
		if(!isValidObject())
			return;

		if (!hasDataChanged())
			return;

		try
		{
			BaseObject baseObject = BaseObject.find(getProject(), getORef());
			CodeList whenStartEndCodes = timeframeEditor.getStartEndCodes();
			TimeframeEditorComponent.setTimeframeValue(getProject(), baseObject, whenStartEndCodes);
			updateFromObject();
		}
		catch (Exception e)
		{
			EAM.panic(e);
		}
	}

	@Override
	public String getTag()
	{
		return "";
	}

	public class TimeframeEditorChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if (!ignoreEditorActions)
				saveIfNeeded();
		}
	}

	private void createEditorPanel()
	{
		try
		{
			ORefList timeframeRefs = new ORefList();

			if (getORef().isValid())
			{
				BaseObject baseObject = BaseObject.find(getProject(), getORef());
				timeframeRefs = baseObject.getTimeframeRefs();
			}

			timeframeEditor = new TimeframeEditorComponent(getProject(), timeframeRefs);
			timeframeEditor.setBackground(AppPreferences.getDataPanelBackgroundColor());
			timeframeEditor.addActionListener(new TimeframeEditorChangeHandler());
			timeframeEditor.setBorder(DataField.createLineBorderWithMargin());
		}
		catch (Exception e)
		{
			EAM.panic(e);
		}
	}

	private boolean ignoreEditorActions;
	private TimeframeEditorComponent timeframeEditor;
}
