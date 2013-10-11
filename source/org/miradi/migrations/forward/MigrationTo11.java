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

package org.miradi.migrations.forward;

import java.util.HashSet;

import org.miradi.main.EAM;
import org.miradi.migrations.AbstractMigrationVisitor;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RemoveMultipleFieldForwardMigrator;
import org.miradi.migrations.VersionRange;
import org.miradi.schemas.TncProjectDataSchema;

public class MigrationTo11 extends RemoveMultipleFieldForwardMigrator
{
	public MigrationTo11(RawProject rawProjectToUse)
	{
		super(rawProjectToUse, TncProjectDataSchema.getObjectType());
	}

	@Override
	protected HashSet<String> createFieldsToRemove()
	{
		HashSet<String> fieldsToRemove = new HashSet<String>();
		fieldsToRemove.add(LEGACY_TAG_TNC_PROJET_TYPES);
		fieldsToRemove.add(LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES);
		
		return fieldsToRemove;
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		AbstractMigrationVisitor visitor = createMigrateForwardVisitor();
		visitAllObjectsInPool(visitor);
		
		return visitor.getMigrationResult();
	}
	
	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		AbstractMigrationVisitor visitor = createReverseMigrateVisitor();
		visitAllObjectsInPool(visitor);
		
		return visitor.getMigrationResult();
	}

	@Override
	protected VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(VERSION_FROM, VERSION_TO);
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
		return EAM.text("This migration removes 2 Tnc fields");
	}
	
	private static final int VERSION_FROM = 10;
	public static final int VERSION_TO = 11;
	
	public final static String LEGACY_TAG_TNC_PROJET_TYPES = "ProjectPlaceTypes";
	public final static String LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES = "OrganizationalPriorities";
}
