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

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.planning.upperPanel.WhoAssignedStateLogic;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ProjectResourceQuestion;
import org.miradi.utils.CodeList;

public class WhoAssignedEditorField extends WhoEditorField
{
	public WhoAssignedEditorField(MainWindow mainWindow, ORef refToUse)
	{
		super(mainWindow, refToUse);
	}

	@Override
	protected String getPanelTitle()
	{
		return EAM.text("Who Assigned");
	}

	@Override
	protected CodeList getWhoTotalsCodeList(BaseObject baseObject)
	{
		return WhoAssignedCodeListEditorComponent.getWhoTotalCodes(baseObject);
	}

	@Override
	protected boolean isWhoCellEditable()
	{
		BaseObject baseObject = BaseObject.find(getProject(), getORef());

		return new WhoAssignedStateLogic(getProject()).isWhoCellEditable(baseObject);
	}

	@Override
	protected DisposablePanel createEditorPanel(BaseObject baseObject, ProjectResourceQuestion question)
	{
		return new WhoAssignedCodeListEditorComponent(baseObject, question);
	}
}
