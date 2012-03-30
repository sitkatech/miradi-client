/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.schemas;

import org.miradi.objects.AccountingCode;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;

public class ResourceAssignmentSchema extends AssignmentSchema
{
	public ResourceAssignmentSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaBaseId(ResourceAssignment.TAG_RESOURCE_ID, ProjectResource.getObjectType());
		createFieldSchemaBaseId(ResourceAssignment.TAG_ACCOUNTING_CODE_ID, AccountingCode.getObjectType());
		createFieldSchemaBaseId(ResourceAssignment.TAG_FUNDING_SOURCE_ID, FundingSourceSchema.getObjectType());
		
		createPseudoFieldSchemaString(ResourceAssignment.PSEUDO_TAG_PROJECT_RESOURCE_LABEL);
		createPseudoFieldSchemaString(ResourceAssignment.PSEUDO_TAG_OWNING_FACTOR_NAME);

		
	}
}
