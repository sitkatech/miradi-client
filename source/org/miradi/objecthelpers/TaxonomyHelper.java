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

package org.miradi.objecthelpers;

import org.miradi.objectpools.TaxonomyAssociationPool;
import org.miradi.objects.MiradiShareTaxonomy;
import org.miradi.objects.TaxonomyAssociation;
import org.miradi.project.Project;
import org.miradi.schemas.MiradiShareTaxonomySchema;
import org.miradi.utils.CodeList;

public class TaxonomyHelper
{
	public static CodeList getTaxonomyElementCodes(Project projectToUse, TaxonomyClassificationMap taxononyClassificationList, String taxonomyAssociationCode)
	{
		return taxononyClassificationList.getTaxonomyElementCodes(projectToUse, taxonomyAssociationCode);
	}
	
	public static TaxonomyAssociation findTaxonomyAssociation(Project project, String taxonomyAssociationCode)
	{
		TaxonomyAssociationPool taxonomyAssociationPool = project.getTaxonomyAssociationPool();
		ORefList taxonomyAssociationsForType = taxonomyAssociationPool.getRefList();
		for(ORef taxonomyAssociationRef : taxonomyAssociationsForType)
		{
			TaxonomyAssociation taxonomyAssociation = TaxonomyAssociation.find(project, taxonomyAssociationRef);
			if (taxonomyAssociation.getTaxonomyAssociationCode().equals(taxonomyAssociationCode))
				return taxonomyAssociation;
		}

		return null;
	}
	
	public static MiradiShareTaxonomy getTaxonomyElementList(Project projectToUse, final TaxonomyAssociation taxonomyAssociation) throws Exception
	{
		String taxonomyCode = taxonomyAssociation.getTaxonomyCode();
		ORefList miradiShareTaxonomyRefs = projectToUse.getPool(MiradiShareTaxonomySchema.getObjectType()).getORefList();
		for(ORef miradiShareTaxonomyRef : miradiShareTaxonomyRefs)
		{
			MiradiShareTaxonomy miradiShareTaxonomy = MiradiShareTaxonomy.find(projectToUse, miradiShareTaxonomyRef);
			final String thisTaxonomyCode = miradiShareTaxonomy.getData(MiradiShareTaxonomySchema.TAG_TAXONOMY_CODE);
			if (thisTaxonomyCode.equals(taxonomyCode))
				return miradiShareTaxonomy;
		}
		
		throw new Exception("Taxonomy object could not be found for code:" + taxonomyCode);
	}
}
