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

package org.miradi.migrations;



abstract public class AbstractMigration
{
	public AbstractMigration(RawProject rawProjectToUse)
	{
		rawProject = rawProjectToUse;
	}
	
	public MigrationResult forwardMigrateIfPossible(VersionRange desiredVersion) throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();
		if (canForwardMigrateThisVersion(getRawProject().getCurrentVersionRange(), desiredVersion))
		{
			migrationResult = migrateForward();
			updateProjectVersionRange(getPostForwardMigrationVersionRange());
		}
		
		return migrationResult;
	}

	public MigrationResult reverseMigrateIfPossible(VersionRange desiredVersion) throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();
		if (canReverseMigrateThisVersion(getRawProject().getCurrentVersionRange(), desiredVersion))
		{
			migrationResult = reverseMigrate();
			updateProjectVersionRange(getPostReverseMigrationVersionRange());
		}
		
		return migrationResult;
	}

	private boolean canForwardMigrateThisVersion(VersionRange currentVersionRange, VersionRange desiredVersion) throws Exception
	{
		VersionRange migratableVersionRange = getMigratableVersionRange();
		return migratableVersionRange.doesContain(currentVersionRange.getHighVersion()) && migratableVersionRange.getHighVersion() <= desiredVersion.getLowVersion();
	}
	
	private boolean canReverseMigrateThisVersion(VersionRange currentVersionRange, VersionRange desiredVersion) throws Exception
	{
		final VersionRange migratableVersionRange = getMigratableVersionRange();
		return migratableVersionRange.doesContain(currentVersionRange.getLowVersion()) && migratableVersionRange.getLowVersion() >= desiredVersion.getHighVersion();
	}

	protected RawProject getRawProject()
	{
		return rawProject;
	}
	
	private void updateProjectVersionRange(final VersionRange postMigrationVersionRange)
	{
		getRawProject().setCurrentVersionRange(postMigrationVersionRange);
	}

	private VersionRange getPostForwardMigrationVersionRange() throws Exception
	{
		return new VersionRange(getToVersion());
	}

	private VersionRange getPostReverseMigrationVersionRange() throws Exception
	{
		return new VersionRange(getFromVersion());
	}
	
	protected void removeField(RawObject rawObject, final String fieldTag)
	{
		rawObject.remove(fieldTag);
	}
	
	protected MigrationResult visitAllObjectsInPool(final AbstractMigrationVisitor visitor) throws Exception
	{
		getRawProject().visitAllObjectsInPool(visitor);
		
		return visitor.getMigrationResult();
	}
	
	protected MigrationResult visitAllORefsInPool(final AbstractMigrationORefVisitor visitor) throws Exception
	{
		getRawProject().visitAllORefsInPool(visitor);

		return visitor.getMigrationResult();
	}

	protected VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(getFromVersion(), getToVersion());
	}
	
	abstract protected int getToVersion();
	
	abstract protected int getFromVersion(); 
	
	abstract protected MigrationResult migrateForward() throws Exception;
	
	abstract protected MigrationResult reverseMigrate() throws Exception;
	
	abstract protected String getDescription();
	
	private RawProject rawProject;
}
