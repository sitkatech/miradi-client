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

import org.miradi.objects.AbstractTaxonomyAssociation;
import org.miradi.objects.AccountingClassificationAssociation;

import java.util.Comparator;

public class AccountingClassificationAssociationBySequenceNoSorter implements Comparator<AbstractTaxonomyAssociation>
{
	public int compare(AbstractTaxonomyAssociation abstractTaxonomyAssociation1, AbstractTaxonomyAssociation abstractTaxonomyAssociation2)
	{
		AccountingClassificationAssociation accountingClassificationAssociation1 = (AccountingClassificationAssociation) abstractTaxonomyAssociation1;
		AccountingClassificationAssociation accountingClassificationAssociation2 = (AccountingClassificationAssociation) abstractTaxonomyAssociation2;

		if(accountingClassificationAssociation1 == accountingClassificationAssociation2)
			return 0;
		
		if(accountingClassificationAssociation1 == null)
			return -1;
		
		if(accountingClassificationAssociation2 == null)
			return 1;

		int sequenceNo1 = accountingClassificationAssociation1.getSequenceNo();
		int sequenceNo2 = accountingClassificationAssociation2.getSequenceNo();

		String name1 = accountingClassificationAssociation1.getLabel();
		String name2 = accountingClassificationAssociation2.getLabel();

		if (sequenceNo1 == sequenceNo2)
		{
			return name1.compareToIgnoreCase(name2);
		}
		else if (sequenceNo1 < sequenceNo2)
		{
			return -1;
		}
		return 1;
	}

}
