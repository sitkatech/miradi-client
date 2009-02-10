/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.wizard.noproject.projectlist;

import java.io.File;
import java.io.IOException;

import org.miradi.main.EAM;

public class ProjectListMoveToDirectory extends ProjectListAction
{

	public ProjectListMoveToDirectory(ProjectListTreeTable tableToUse)
	{
		super(tableToUse, EAM.text("Move To"));
	}

	@Override
	protected void doWork() throws Exception
	{
		DirectorySelectionDialog dialog = new DirectorySelectionDialog(getMainWindow());
		dialog.setVisible(true);
		File selectedDirectory = dialog.getSelectedDirectory();
		if (selectedDirectory == null)
			return;
		
		File fileToMove = getFile();
		
		File newLocation = new File(selectedDirectory, fileToMove.getName());
		boolean wasMoved = fileToMove.renameTo(newLocation);
		if (!wasMoved)
			throw new IOException();
	}

	@Override
	protected String getErrorMessage()
	{
		return EAM.text("Error moving project: ");
	}
}
