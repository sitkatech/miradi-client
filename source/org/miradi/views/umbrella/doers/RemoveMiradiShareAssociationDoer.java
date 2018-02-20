/*
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.views.umbrella.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.MiradiShareProjectData;
import org.miradi.objects.ProjectMetadata;
import org.miradi.schemas.AbstractAssignmentSchema;
import org.miradi.schemas.MiradiShareProjectDataSchema;
import org.miradi.utils.CommandVector;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.umbrella.UmbrellaView;

import java.awt.*;
import java.util.Set;

public class RemoveMiradiShareAssociationDoer extends ObjectsDoer
{
    @Override
    public boolean isAvailable()
    {
        if(!super.isAvailable())
            return false;

        BaseObject singleSelectedObject = getSingleSelectedObject();
        if (singleSelectedObject == null)
            return false;

        return MiradiShareProjectData.is(singleSelectedObject);
    }

    @Override
    protected void doIt() throws Exception
    {
        if(!isAvailable())
            return;

        String[] buttons = {EAM.text("Yes"), EAM.text("No"), };
        final String title = EAM.substituteSingleString(EAM.text("Remove %s"), getObjectText());
        final String text = EAM.substituteSingleString(EAM.text("\nAre you sure you want to remove the %s?"), getObjectText());
        if(!EAM.confirmDialog(title, new String[]{text}, buttons))
            return;

        Cursor prevCursor = getMainWindow().getCursor();
        getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try
        {
            getProject().executeCommand(new CommandBeginTransaction());
            try
            {
                BaseObject miradiShareProjectData = getSingleSelectedObject();
                if (miradiShareProjectData != null)
                    getProject().executeWithoutRecording(createCommandsToRemoveMiradiShareAssociation(miradiShareProjectData));
            }
            finally
            {
                getProject().executeCommand(new CommandEndTransaction());
                UmbrellaView currentView = getMainWindow().getCurrentView();
                if (currentView != null)
                    currentView.refresh();
            }
        }
        catch(CommandFailedException e)
        {
            throw(e);
        }
        catch(Exception e)
        {
            EAM.logException(e);
            throw new CommandFailedException(e);
        }
        finally
        {
            getMainWindow().setCursor(prevCursor);
        }
    }

    @Override
    protected BaseObject getSingleSelectedObject()
    {
        BaseObject[] selectedObjects = getObjects();
        if (selectedObjects == null)
            return null;

        BaseObject selectedObject = null;

        for (BaseObject object : selectedObjects)
        {
            if (object.isType(MiradiShareProjectDataSchema.getObjectType()))
            {
                selectedObject = object;
                break;
            }
        }

        return selectedObject;
    }

    private CommandVector createCommandsToRemoveMiradiShareAssociation(BaseObject miradiShareProjectData) throws Exception
    {
        CommandVector commandsToRemoveMiradiShareAssociation = new CommandVector();

        commandsToRemoveMiradiShareAssociation.addAll(miradiShareProjectData.createCommandsToClear());
        commandsToRemoveMiradiShareAssociation.addAll(commandsToClearMiradiShareProjectCode());
        commandsToRemoveMiradiShareAssociation.addAll(commandsToClearAllTaxonomyClassifications());
        commandsToRemoveMiradiShareAssociation.addAll(commandsToDeleteAllObjects(ObjectType.ACCOUNTING_CLASSIFICATION_ASSOCIATION));
        commandsToRemoveMiradiShareAssociation.addAll(commandsToDeleteAllObjects(ObjectType.TAXONOMY_ASSOCIATION));
        commandsToRemoveMiradiShareAssociation.addAll(commandsToDeleteAllObjects(ObjectType.MIRADI_SHARE_TAXONOMY));

        return commandsToRemoveMiradiShareAssociation;
    }

    private CommandVector commandsToClearMiradiShareProjectCode()
    {
        CommandVector commandsToClearMiradiShareProjectCode = new CommandVector();

        ProjectMetadata metadata = getProject().getMetadata();
        StringRefMap externalIdsStringRefMap = metadata.getXenodataStringRefMap();
        Set<String> externalIds = externalIdsStringRefMap.getKeys();
        if (externalIds.contains(MiradiShareProjectData.MIRADI_SHARE_PROJECT_CODE))
        {
            StringRefMap updatedExternalIdsStringRefMap = new StringRefMap();
            for (String externalId : externalIds)
            {
                if (!externalId.equalsIgnoreCase(MiradiShareProjectData.MIRADI_SHARE_PROJECT_CODE))
                    updatedExternalIdsStringRefMap.add(externalId, externalIdsStringRefMap.getValue(externalId));
            }

            commandsToClearMiradiShareProjectCode.add(new CommandSetObjectData(ObjectType.PROJECT_METADATA, metadata.getId(), ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, updatedExternalIdsStringRefMap.toJsonString()));
        }

        return commandsToClearMiradiShareProjectCode;
    }

    private CommandVector commandsToDeleteAllObjects(int objectType) throws Exception
    {
        CommandVector commandsToDeleteAllObjects = new CommandVector();

        EAMObjectPool pool = getProject().getPool(objectType);
        if(pool != null)
        {
            for (BaseObject objectToDelete : pool.getAllObjects())
            {
                CommandVector commandsToDeleteChildrenAndObject = objectToDelete.createCommandsToDeleteChildrenAndObject();
                commandsToDeleteAllObjects.addAll(commandsToDeleteChildrenAndObject);
            }
        }

        return commandsToDeleteAllObjects;
    }

    private CommandVector commandsToClearAllTaxonomyClassifications() throws Exception
    {
        CommandVector commandsToClearAllTaxonomyClassifications = new CommandVector();

        for (int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
        {
            commandsToClearAllTaxonomyClassifications.addAll(commandsToClearObjectField(objectType, BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
            commandsToClearAllTaxonomyClassifications.addAll(commandsToClearObjectField(objectType, AbstractAssignmentSchema.TAG_ACCOUNTING_CLASSIFICATION_CONTAINER));
        }

        return commandsToClearAllTaxonomyClassifications;
    }

    private CommandVector commandsToClearObjectField(int objectType, String fieldTag) throws Exception
    {
        CommandVector commandsToClearObjectTaxonomyClassifications = new CommandVector();

        EAMObjectPool pool = getProject().getPool(objectType);
        if(pool != null)
        {
            for (BaseObject objectToClear : pool.getAllObjects())
            {
                if (!objectToClear.getSchema().containsField(fieldTag))
                    break;

                commandsToClearObjectTaxonomyClassifications.add(new CommandSetObjectData(objectType, objectToClear.getId(), fieldTag, ""));
            }
        }

        return commandsToClearObjectTaxonomyClassifications;
    }

    protected String getObjectText()
    {
        return EAM.text("Miradi Share Association");
    }
}
