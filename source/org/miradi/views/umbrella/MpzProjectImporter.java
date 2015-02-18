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

package org.miradi.views.umbrella;

import java.io.File;

import org.martus.util.UnicodeWriter;
import org.miradi.dialogs.base.ProgressDialog;
import org.miradi.exceptions.UserCanceledException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.MpzToMpfConverter;
import org.miradi.utils.GenericMiradiFileFilter;
import org.miradi.utils.MiradiBackgroundWorkerThread;
import org.miradi.utils.MpzFileFilterForChooserDialog;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.ZipFileFilterForChooserDialog;

public class MpzProjectImporter extends AbstractProjectImporter
{
	public MpzProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	@Override
	protected void createProject(File importFile, File newProjectFile, ProgressInterface progressIndicator) throws Exception
	{
		ProgressDialog dialog = new ProgressDialog(getMainWindow(), EAM.text("Importing MPZ"));
		MiradiBackgroundWorkerThread worker = new ImportMpzWorker(importFile, newProjectFile, dialog);
		dialog.doWorkInBackgroundWhileShowingProgress(worker);
		worker.cleanup();
	}

	@Override
	protected void possiblyNotifyUserOfAutomaticMigration(File importFile) throws Exception
	{
		CpmzProjectImporter.possiblyNotifyUserOfAutoMigration(importFile);
	}
	
	private static class ImportMpzWorker extends MiradiBackgroundWorkerThread
	{
		protected ImportMpzWorker(File importFrom, File saveTo, ProgressInterface progressToNotify)
		{
			super(progressToNotify);
			
			mpzFile = importFrom;
			mpfFile = saveTo;
		}

		@Override
		protected void doRealWork() throws Exception
		{
			String contents = convertMpzToMpfString(mpzFile);
			if(getProgressIndicator().shouldExit())
				throw new UserCanceledException();

			getProgressIndicator().setStatusMessage(EAM.text("Writing..."), 1);
			UnicodeWriter writer = new UnicodeWriter(mpfFile);
			writer.write(contents);
			writer.close();
		}
		
		private String convertMpzToMpfString(File mpzFileToImport) throws Exception
		{
			return MpzToMpfConverter.convert(mpzFileToImport, getProgressIndicator());
		}

		File mpzFile;
		File mpfFile;
	}

	@Override
	protected GenericMiradiFileFilter[] getFileFilters()
	{
		return new GenericMiradiFileFilter[] {new ZipFileFilterForChooserDialog(), new MpzFileFilterForChooserDialog()};
	}

}
