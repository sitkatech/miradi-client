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

package org.miradi.migrations;

import java.util.HashSet;


public class MigrationResult extends HashSet<String>
{
	private MigrationResult()
	{
	}
	
	public static MigrationResult createUninitializedResult()
	{
		MigrationResult migrationResult = new MigrationResult();
		migrationResult.add(UNINITIALIZED);
		
		return migrationResult;
	}
	
	public static MigrationResult createSuccess()
	{
		MigrationResult migrationResult = new MigrationResult();
		migrationResult.addSuccess();
		
		return migrationResult;
	}
	
	public static MigrationResult createDataLoss()
	{
		MigrationResult migrationResult = new MigrationResult();
		migrationResult.addDataLoss();
		
		return migrationResult;
	}
	
	public void merge(MigrationResult migrationResult)
	{
		addAll(migrationResult);
	}
	
	public boolean didSucceed()
	{
		return contains(SUCCESS);
	}
	
	public boolean didLooseData()
	{
		return contains(DATA_LOSS);
	}
	
	public boolean didFail()
	{
		return contains(FAILED);
	}
	
	public boolean cannotMigrate()
	{
		return contains(CANNOT_MIGRATE);
	}

	public void addSuccess()
	{
		add(SUCCESS);
	}

	public void addDataLoss()
	{
		add(DATA_LOSS);
	}

	private static final String SUCCESS = "Success";
	private static final String DATA_LOSS = "DataLoss";
	private static final String FAILED = "Failed";
	private static final String CANNOT_MIGRATE = "CannotMigrate";
	private static final String UNINITIALIZED = "Uninitialized";
}
