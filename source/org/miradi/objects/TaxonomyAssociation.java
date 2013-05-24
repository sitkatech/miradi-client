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

package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TaxonomyElement;
import org.miradi.objecthelpers.TaxonomyHelper;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.TaxonomyClassificationSelectionModeQuestion;
import org.miradi.questions.TaxonomyMultiSelectModeQuestion;
import org.miradi.schemas.TaxonomyAssociationSchema;

public class TaxonomyAssociation extends BaseObject
{
	public TaxonomyAssociation(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, createSchema());
	}

	public static TaxonomyAssociationSchema createSchema()
	{
		return new TaxonomyAssociationSchema();
	}
	
	public int getBaseObjectType()
	{
		return getIntegerData(TaxonomyAssociationSchema.TAG_BASE_OBJECT_TYPE);
	}
	
	public String getTaxonomyAssociationPoolName()
	{
		return getStringData(TaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_POOL_NAME);
	}
	
	public String getTaxonomyAssociationCode()
	{
		return getData(TaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_CODE);
	}
	
	public String getTaxonomyCode()
	{
		return getData(TaxonomyAssociationSchema.TAG_TAXONOMY_CODE);
	}
	
	public String getTaxonomyClassificationSelectionTypeCode()
	{
		return getData(TaxonomyAssociationSchema.TAG_SELECTION_TYPE);
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
	
	public boolean isMultiSelectionTaxonomy()
	{
		String multiSelectCode = getData(TaxonomyAssociationSchema.TAG_MULTI_SELECT);
		
		return multiSelectCode.equals(TaxonomyMultiSelectModeQuestion.MULTI_SELECT_CODE);
	}
	
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
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public static boolean is(final int onbjectType)
	{
		return TaxonomyAssociationSchema.getObjectType() == onbjectType;
	}
	
	public static TaxonomyAssociation find(ObjectManager objectManager, ORef miradiSharfeTaxonomyAssociationRe)
	{
		return (TaxonomyAssociation) objectManager.findObject(miradiSharfeTaxonomyAssociationRe);
	}
	
	public static TaxonomyAssociation find(Project project, ORef miradiShareTaxonomyAssociationRef)
	{
		return find(project.getObjectManager(), miradiShareTaxonomyAssociationRef);
	}
}
