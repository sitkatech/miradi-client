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

import org.martus.util.UnicodeStringWriter;
import org.miradi.exceptions.InvalidICUNSelectionException;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.project.ProjectSaver;
import org.miradi.utils.CpmzFileChooser;
import org.miradi.utils.MpfFileFilter;
import org.miradi.utils.MpzFileFilterForChooserDialog;
import org.miradi.utils.ProgressInterface;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.conpro.exporter.ConProMiradiXmlValidator;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;

public class ExportCpmzDoer extends XmlExporterDoer
{
	@Override
	protected boolean export(File chosen, ProgressInterface progressInterface) throws Exception
	{
		Vector<String> messages = getMissingRequiredFieldMessages();
		if (messages.size() > 0)
		{
			String message = EAM.text("<HTML>Your project is missing some data that is required for upload to ConPro.<br>" +
					"Please correct the problems listed below then attempt to export the project again:<BR><BR>");
			for (String missingFieldMessage : messages)
			{
				message += missingFieldMessage + "<BR><BR>";
			}
			
			EAM.errorDialog(message);
			
			return false;
		}
		
		return exportCpmzFile(chosen, progressInterface);
	}

	@Override
	protected CpmzFileChooser createFileChooser()
	{
		return new CpmzFileChooser(getMainWindow());
	}

	private boolean exportCpmzFile(File chosen, ProgressInterface progressInterface) throws Exception
	{
		progressInterface.setStatusMessage(EAM.text("save..."), 4);
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(chosen));
		try
		{
			addProjectAsXmlToZip(zipOut);
			progressInterface.incrementProgress();
			
			addProjectAsMpfToZip(zipOut);
			progressInterface.incrementProgress();
			
			addUnreadableMpzToZip(zipOut);
			progressInterface.incrementProgress();
			
			addDiagramImagesToZip(zipOut);
			progressInterface.incrementProgress();
			
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

	private void addProjectAsMpfToZip(ZipOutputStream zipOut) throws Exception
	{
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		ProjectSaver.saveProject(getProject(), writer);
		createZipEntry(zipOut, PROJECT_MPF_NAME, writer.toString());
	}
	
	private void addUnreadableMpzToZip(ZipOutputStream zipOut) throws Exception
	{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ZipOutputStream mpzZipOut = new ZipOutputStream(byteOut);
		try
		{
			createZipEntry(mpzZipOut, VERSION_ENTRY_PATH, FUTURE_VERSION);
		}
		finally
		{
			mpzZipOut.close();
		}
		
		createZipEntry(zipOut, PROJECT_ZIP_FILE_NAME, byteOut.toByteArray());
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
			missingFieldMessages.add(EAM.text("Team Contact - At least one project Team Member must be specified as Team Contact, <br>" +
					"located in Summary View, Team Tab, Roles field"));
		
		if (findATeamContact() != null && findATeamContact().getEmail().length() == 0)
			missingFieldMessages.add(EAM.text("Team Contact Email - The Team Contact must have an email address, <br>" +
					"located in Summary View, Team Tab, ensure Team Contact has email specified"));
		
		if (getProject().getMetadata().getCountryCodes().isEmpty())
			missingFieldMessages.add(EAM.text("Country - Located in Summary View, Location Tab. Select at least one Country"));
		
		if (!hasTncOperatingUnit())
			missingFieldMessages.add(EAM.text("TNC Operating Unit - At least one TNC Operating Unit must be selected, <br>" +
					"located in Summary View, TNC tab. Organizations other than TNC should select 'Non-TNC'"));
		
		return missingFieldMessages;
	}

	private boolean hasTncOperatingUnit()
	{
		ProjectMetadata metadata = getProject().getMetadata();
		if(metadata == null)
			return false;
		
		try
		{
			if(metadata.getCodeList(metadata.TAG_TNC_OPERATING_UNITS).size() > 0)
				return true;
		}
		catch(Exception e)
		{
			EAM.unexpectedErrorDialog(e);
		}
		
		return false;
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
	public static final String PROJECT_MPF_NAME = "project" + MpfFileFilter.EXTENSION;
	
	private static final String VERSION_ENTRY_PATH = "/project/json/version";
	private static final String FUTURE_VERSION = "{\"Version\":99999}";
}
