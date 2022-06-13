/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.questions;

import org.miradi.dialogs.dashboard.StaticLongDescriptionProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.TwoLevelEntry;
import org.miradi.objecthelpers.TwoLevelFileLoader;
import org.miradi.utils.CodeList;

import java.util.*;
import java.util.stream.Collectors;


public abstract class TaxonomyClassificationQuestion extends SingleSelectTwoLevelQuestion
{
	public TaxonomyClassificationQuestion(TwoLevelFileLoader twoLevelFileLoaderToUse)
	{
		super(twoLevelFileLoaderToUse);
	}

	@Override
	protected ChoiceItem[] loadChoices()
	{
        try
        {
            Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
            TwoLevelEntry[] twoLevelEntries = this.getTwoLevelFileLoader().load();

            List<TwoLevelEntry> rootEntries = Arrays.stream(twoLevelEntries).filter(i -> i.getEntryLevel() == 0).collect(Collectors.toList());
            List<TwoLevelEntry> childEntries = Arrays.stream(twoLevelEntries).filter(i -> i.getEntryLevel() != 0).collect(Collectors.toList());
            Map<String, List<TwoLevelEntry>> childEntriesMap = childEntries.stream().collect(Collectors.groupingBy(TwoLevelEntry::getParentEntryCode));

            for (TwoLevelEntry rootEntry : rootEntries)
            {
                ChoiceItemWithChildren choice = createChoiceItem(childEntriesMap, rootEntry);
				choices.add(choice);
            }

            return choices.toArray(new ChoiceItem[0]);
        }
		catch (Exception e)
        {
            EAM.logException(e);
            throw new RuntimeException("error processing two level entry inside:" + this.getTwoLevelFileLoader().getFileName());
        }
	}

    @Override
	public CodeList getAllCodes()
	{
		CodeList allCodes = new CodeList();
		ChoiceItem[] choices = getChoices();
		for (int index = 0; index < choices.length; ++index)
		{
			ChoiceItem choiceItem = choices[index];
			allCodes.addAll(getCodesOfItemAndChildren(choiceItem));
		}

		return allCodes;
	}

    private CodeList getCodesOfItemAndChildren(ChoiceItem parentChoiceItem)
	{
		CodeList allCodes = new CodeList();
		allCodes.add(parentChoiceItem.getCode());
		Vector<ChoiceItem> children = parentChoiceItem.getChildren();
		for (ChoiceItem choiceItem: children)
		{
			allCodes.addAll(getCodesOfItemAndChildren(choiceItem));
		}

		return allCodes;
	}

    private ChoiceItemWithChildren createChoiceItem(Map<String, List<TwoLevelEntry>> childEntriesMap, TwoLevelEntry entry) throws Exception
    {
        String code = entry.getEntryCode();
        String label = getSafeXmlEncodedValue(entry.getEntryLabel());
        String description  = getSafeXmlEncodedValue(entry.getDescription());
        String longDescription = getSafeXmlEncodedValue(entry.getLongDescription());

        ChoiceItemWithChildren choice = new ChoiceItemWithChildren(code, label, description, new StaticLongDescriptionProvider(longDescription));
        choice.setSelectable(entry.isSelectable());

        List<TwoLevelEntry> childEntries = childEntriesMap.get(code);
        if (childEntries != null)
            addChildrenChoices(childEntriesMap, choice, childEntries);

        return choice;
    }

	private void addChildrenChoices(Map<String, List<TwoLevelEntry>> childEntriesMap, ChoiceItemWithChildren parentChoiceItem, List<TwoLevelEntry> childEntries) throws Exception
	{
        for (TwoLevelEntry childEntry: childEntries)
        {
            ChoiceItemWithChildren choice = createChoiceItem(childEntriesMap, childEntry);
            parentChoiceItem.addChild(choice);
        }
	}

    public ChoiceItem findChoiceItem(String code)
    {
        Optional<ChoiceItem> rootChoiceItem = Arrays.stream(getChoices()).findFirst();

        if(rootChoiceItem.isPresent())
        {
            ChoiceItemWithChildren choiceItem = (ChoiceItemWithChildren)rootChoiceItem.get();
            return recursivelyFindChoiceItem(choiceItem, code);
        }

        return null;
    }

    private ChoiceItem recursivelyFindChoiceItem(ChoiceItem choiceItem, String codeToFind)
    {
        if (choiceItem.getCode().equals(codeToFind))
            return choiceItem;

        for(ChoiceItem childChoiceItem : choiceItem.getChildren())
        {
            ChoiceItem foundChoiceItem = recursivelyFindChoiceItem(childChoiceItem, codeToFind);
            if (foundChoiceItem != null)
                return foundChoiceItem;
        }

        return null;
    }

	@Override
	public boolean hasLongDescriptionProvider()
	{
		return true;
	}
}
