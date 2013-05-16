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

package org.miradi.questions;

import org.miradi.objecthelpers.TaxonomyElement;
import org.miradi.objects.MiradiShareTaxonomy;
import org.miradi.objects.TaxonomyAssociation;
import org.miradi.utils.CodeList;

public class MiradiShareTaxonomyQuestion extends DynamicChoiceWithRootChoiceItem
{
	public MiradiShareTaxonomyQuestion(MiradiShareTaxonomy miradiShareTaxonomyToUse, TaxonomyAssociation taxonomyAssociationToUse)
	{
		miradiShareTaxonomy = miradiShareTaxonomyToUse;
		taxonomyAssociation = taxonomyAssociationToUse;
	}
	
	@Override
	protected ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		return createChoiceItems(miradiShareTaxonomy, taxonomyAssociation);
	}
	
	private static ChoiceItemWithChildren createChoiceItems(MiradiShareTaxonomy miradiShareTaxonomyToUse, TaxonomyAssociation taxonomyAssociationToUse) throws Exception
	{
		CodeList topLevelTaxonomyElementCodes = miradiShareTaxonomyToUse.getTopLevelTaxonomyElementCodes();
		ChoiceItemWithChildren rootChoiceItem = new ChoiceItemWithChildren("", "", "");
		addChildrenChoices(miradiShareTaxonomyToUse, rootChoiceItem, topLevelTaxonomyElementCodes, taxonomyAssociationToUse);

		return rootChoiceItem;
	}

	private static void addChildrenChoices(MiradiShareTaxonomy miradiShareTaxonomyToUse, ChoiceItemWithChildren parentChoiceItem, CodeList childCodes, TaxonomyAssociation taxonomyAssociationToUse) throws Exception
	{
		for (String taxonomyElementCode : childCodes)
		{
			TaxonomyElement taxonomyElement = miradiShareTaxonomyToUse.findTaxonomyElement(taxonomyElementCode);
			ChoiceItemWithChildren childChoiceItem = new ChoiceItemWithChildren(taxonomyElement.getCode(), taxonomyElement.getLabel(), taxonomyElement.getDescription());
			final boolean isSelectable = taxonomyAssociationToUse.isSelectable(taxonomyElement);
			childChoiceItem.setSelectable(isSelectable);
			parentChoiceItem.addChild(childChoiceItem);
			addChildrenChoices(miradiShareTaxonomyToUse, childChoiceItem, taxonomyElement.getChildCodes(), taxonomyAssociationToUse);
		}
	}
	
	private MiradiShareTaxonomy miradiShareTaxonomy;
	private TaxonomyAssociation taxonomyAssociation;
}
