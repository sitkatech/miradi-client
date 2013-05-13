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

package org.miradi.questions;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.TaxonomyElement;
import org.miradi.objecthelpers.TaxonomyElementList;

public class MiradiShareTaxonomyQuestion extends MultipleSelectStaticChoiceQuestion
{
	public MiradiShareTaxonomyQuestion(TaxonomyElementList taxonomyElementListToUse)
	{
		super(createTaxonomyChoices(taxonomyElementListToUse));
	}
	
	private static ChoiceItem[] createTaxonomyChoices(TaxonomyElementList taxonomyElementListToUse)
	{
		try
		{
			return createChoiceItems(taxonomyElementListToUse);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		
		return new ChoiceItem[0];
	}

	private static ChoiceItem[] createChoiceItems(TaxonomyElementList taxonomyElements)
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		for (TaxonomyElement taxonomyElement : taxonomyElements)
		{
			ChoiceItem choiceItem = new ChoiceItem(taxonomyElement.getCode(), taxonomyElement.getLabel(), taxonomyElement.getDescription());
			choices.add(choiceItem);
		}
		
		return choices.toArray(new ChoiceItem[0]);
	}
}
