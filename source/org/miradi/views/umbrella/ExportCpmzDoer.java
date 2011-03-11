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
package org.miradi.views.umbrella;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.zip.ZipOutputStream;

import org.miradi.exceptions.InvalidICUNSelectionException;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.objects.ProjectResource;
import org.miradi.project.ProjectMpzWriter;
import org.miradi.utils.CpmzFileChooser;
import org.miradi.utils.MpzFileFilterForChooserDialog;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.conpro.exporter.ConProMiradiXmlValidator;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;

public class ExportCpmzDoer extends XmlExporterDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		return (getProject().getDatabase().isLocalProject());
	}
	
	@Override
	protected boolean export(File chosen) throws Exception
	{
		Vector<String> messages = getMissingRequiredFieldMessages();
		if (messages.size() > 0)
		{
			String message = EAM.text("<HTML>Your project is missing some data that is required for upload to ConPro. Please correct the problems listed below then attempt to export the project again.<BR>");
			for (String missingFieldMessage : messages)
			{
				message += missingFieldMessage + "<BR>";
			}
			
			EAM.errorDialog(message);
			
			return false;
		}
		
		return exportCpmzFile(chosen);
	}

	@Override
	protected CpmzFileChooser createFileChooser()
	{
		return new CpmzFileChooser(getMainWindow());
	}

	private boolean exportCpmzFile(File chosen) throws Exception
	{
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(chosen));
		try
		{
			addProjectAsXmlToZip(zipOut);			
			addProjectAsMpzToZip(zipOut);
			addDiagramImagesToZip(zipOut);
			return true;
		}
		catch(ValidationException e)
		{
			EAM.logException(e);
			EAM.errorDialog(e.getMessage());
		}
		catch (InvalidICUNSelectionException e)
		{
			String errorMessage = EAM.substitute(EAM.text("Please choose a specific IUCN classification (Not a category). Fix needed for Strategy named:\n'%s'"), e.getStrategy().toString());
			EAM.logException(e);
			EAM.errorDialog(errorMessage);
		}
		catch(Exception e)
		{
			// NOTE: If we created no zip entries, the finally will throw
			// a generic exception instead of this one, so log this one
			// while we have a chance
			EAM.logException(e);
			throw(e);
		}
		finally
		{
			zipOut.close();
		}
		
		return false;
	}

	private void addProjectAsMpzToZip(ZipOutputStream zipOut) throws Exception
	{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(byteOut);
		try
		{
			ProjectMpzWriter.writeProjectZip(out, getProject());
		}
		finally
		{
			out.close();
		}
		
		writeContent(zipOut, PROJECT_ZIP_FILE_NAME, byteOut.toByteArray());
	}

	@Override
	protected XmlExporter createExporter() throws Exception
	{
		return new ConproXmlExporter(getProject());
	}
	
	@Override
	protected boolean isValidXml(ByteArrayInputStream inputStream) throws Exception
	{
		return new ConProMiradiXmlValidator().isValid(inputStream);
	}
	
	@Override
	protected boolean doesConfirmBetaExport() throws Exception
	{
		return true;
	}

	private Vector<String> getMissingRequiredFieldMessages()
	{
		Vector<String> missingFieldMessages = new Vector<String>();
		if (getProject().getMetadata().getProjectName().length() == 0)
			missingFieldMessages.add(EAM.text("Project Name - Located in Summary View, Project Tab, Project Name field"));
		
		if (findATeamContact() == null)
			missingFieldMessages.add(EAM.text("Team Contact - At least one project Team Member must be specified as Team Contact Located in Summary View, Team Tab, Roles field"));
		
		if (findATeamContact() != null && findATeamContact().getEmail().length() == 0)
			missingFieldMessages.add(EAM.text("Team Contact Email - The Team Contact must have an email address. Located in Summary View, Team Tab, ensure Team Contact has email specified"));
		
		if (getProject().getMetadata().getCountryCodes().isEmpty())
			missingFieldMessages.add(EAM.text("Country - Located in Summary View, Location Tab. Select at least one Country"));
		
		return missingFieldMessages;
	}

	private ProjectResource findATeamContact()
	{
		ProjectResource[] resources = getProject().getResourcePool().getAllProjectResources();
		for (int index = 0; index < resources.length; ++index)
		{
			if (resources[index].isTeamContact())
				return resources[index];
		}
		
		return null;
	}

	public static final String PROJECT_ZIP_FILE_NAME = "project" + MpzFileFilterForChooserDialog.EXTENSION;
}
