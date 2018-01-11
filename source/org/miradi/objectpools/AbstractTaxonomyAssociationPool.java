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

package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objects.AbstractTaxonomyAssociation;
import org.miradi.objects.BaseObject;

import java.util.Comparator;
import java.util.Vector;

abstract public class AbstractTaxonomyAssociationPool extends BaseObjectPool
{
	public AbstractTaxonomyAssociationPool(IdAssigner idAssignerToUse, int objectTypeToStore)
	{
		super(idAssignerToUse, objectTypeToStore);
	}
	
	public void put(AbstractTaxonomyAssociation miradiShareTaxonomyAssociation) throws Exception
	{
		put(miradiShareTaxonomyAssociation.getId(), miradiShareTaxonomyAssociation);
	}

	abstract public Comparator<AbstractTaxonomyAssociation> getSorter();

	public AbstractTaxonomyAssociation find(BaseId id)
	{
		return (AbstractTaxonomyAssociation) getRawObject(id);
	}

	public Vector<AbstractTaxonomyAssociation> findTaxonomyAssociationsForBaseObject(final BaseObject baseObjectToFindTaxonomyAssociationsFor)
	{
		Vector<AbstractTaxonomyAssociation> taxonomyAssociationsForType = new Vector<AbstractTaxonomyAssociation>();
		Vector<BaseObject> taxonomyAssociations = getAllObjects();
		for(BaseObject baseObject : taxonomyAssociations)
		{
			AbstractTaxonomyAssociation taxonomyAssociation = (AbstractTaxonomyAssociation) baseObject;
			if (taxonomyAssociation.isTaxonomyAssociationFor(baseObjectToFindTaxonomyAssociationsFor))
				taxonomyAssociationsForType.add(taxonomyAssociation);
		}
		
		return taxonomyAssociationsForType;
	}
	
	public Vector<AbstractTaxonomyAssociation> findTaxonomyAssociationsForPoolName(final String taxonomyAssociationPoolName)
	{
		Vector<AbstractTaxonomyAssociation> taxonomyAssociationsForType = new Vector<AbstractTaxonomyAssociation>();
		Vector<BaseObject> taxonomyAssociations = getAllObjects();
		for(BaseObject baseObject : taxonomyAssociations)
		{
			AbstractTaxonomyAssociation taxonomyAssociation = (AbstractTaxonomyAssociation) baseObject;
			if (taxonomyAssociation.isTaxonomyAssociationFor(taxonomyAssociationPoolName))
				taxonomyAssociationsForType.add(taxonomyAssociation);
		}
		
		return taxonomyAssociationsForType;
	}
}
