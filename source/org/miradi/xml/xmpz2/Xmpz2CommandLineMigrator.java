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


import org.martus.util.UnicodeWriter;
import org.miradi.exceptions.*;
import org.miradi.files.AbstractMpfFileFilter;
import org.miradi.main.ProjectFileImporterHelper;
import org.miradi.main.EAM;
import org.miradi.main.Miradi;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RawProjectLoader;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.project.Project;
import org.miradi.project.RawProjectSaver;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.Translation;
import org.miradi.views.umbrella.Xmpz2ProjectImporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Xmpz2CommandLineMigrator
{
    public static void main(String[] args)
    {
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

            // TODO: re-export project...

			System.out.println("Project successfully migrated");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Project not found");
            System.exit(1);
        }
        catch (ProjectFileTooOldException | OldSchemaVersionException e)
        {
            System.out.println("Project too old for migration");
            System.exit(1);
        }
        catch (ProjectFileTooNewException | FutureSchemaVersionException e)
        {
            System.out.println("Project too new for migration");
            System.exit(1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        finally
        {
            EAM.setDefaultHomeDirectoryPreference();
            System.exit(0);
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

    private static void exportProject(File projectFile) throws Exception
    {
    }
}
