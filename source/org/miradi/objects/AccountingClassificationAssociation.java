/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
import org.miradi.schemas.AccountingClassificationAssociationSchema;

public class AccountingClassificationAssociation extends AbstractTaxonomyAssociation
{
	public AccountingClassificationAssociation(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, createSchema(objectManager));
	}

	public static AccountingClassificationAssociationSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static AccountingClassificationAssociationSchema createSchema(ObjectManager objectManager)
	{
		return (AccountingClassificationAssociationSchema) objectManager.getSchemas().get(ObjectType.ACCOUNTING_CLASSIFICATION_ASSOCIATION);
	}

	@Override
	protected String getTaxonomyAssociationPoolName()
	{
		return getStringData(AccountingClassificationAssociationSchema.TAG_ACCOUNTING_CLASSIFICATION_ASSOCIATION_POOL_NAME);
	}

	@Override
	public boolean isMultiSelectionTaxonomy()
	{
		return false;
	}

	public int getSequenceNo()
	{
		return getIntegerData(AccountingClassificationAssociationSchema.TAG_SEQUENCE_NO);
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
		return AccountingClassificationAssociationSchema.getObjectType() == onbjectType;
	}
	
	public static AccountingClassificationAssociation find(ObjectManager objectManager, ORef miradiShareAccountingClassificationAssociationRef)
	{
		return (AccountingClassificationAssociation) objectManager.findObject(miradiShareAccountingClassificationAssociationRef);
	}
	
	public static AccountingClassificationAssociation find(Project project, ORef miradiShareAccountingClassificationAssociationRef)
	{
		return find(project.getObjectManager(), miradiShareAccountingClassificationAssociationRef);
	}
}
