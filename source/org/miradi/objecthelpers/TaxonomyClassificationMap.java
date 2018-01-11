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

package org.miradi.objecthelpers;

import org.miradi.main.EAM;
import org.miradi.objects.AbstractTaxonomyAssociation;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class TaxonomyClassificationMap extends CodeToCodeListMap
{
	public TaxonomyClassificationMap()
	{
		super();
	}

	public TaxonomyClassificationMap(String taxonomyCodeToTaxonomyElementCodesMap) throws Exception
	{
		this(new EnhancedJsonObject(taxonomyCodeToTaxonomyElementCodesMap));
	}
	
	public TaxonomyClassificationMap(EnhancedJsonObject jsonToUse) throws Exception
	{
		super(jsonToUse);
	}
	
	public static CodeList getTaxonomyElementCodes(Project projectToUse, String taxonomyClassificationMapAsString, String taxonomyAssociationCode) throws Exception
	{
		return  new TaxonomyClassificationMap(taxonomyClassificationMapAsString).getTaxonomyElementCodes(projectToUse, taxonomyAssociationCode);
	}
	
	public CodeList getTaxonomyElementCodes(Project projectToUse, String taxonomyAssociationCode)
	{
		try
		{
			AbstractTaxonomyAssociation taxonomyAssociation = TaxonomyHelper.findTaxonomyAssociation(projectToUse, taxonomyAssociationCode);
			if (taxonomyAssociation == null)
				return new CodeList();
			
			String taxonomyCode = taxonomyAssociation.getTaxonomyCode();
			if (contains(taxonomyCode))
			{
				return getCodeList(taxonomyCode);
			}
		}
		catch(Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		
		return new CodeList();
	}
}
