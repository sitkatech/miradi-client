/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.umbrella;

import java.io.File;
import javax.swing.JFileChooser;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.project.ProjectZipper;



public class ExportZipFileDoer extends MainWindowDoer
{
	public boolean isAvailable() 
	{
		Project project = getProject();
		return project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		JFileChooser dlg = new JFileChooser();
		dlg.setDialogTitle(EAM.text("Title|Save Zip File"));
		dlg.setDialogType(JFileChooser.CUSTOM_DIALOG);
		dlg.setApproveButtonToolTipText(EAM.text("TT|Save Zip File"));
		if(dlg.showDialog(getMainWindow(), EAM.text("Save Zip")) != JFileChooser.APPROVE_OPTION)
			return;

		File chosen = dlg.getSelectedFile();
		if(chosen.exists())
		{
			String title = EAM.text("Title|Overwrite existing file?");
			String[] body = {EAM.text("This will replace the existing file.")};
			if(!EAM.confirmDialog(title, body))
				return;
			chosen.delete();
		}
		
		try 
		{
			String fileName = chosen.getAbsolutePath();
			if (!fileName.endsWith(".zip")) fileName = fileName + ".zip";
			File newFile = new File(fileName);
			zipFile(newFile); 
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CommandFailedException(EAM.text("Error Export To Zip: Possible Write Protected: ") + e);
		} 
	}

	private void zipFile(File out) throws Exception 
	{
		File projectDir = getProject().getDatabase().getTopDirectory();
		ProjectZipper.createProjectZipFile(out,projectDir);
	}
}
