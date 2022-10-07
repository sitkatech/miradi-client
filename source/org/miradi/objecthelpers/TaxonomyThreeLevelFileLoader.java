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

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.utils.StringUtilities;

public class TaxonomyThreeLevelFileLoader extends TwoLevelFileLoader
{
    public TaxonomyThreeLevelFileLoader(String fileNameToUse)
    {
        super(fileNameToUse);
    }

    @Override
    protected Vector<TwoLevelEntry> processVector(Vector<Vector<String>> fileVector)
    {
        Vector<TwoLevelEntry> taxonomyItems = new Vector<TwoLevelEntry>();
        taxonomyItems.add(new ThreeLevelEntry("", EAM.text("Not Specified")));

        String prevLevel1Code = "";
        String prevLevel2Code = "";

        int level1Index = 0;
        int level2Index = 0;
        int level3Index = 0;

        for(int i = 0; i < fileVector.size(); ++i)
        {
            Vector row = fileVector.get(i);
            if(row.size() < columnCount)
                throw new RuntimeException("Not enough elements in: " + row);

            String code = (String) row.get(indexCode);
            String level1Descriptor = (String) row.get(indexLevel1);
            String level2Descriptor = (String) row.get(indexLevel2);
            String level3Descriptor = (String) row.get(indexLevel3);
            String longDescription = buildLongDescription(row);

            if(!getLevel1Code(code).equals(prevLevel1Code))
            {
                level2Index = 0;
                level3Index = 0;
                prevLevel1Code = getLevel1Code(code);

                ++level1Index;
                String level1Code = getLevel1Code(code);
                String taxonomyLevelText = level1Code + " " + level1Descriptor;
                taxonomyItems.add(new ThreeLevelEntry(level1Code, taxonomyLevelText, "", longDescription, 1, ""));
            }

            if(!getLevel2Code(code).equals(prevLevel2Code))
            {
                level3Index = 0;
                prevLevel2Code = getLevel2Code(code);

                ++level2Index;
                String taxonomyLevel2Text = " " + getLevel2CodeLabel(code) + " " + level2Descriptor;
                taxonomyItems.add(new ThreeLevelEntry(getLevel2Code(code), taxonomyLevel2Text, "", longDescription, 2, prevLevel1Code));
            }

            ++level3Index;
            String taxonomyLevel3Text = " " + getLevel3CodeLabel(code) + " " + level3Descriptor;
            TwoLevelEntry entry = new ThreeLevelEntry(code, taxonomyLevel3Text, "", longDescription, 3, prevLevel2Code);
            taxonomyItems.add(entry);
        }

        return taxonomyItems;
    }

    private String getLevel1Code(String code)
    {
        return getCodeParts(code)[0];
    }

    private String getLevel2Code(String code)
    {
        return getLevel1Code(code) + "." + getCodeParts(code)[1];
    }

    private String getLevel2CodeLabel(String code)
    {
        return getCodeParts(code)[1];
    }

    private String getLevel3CodeLabel(String code)
    {
        return getLevel2CodeLabel(code) + "." + getCodeParts(code)[2];
    }

    private String[] getCodeParts(String code)
    {
        return code.split("\\.");
    }

    private String buildLongDescription(Vector row)
    {
        return row.get(indexLevel1Def) + StringUtilities.NEW_LINE +
                row.get(indexLevel2Def) + StringUtilities.NEW_LINE +
                row.get(indexLevel3Def) + StringUtilities.NEW_LINE +
                EAM.text("Examples") + ": " + row.get(indexExamples);
    }

    @Override
    protected TwoLevelEntry createEntry(Vector row)
    {
        throw new RuntimeException("TaxonomyTwoLevelFileLoader overrides processVector and does not call super.processVector");
    }

    private final int indexCode = 0;
    private final int indexLevel1 = 1;
    private final int indexLevel2 = 2;
    private final int indexLevel3 = 3;
    private final int indexLevel1Def = 4;
    private final int indexLevel2Def = 5;
    private final int indexLevel3Def = 6;
    private final int indexExamples = 7;
    private final int columnCount = 8;
}
