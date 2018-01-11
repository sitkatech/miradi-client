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

package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.TaxonomyElement;
import org.miradi.objecthelpers.TaxonomyHelper;
import org.miradi.project.ObjectManager;
import org.miradi.questions.TaxonomyClassificationSelectionModeQuestion;
import org.miradi.schemas.AbstractTaxonomyAssociationSchema;
import org.miradi.schemas.BaseObjectSchema;

public abstract class AbstractTaxonomyAssociation extends BaseObject
{
	public AbstractTaxonomyAssociation(ObjectManager objectManager, BaseId id, final BaseObjectSchema schemaToUse)
	{
		super(objectManager, id, schemaToUse);
	}

	public int getBaseObjectType()
	{
		return getIntegerData(AbstractTaxonomyAssociationSchema.TAG_BASE_OBJECT_TYPE);
	}

	public String getTaxonomyAssociationCode()
	{
		return getData(AbstractTaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_CODE);
	}

	public String getTaxonomyCode()
	{
		return getData(AbstractTaxonomyAssociationSchema.TAG_TAXONOMY_CODE);
	}

	protected String getTaxonomyClassificationSelectionTypeCode()
	{
		return getData(AbstractTaxonomyAssociationSchema.TAG_SELECTION_TYPE);
	}

	public boolean isTaxonomyAssociationFor(final String poolName)
	{
		return getTaxonomyAssociationPoolName().equals(poolName);
	}
	
	public boolean isTaxonomyAssociationFor(BaseObject baseObjectToFindTaxonomyAssociationsFor)
	{
		if (Cause.is(baseObjectToFindTaxonomyAssociationsFor))
			return TaxonomyHelper.isTaxonomyAssociationForCause(getTaxonomyAssociationPoolName(), (Cause)baseObjectToFindTaxonomyAssociationsFor); 
			
		return getBaseObjectType() ==  baseObjectToFindTaxonomyAssociationsFor.getType();
	}
	
	public boolean isSingleSelectionTaxonomy()
	{
		return !isMultiSelectionTaxonomy();
	}
	
	abstract public boolean isMultiSelectionTaxonomy();

	abstract protected String getTaxonomyAssociationPoolName();

	public  boolean isSelectable(TaxonomyElement taxonomyElementToUse)
	{
		final boolean isParent = taxonomyElementToUse.getChildCodes().hasData();
		final boolean leafOnlySelectionType = leafOnlySelection();
		if (leafOnlySelectionType && isParent)
			return false;
		
		return true;
	}
	
	private boolean leafOnlySelection()
	{
		return getTaxonomyClassificationSelectionTypeCode().equals(TaxonomyClassificationSelectionModeQuestion.LEAF_ONLY_CODE);
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
}
