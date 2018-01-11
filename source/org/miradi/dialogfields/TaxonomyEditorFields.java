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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objectpools.AbstractTaxonomyAssociationPool;
import org.miradi.project.Project;

import javax.swing.*;

public class TaxonomyEditorFields extends ObjectDataInputField
{
	public TaxonomyEditorFields(Project projectToUse, int objectType, String tagToUse, AbstractTaxonomyAssociationPool taxonomyAssociationPoolToUse)
	{
		super(projectToUse, ORef.createInvalidWithType(objectType), tagToUse);
		taxonomyAssociationPool = taxonomyAssociationPoolToUse;
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
			panelWithFields = new TaxonomyFieldsPanel(getProject(), getTag(), taxonomyAssociationPool);

		return panelWithFields;
	}
	
	@Override
	public void setObjectRef(ORef refToUse)
	{
		super.setObjectRef(refToUse);
		try
		{
			if (refToUse.equals(currentRef))
				return;
			
			currentRef = refToUse;
			panelWithFields.setObjectRef(refToUse);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}
	
	
	private ORef currentRef; 
	private TaxonomyFieldsPanel panelWithFields;
	private AbstractTaxonomyAssociationPool taxonomyAssociationPool;
}
