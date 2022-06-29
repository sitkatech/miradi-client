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
package org.miradi.objects;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.project.ObjectManager;
import org.miradi.schemas.BaseObjectSchema;


abstract public class AbstractDiagramObject extends BaseObject
{
    public AbstractDiagramObject(ObjectManager objectManager, BaseId idToUse, final BaseObjectSchema schemaToUse) throws Exception
    {
        super(objectManager, idToUse, schemaToUse);

		setZIndex(getDefaultZIndex());
    }

    public int getZIndex()
    {
        return getIntegerData(TAG_Z_INDEX);
    }

    protected void setZIndex(int zIndex) throws Exception
    {
        setData(TAG_Z_INDEX, String.valueOf(zIndex));
    }

    protected abstract int getMinZIndex() throws Exception;

    public CommandSetObjectData createCommandToIncrementZIndex() throws Exception
    {
        int zIndex = getZIndex();
        int updatedZIndex = zIndex + 1;

        return new CommandSetObjectData(getSchema().getType(), getId(), AbstractDiagramObject.TAG_Z_INDEX, updatedZIndex);
    }

    public CommandSetObjectData createCommandToMaximizeZIndex(DiagramObject diagramObject) throws Exception
    {
        int zIndex = diagramObject.getTopZIndex();
        int updatedZIndex = zIndex + 1;

        return new CommandSetObjectData(getSchema().getType(), getId(), AbstractDiagramObject.TAG_Z_INDEX, updatedZIndex);
    }

    public CommandSetObjectData createCommandToDecrementZIndex() throws Exception
    {
		int zIndex = getZIndex();
        int minZIndex = getMinZIndex();
        int updatedZIndex = Math.max(minZIndex, zIndex - 1);

        return new CommandSetObjectData(getSchema().getType(), getId(), AbstractDiagramObject.TAG_Z_INDEX, updatedZIndex);
    }

        public CommandSetObjectData createCommandToMinimizeZIndex(DiagramObject diagramObject) throws Exception
    {
        int zIndex = diagramObject.getBottomZIndex();
        int minZIndex = getMinZIndex();
        int updatedZIndex = Math.max(minZIndex, zIndex - 1);

        return new CommandSetObjectData(getSchema().getType(), getId(), AbstractDiagramObject.TAG_Z_INDEX, updatedZIndex);
    }

    public static int getDefaultZIndex()
    {
        return 0;
    }

    public static final String TAG_Z_INDEX = "ZIndex";
}
