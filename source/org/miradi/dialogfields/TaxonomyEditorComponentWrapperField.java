/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objecthelpers.TaxonomyClassificationMap;
import org.miradi.objects.AbstractTaxonomyAssociation;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class TaxonomyEditorComponentWrapperField extends ComponentWrapperObjectDataInputField
{
	public TaxonomyEditorComponentWrapperField(Project projectToUse, ORef refToUse, String tagToUse, SavebleComponent componentToUse, AbstractTaxonomyAssociation taxonomyAssociationToUse)
	{
		super(projectToUse, refToUse, tagToUse, componentToUse);
		
		taxonomyAssociation = taxonomyAssociationToUse;
	}

	@Override
	public String getText()
	{
		try
		{
			BaseObject baseObject = BaseObject.find(getProject(), getORef());
			TaxonomyClassificationMap taxonomyClassificationList = new TaxonomyClassificationMap(baseObject.getData(this.getTag()));
			CodeList selectedTaxonomyElementCodes = new CodeList(super.getText());
			final String taxonomyCode = getTaxonomyAssociation().getTaxonomyCode();
			taxonomyClassificationList.safelyRemoveCode(taxonomyCode);
			if (selectedTaxonomyElementCodes.hasData())
				taxonomyClassificationList.putCodeList(taxonomyCode, selectedTaxonomyElementCodes);
			
			return taxonomyClassificationList.toJsonString();
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
			return "";
		}
	}

	@Override
	public void setText(String newValue)
	{
		try
		{
			CodeList taxonomyElementCodes = TaxonomyClassificationMap.getTaxonomyElementCodes(getProject(), newValue, getTaxonomyAssociation().getTaxonomyAssociationCode());
			super.setText(taxonomyElementCodes.toJsonString());
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	private AbstractTaxonomyAssociation getTaxonomyAssociation()
	{
		return taxonomyAssociation;
	}
	
	private AbstractTaxonomyAssociation taxonomyAssociation;
}
