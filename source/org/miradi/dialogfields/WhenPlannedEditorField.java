/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import org.miradi.dialogfields.editors.WhenPlannedEditorComponent;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourcePlan;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateUnitEffortList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WhenPlannedEditorField extends ObjectDataField
{
	public WhenPlannedEditorField(MainWindow mainWindow, ORef refToUse)
	{
		super(mainWindow.getProject(), refToUse);
	}

	private boolean hasDataChanged()
	{
		try
		{
			CodeList whenStartEndCodes = whenPlannedEditor.getStartEndCodes();
			DateUnitEffortList editorDateUnitEffortList = WhenPlannedEditorComponent.createDateUnitEffortList(whenStartEndCodes);
			DateUnitEffortList resourcePlanDateUnitEffortList = new DateUnitEffortList();

			BaseObject baseObject = BaseObject.find(getProject(), getORef());

			ORefList resourcePlansRefs = baseObject.getResourcePlanRefs();
			if (!resourcePlansRefs.isEmpty())
			{
				ORef resourcePlanRef = baseObject.getResourcePlanRefs().get(0);
				ResourcePlan resourcePlan = ResourcePlan.find(getProject(), resourcePlanRef);
				resourcePlanDateUnitEffortList = resourcePlan.getDateUnitEffortList();
			}

			return !editorDateUnitEffortList.equals(resourcePlanDateUnitEffortList);
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
		return baseObject.isPlannedWhenEditable();
	}

	@Override
	public void updateFromObject()
	{
		updateEditableState();

		if (!isValidObject())
			return;

		ignoreEditorActions = true;

		BaseObject baseObject = BaseObject.find(getProject(), getORef());
		ORefList planningObjectRefs = baseObject.getResourcePlanRefs();
		whenPlannedEditor.setPlanningObjectRefs(planningObjectRefs);

		ignoreEditorActions = false;
	}

	@Override
	public JComponent getComponent()
	{
		if (whenPlannedEditor == null)
			createEditorPanel();

		return whenPlannedEditor;
	}

	@Override
	public void saveIfNeeded()
	{
		if(!isValidObject())
			return;

		if(!hasDataChanged())
			return;

		try
		{
			BaseObject baseObject = BaseObject.find(getProject(), getORef());
			CodeList whenStartEndCodes = whenPlannedEditor.getStartEndCodes();
			WhenPlannedEditorComponent.setWhenPlannedValue(getProject(), baseObject, whenStartEndCodes);
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

	public class WhenPlannedEditorChangeHandler implements ActionListener
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
			ORefList planningObjectRefs = new ORefList();

			if (getORef().isValid())
			{
				BaseObject baseObject = BaseObject.find(getProject(), getORef());
				planningObjectRefs = baseObject.getResourcePlanRefs();
			}

			whenPlannedEditor = new WhenPlannedEditorComponent(getProject(), planningObjectRefs);
			whenPlannedEditor.setBackground(AppPreferences.getDataPanelBackgroundColor());
			whenPlannedEditor.addActionListener(new WhenPlannedEditorChangeHandler());
			whenPlannedEditor.setBorder(DataField.createLineBorderWithMargin());
		}
		catch (Exception e)
		{
			EAM.panic(e);
		}
	}

	private boolean ignoreEditorActions;
	private WhenPlannedEditorComponent whenPlannedEditor;
}
