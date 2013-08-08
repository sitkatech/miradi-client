/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class TaxonomyEditorFields extends ObjectDataInputField
{
	public TaxonomyEditorFields(Project projectToUse, int objectType)
	{
		super(projectToUse, ORef.createInvalidWithType(objectType), BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
	}
	
	@Override
	public String getText()
	{
		return "";
	}

	@Override
	public void setText(String newValue)
	{
		panelWithFields.setText(newValue);
	}

	@Override
	public JComponent getComponent()
	{
		if (panelWithFields == null)
		{
			panelWithFields = new TaxonomyFieldsPanel(getProject());
		}
		
		return panelWithFields;
	}
	
	@Override
	public void setObjectRef(ORef refToUse)
	{
		super.setObjectRef(refToUse);
		try
		{
			panelWithFields.setObjectRef(refToUse);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}
	
	private TaxonomyFieldsPanel panelWithFields; 
}
