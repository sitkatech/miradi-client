/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.umbrella;

import java.io.File;

import org.miradi.main.MainWindow;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.GenericMiradiFileFilter;
import org.miradi.utils.MpfFileFilterWithDirectories;
import org.miradi.utils.ProgressInterface;

public class MpfProjectImporter extends AbstractProjectImporter
{
	public MpfProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	@Override
	protected void createProject(File importFile, File newProjectFile, ProgressInterface progressIndicator) throws Exception
	{
		FileUtilities.copyFile(importFile, newProjectFile);
	}

	@Override
	protected GenericMiradiFileFilter[] getFileFilters()
	{
		return new GenericMiradiFileFilter[] {new MpfFileFilterWithDirectories()};
	}

	@Override
	protected void possiblyNotifyUserOfAutomaticMigration(File file) throws Exception
	{
	}
}
