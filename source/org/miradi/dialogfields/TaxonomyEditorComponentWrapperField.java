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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TaxonomyClassificationMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TaxonomyAssociation;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class TaxonomyEditorComponentWrapperField extends ComponentWrapperObjectDataInputField
{
	public TaxonomyEditorComponentWrapperField(Project projectToUse, ORef refToUse, String tagToUse, SavebleComponent componentToUse, TaxonomyAssociation taxonomyAssociationToUse)
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
			TaxonomyClassificationMap taxonomyClassificationList = new TaxonomyClassificationMap(baseObject.getData(BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
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

	private TaxonomyAssociation getTaxonomyAssociation()
	{
		return taxonomyAssociation;
	}
	
	private TaxonomyAssociation taxonomyAssociation;
}
