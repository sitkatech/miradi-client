/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.migrations.forward.MigrationTo28;

public class TestMigrationTo28 extends AbstractTestMigration
{
	public TestMigrationTo28(String name)
	{
		super(name);
	}

	// note: since work plan data is subsequently removed from indicators / methods, we won't explicitly test here
	// logic is exactly the same as what is exercised for shared tasks in migration 28

	@Override
	protected int getFromVersion()
	{
		return MigrationTo28.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo28.VERSION_TO;
	}
}
