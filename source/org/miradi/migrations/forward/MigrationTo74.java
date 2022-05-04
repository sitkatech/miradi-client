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

package org.miradi.migrations.forward;

import org.miradi.main.EAM;
import org.miradi.migrations.AbstractMigration;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawProject;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;

import java.util.HashMap;

public class MigrationTo74 extends AbstractMigration
{
    public MigrationTo74(RawProject rawProjectToUse)
    {
        super(rawProjectToUse);
    }

    @Override
    protected MigrationResult migrateForward() throws Exception
    {
        return MigrationResult.createSuccess();
    }

    @Override
    protected MigrationResult reverseMigrate() throws Exception
    {
        MigrationResult migrationResult = MigrationResult.createUninitializedResult();

        if (getRawProject().containsAnyObjectsOfType(ObjectType.OUTPUT))
        {
            ORefList outputRefs = getRawProject().getAllRefsForType(ObjectType.OUTPUT);

            if (!outputRefs.isEmpty())
            {
                HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
                tokenReplacementMap.put("%outputCount", Integer.toString(outputRefs.size()));
                final String dataLossMessage = EAM.substitute(EAM.text("%outputCount Output(s) will be removed."), tokenReplacementMap);
                migrationResult.addDataLoss(dataLossMessage);
            }

            getRawProject().deletePoolWithData(ObjectType.OUTPUT);
        }

        return migrationResult;
    }

    @Override
    protected int getToVersion()
    {
        return VERSION_TO;
    }

    @Override
    protected int getFromVersion()
    {
        return VERSION_FROM;
    }

    @Override
    protected String getDescription()
    {
        return EAM.text("This migration handles the removal of outputs.");
    }

    public static final int VERSION_FROM = 73;
    public static final int VERSION_TO = 74;
}

