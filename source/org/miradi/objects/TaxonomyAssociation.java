/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.TaxonomyMultiSelectModeQuestion;
import org.miradi.schemas.TaxonomyAssociationSchema;

public class TaxonomyAssociation extends AbstractTaxonomyAssociation
{
	public TaxonomyAssociation(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, createSchema(objectManager));
	}

	public static TaxonomyAssociationSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static TaxonomyAssociationSchema createSchema(ObjectManager objectManager)
	{
		return (TaxonomyAssociationSchema) objectManager.getSchemas().get(ObjectType.TAXONOMY_ASSOCIATION);
	}

	@Override
	protected String getTaxonomyAssociationPoolName()
	{
		return getStringData(TaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_POOL_NAME);
	}

	@Override
	public boolean isMultiSelectionTaxonomy()
	{
		String multiSelectCode = getData(TaxonomyAssociationSchema.TAG_MULTI_SELECT);
		
		return multiSelectCode.equals(TaxonomyMultiSelectModeQuestion.MULTI_SELECT_CODE);
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
	
	public static TaxonomyAssociation find(ObjectManager objectManager, ORef miradiShareTaxonomyAssociationRef)
	{
		return (TaxonomyAssociation) objectManager.findObject(miradiShareTaxonomyAssociationRef);
	}
	
	public static TaxonomyAssociation find(Project project, ORef miradiShareTaxonomyAssociationRef)
	{
		return find(project.getObjectManager(), miradiShareTaxonomyAssociationRef);
	}
}
