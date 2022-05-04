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
import org.miradi.migrations.RemoveDiagramFactorVisitor;
import org.miradi.objecthelpers.ObjectType;

public class MigrationTo77 extends AbstractMigration
{
    public MigrationTo77(RawProject rawProjectToUse)
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
        RemoveDiagramFactorVisitor visitor = new RemoveDiagramFactorVisitor(getRawProject(), ObjectType.ANALYTICAL_QUESTION);
        return visitor.visit();
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
        return EAM.text("This migration handles the removal of analytical questions.");
    }

    public static final int VERSION_FROM = 76;
    public static final int VERSION_TO = 77;
}

