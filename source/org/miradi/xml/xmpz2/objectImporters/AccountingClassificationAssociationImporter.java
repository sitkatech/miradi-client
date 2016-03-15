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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.AbstractTaxonomyAssociationSchema;
import org.miradi.schemas.AccountingClassificationAssociationSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;


public class AccountingClassificationAssociationImporter extends BaseObjectImporter
{
	public AccountingClassificationAssociationImporter(Xmpz2XmlImporter importerToUse, String poolNameForTypeToUse, int parentTypeToUse)
	{
		super(importerToUse, new AccountingClassificationAssociationSchema());
		
		accountingClassificationAssociationPoolName = poolNameForTypeToUse;
		parentType = parentTypeToUse;
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);

		getImporter().setData(refToUse, AccountingClassificationAssociationSchema.TAG_ACCOUNTING_CLASSIFICATION_ASSOCIATION_POOL_NAME, accountingClassificationAssociationPoolName);
		getImporter().setData(refToUse, AbstractTaxonomyAssociationSchema.TAG_BASE_OBJECT_TYPE, parentType);
	}

	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(AbstractTaxonomyAssociationSchema.TAG_BASE_OBJECT_TYPE))
			return true;
		
		if (tag.equals(AbstractTaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_CODE))
			return true;
		
		if (tag.equals(AccountingClassificationAssociationSchema.TAG_ACCOUNTING_CLASSIFICATION_ASSOCIATION_POOL_NAME))
			return true;
		
		return false;
	}

	@Override
	public ORef createBaseObject(final BaseObjectImporter importer,	Node baseObjectNode) throws Exception
	{
		ORef newTaxonomyAssociationRef = getProject().createObject(AccountingClassificationAssociationSchema.getObjectType());
		String taxonomyAssociationCode = getImporter().getAttributeValue(baseObjectNode, ACCOUNTING_CLASSIFICATION_ASSOCIATION_CODE);
		getImporter().setData(newTaxonomyAssociationRef, AbstractTaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_CODE, taxonomyAssociationCode);
		
		return newTaxonomyAssociationRef;
	}
	
	@Override
	public String createPoolElementName() throws Exception
	{
		return accountingClassificationAssociationPoolName;
	}
	
	@Override
	protected String getXmpz2ElementName() throws Exception 
	{
		return accountingClassificationAssociationPoolName;
	}

	private String accountingClassificationAssociationPoolName;
	private int parentType;
}
