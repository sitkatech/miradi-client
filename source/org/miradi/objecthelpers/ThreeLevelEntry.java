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
package org.miradi.objecthelpers;

public class ThreeLevelEntry extends TwoLevelEntry
{
    public ThreeLevelEntry(String code, String labelToUse)
    {
        this(code, labelToUse, "");
    }

    public ThreeLevelEntry(String code, String labelToUse, String descriptionToUse)
    {
        this(code, labelToUse, descriptionToUse, "", 0);
    }

    public ThreeLevelEntry(String code, String labelToUse, String descriptionToUse, String longDescriptionToUse, int entryLevelToUse)
    {
        super(code, labelToUse, descriptionToUse, longDescriptionToUse, entryLevelToUse, "");
    }

    public ThreeLevelEntry(String code, String labelToUse, String descriptionToUse, String longDescriptionToUse, int entryLevelToUse, String parentEntryCodeToUse)
    {
        super(code, labelToUse, descriptionToUse, longDescriptionToUse, entryLevelToUse, parentEntryCodeToUse);
    }

    @Override
    public boolean isSelectable()
    {
        if(super.isEmptyItem())
            return true;

        if(super.getEntryLevel() == 3)
            return true;

        return false;
    }
}
