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

package org.miradi.dialogs.base;

import org.miradi.dialogfields.ComponentWrapperObjectDataInputField;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogfields.SavebleComponent;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;

public class OneFieldObjectDataInputPanel extends ObjectDataInputPanel
{
	//FIXME: This panel will contain multiple ODIF's, 
	// eliminating the need for a fake field with a fake tag
	public OneFieldObjectDataInputPanel(Project projectToUse, SavebleComponent savebleComponentToUse)
	{
		this (projectToUse, ORef.INVALID, Dashboard.PSEUDO_TEMP_TAG, savebleComponentToUse);
	}
	
	public OneFieldObjectDataInputPanel(Project projectToUse, ORef orefToUse, String tagToUse, SavebleComponent savebleComponentToUse)
	{
		this(projectToUse, orefToUse,tagToUse,  new ComponentWrapperObjectDataInputField(projectToUse, orefToUse, tagToUse, savebleComponentToUse));
	}
	
	public OneFieldObjectDataInputPanel(Project projectToUse, ORef orefToUse, String tagToUse, ObjectDataInputField singleField)
	{
		super(projectToUse, orefToUse);
		
		addFieldWithoutLabel(singleField);
		
		updateFieldsFromProject();
	}
	
	public ComponentWrapperObjectDataInputField getSingleField()
	{
		return (ComponentWrapperObjectDataInputField) getFields().firstElement();
	}

	@Override
	public String getPanelDescription()
	{
		return "OneFieldObjectDataInputPanel";
	}
}
