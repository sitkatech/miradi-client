/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objecthelpers.TaxonomyElement;
import org.miradi.objecthelpers.TaxonomyElementList;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.MiradiShareTaxonomySchema;
import org.miradi.utils.CodeList;

public class MiradiShareTaxonomy extends BaseObject
{
	public MiradiShareTaxonomy(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, createSchema(objectManager));
	}

	public static MiradiShareTaxonomySchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static MiradiShareTaxonomySchema createSchema(ObjectManager objectManager)
	{
		return (MiradiShareTaxonomySchema) objectManager.getSchemas().get(ObjectType.MIRADI_SHARE_TAXONOMY);
	}

	public TaxonomyElementList getTaxonomyElements() throws Exception
	{
		return new TaxonomyElementList(getData(MiradiShareTaxonomySchema.TAG_TAXONOMY_ELEMENTS));
	}
	
	public CodeList getTopLevelTaxonomyElementCodes() throws Exception
	{
		return getCodeList(MiradiShareTaxonomySchema.TAG_TAXONOMY_TOP_LEVEL_ELEMENT_CODES);
	}
	
	public TaxonomyElement findTaxonomyElement(String taxonomyElementCode) throws Exception
	{
		TaxonomyElementList taxonomyElementList = getTaxonomyElements();
		for(TaxonomyElement taxonomyElement : taxonomyElementList)
		{
			if (taxonomyElement.getCode().equals(taxonomyElementCode))
				return taxonomyElement;
		}
		
		return null;
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
		return MiradiShareTaxonomySchema.getObjectType() == onbjectType;
	}
	
	public static MiradiShareTaxonomy find(ObjectManager objectManager, ORef miradiShareTaxonomyRef)
	{
		return (MiradiShareTaxonomy) objectManager.findObject(miradiShareTaxonomyRef);
	}
	
	public static MiradiShareTaxonomy find(Project project, ORef miradiShareTaxonomyRef)
	{
		return find(project.getObjectManager(), miradiShareTaxonomyRef);
	}
}
