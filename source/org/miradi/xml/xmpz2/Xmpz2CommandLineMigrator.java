/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2;


import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeWriter;
import org.miradi.exceptions.FutureSchemaVersionException;
import org.miradi.exceptions.OldSchemaVersionException;
import org.miradi.exceptions.ProjectFileTooNewException;
import org.miradi.exceptions.ProjectFileTooOldException;
import org.miradi.files.AbstractMpfFileFilter;
import org.miradi.main.EAM;
import org.miradi.main.Miradi;
import org.miradi.main.ProjectFileImporterHelper;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RawProjectLoader;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.project.Project;
import org.miradi.project.ProjectLoader;
import org.miradi.project.RawProjectSaver;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.MiradiZipFile;
import org.miradi.utils.Translation;
import org.miradi.utils.Xmpz2ZipFileChooser;
import org.miradi.views.umbrella.XmlExporterDoer;
import org.miradi.views.umbrella.Xmpz2ProjectImporter;
import org.miradi.views.umbrella.doers.Xmpz2ProjectExportDoer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Xmpz2CommandLineMigrator
{
    public static void main(String[] args) throws Exception
    {
        int statusCode = 1;

        try
        {
            Miradi.addThirdPartyJarsToClasspath();
            Translation.initialize();

            if (args.length != 1)
            {
                System.out.println("Must specify path to project (XMPZ2 format) to migrate");
                System.exit(1);
            }

            File projectFile = new File(args[0]);
            if (!ProjectFileImporterHelper.isXmpz2(projectFile))
            {
                System.out.println("Invalid XMPZ2 project");
                System.exit(1);
            }

            File projectDirectory = projectFile.getParentFile();
            if (!EAM.isPreferredHomeDirectoryValid(projectDirectory))
            {
                System.out.println("Working directory not writable: " + projectDirectory.getAbsolutePath());
                System.exit(1);
            }

            EAM.setHomeDirectoryPreference(projectDirectory);
			System.out.println("Working directory set to: " + projectDirectory.getAbsolutePath());

            File newProjectFile = importProject(projectFile);

            migrateProject(newProjectFile);

            File exportedProjectFile = exportProject(newProjectFile);

            copyExistingDiagramImages(projectFile, exportedProjectFile);

			System.out.println("Project successfully migrated");

            statusCode = 0;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Project not found");
            throw new Exception(e.getMessage(), e);
        }
        catch (ProjectFileTooOldException | OldSchemaVersionException e)
        {
            System.out.println("Project too old for migration");
            throw new Exception(e.getMessage(), e);
        }
        catch (ProjectFileTooNewException | FutureSchemaVersionException e)
        {
            System.out.println("Project too new for migration");
            throw new Exception(e.getMessage(), e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new Exception(e.getMessage(), e);
        }
        finally
        {
            EAM.setDefaultHomeDirectoryPreference();
            System.exit(statusCode);
        }
    }

    private static File importProject(File projectFile) throws Exception
    {
		String proposedProjectName = FileUtilities.fileNameWithoutExtension(projectFile.getName());
		String proposedProjectFileName = AbstractMpfFileFilter.createNameWithExtension(proposedProjectName);
        File projectDirectory = projectFile.getParentFile();

        Files.deleteIfExists(Paths.get(projectDirectory.getAbsolutePath(), proposedProjectFileName));
        File newProjectFile = new File(projectDirectory, proposedProjectFileName);

        Xmpz2ProjectImporter importer = new Xmpz2ProjectImporter(new CommandLineMainWindow(), true);
        importer.createProject(projectFile, newProjectFile, new CommandLineProgressIndicator());

        return newProjectFile;
    }

    private static void migrateProject(File projectFile) throws Exception
    {
        MigrationManager migrationManager = new MigrationManager();
        migrationManager.validateProjectVersion(projectFile);

        if (migrationManager.needsMigration(projectFile))
        {
            RawProject rawProjectToMigrate = RawProjectLoader.loadProject(projectFile);
            MigrationResult migrationResult = migrationManager.migrate(rawProjectToMigrate, Project.getMiradiVersionRange());
            if (!migrationResult.didSucceed())
            {
                String message = EAM.text("Could not migrate");

                if (migrationResult.cannotMigrate())
                    message = migrationResult.getUserFriendlyGroupedCannotMigrateMessagesAsString();

                if (migrationResult.didLoseData())
                    message = migrationResult.getUserFriendlyGroupedDataLossMessagesAsString();

                throw new Exception(EAM.text("Migration failed: " + message));
            }

            String migratedProjectAsString = RawProjectSaver.saveProject(rawProjectToMigrate);
            UnicodeWriter fileWriter = new UnicodeWriter(projectFile);
            fileWriter.write(migratedProjectAsString);
            fileWriter.close();
        }
    }

    private static File exportProject(File projectFile) throws Exception
    {
        String projectFileNameWithoutExt = FileUtilities.fileNameWithoutExtension(projectFile.getName());
        String exportedProjectFileName = projectFileNameWithoutExt + "-exported." + Xmpz2ZipFileChooser.XMPZ_UI_EXTENSION_TAG;

        File projectDirectory = projectFile.getParentFile();
        Files.deleteIfExists(Paths.get(projectDirectory.getAbsolutePath(), exportedProjectFileName));
        File exportedProjectFile = new File(projectDirectory, exportedProjectFileName);

        String contents = UnicodeReader.getFileContents(projectFile);
        Project project = new Project(true);
        ProjectLoader.loadProject(new UnicodeStringReader(contents), project);
        project.finishOpeningAfterLoad(projectFile);

        Xmpz2ProjectExportDoer exporter = new Xmpz2ProjectExportDoer();
        exporter.export(project, exportedProjectFile, new CommandLineProgressIndicator());

        return exportedProjectFile;
    }

    private static void copyExistingDiagramImages(File originalProjectFile, File migratedProjectFile) throws Exception
    {
        String originalProjectFileNameWithoutExt = FileUtilities.fileNameWithoutExtension(originalProjectFile.getName());
        String updatedProjectFileName = originalProjectFileNameWithoutExt + "-migrated." + Xmpz2ZipFileChooser.XMPZ_UI_EXTENSION_TAG;

        File projectDirectory = originalProjectFile.getParentFile();
        Files.deleteIfExists(Paths.get(projectDirectory.getAbsolutePath(), updatedProjectFileName));
        File updatedProjectFile = new File(projectDirectory, updatedProjectFileName);

        ZipOutputStream updatedProjectFileZipOutputStream = new ZipOutputStream(new FileOutputStream(updatedProjectFile));

        MiradiZipFile originalProjectZipFile = new MiradiZipFile(originalProjectFile);
        Enumeration<? extends ZipEntry> originalProjectZipFileEntries = originalProjectZipFile.entries();
        while (originalProjectZipFileEntries.hasMoreElements())
        {
            ZipEntry zipEntry = originalProjectZipFileEntries.nextElement();
            if (zipEntry.getName().startsWith(XmlExporterDoer.IMAGES_DIR_NAME_IN_ZIP))
            {
                copyZipEntry(originalProjectZipFile, zipEntry, updatedProjectFileZipOutputStream);
                updatedProjectFileZipOutputStream.closeEntry();
            }
        }

        MiradiZipFile migratedProjectZipFile = new MiradiZipFile(migratedProjectFile);
        Enumeration<? extends ZipEntry> migratedProjectZipFileEntries = migratedProjectZipFile.entries();
        while (migratedProjectZipFileEntries.hasMoreElements())
        {
            ZipEntry zipEntry = migratedProjectZipFileEntries.nextElement();
            copyZipEntry(migratedProjectZipFile, zipEntry, updatedProjectFileZipOutputStream);
            updatedProjectFileZipOutputStream.closeEntry();
        }

        updatedProjectFileZipOutputStream.close();
    }

    private static void copyZipEntry(MiradiZipFile originalZipFile, ZipEntry zipEntry, ZipOutputStream zipOutputStream) throws Exception
    {
        byte[] buffer = new byte[512];

        ZipEntry copiedZipEntry = new ZipEntry(zipEntry);
        copiedZipEntry.setCompressedSize(-1);
        zipOutputStream.putNextEntry(copiedZipEntry);
        InputStream inputStream = originalZipFile.getInputStream(zipEntry);
        while (0 < inputStream.available())
        {
            int inputStreamBytes = inputStream.read(buffer);
            if (inputStreamBytes > 0)
                zipOutputStream.write(buffer,0, inputStreamBytes);
        }
        inputStream.close();
    }
}
