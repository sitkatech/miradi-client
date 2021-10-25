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

package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.AccountingClassificationAssociationBySequenceNoSorter;
import org.miradi.objects.AbstractTaxonomyAssociation;
import org.miradi.objects.AccountingClassificationAssociation;
import org.miradi.objects.BaseObject;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.AccountingClassificationAssociationSchema;
import org.miradi.schemas.BaseObjectSchema;

import java.util.Comparator;

public class AccountingClassificationAssociationPool extends AbstractTaxonomyAssociationPool
{
	public AccountingClassificationAssociationPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, AccountingClassificationAssociationSchema.getObjectType());
	}
	
	@Override
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId)
	{
		return new AccountingClassificationAssociation(objectManager, new BaseId(actualId.asInt()));
	}
	
	@Override
	public BaseObjectSchema createBaseObjectSchema(Project projectToUse)
	{
		return AccountingClassificationAssociation.createSchema(projectToUse);
	}

	@Override
	public Comparator<AbstractTaxonomyAssociation> getSorter()
	{
		return new AccountingClassificationAssociationBySequenceNoSorter();
	}
}
