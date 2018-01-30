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
package org.miradi.objectdata;

import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.InvalidUUIDException;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;
import org.w3c.dom.Node;

import java.util.UUID;

public class UUIDData extends ObjectData
{
    public UUIDData(String tagToUse)
    {
        super(tagToUse);
        value = null;
    }

    @Override
    public void set(String newValue) throws Exception
    {
        if(newValue.length() == 0)
        {
            value = null;
            return;
        }

        try
        {
            value = UUID.fromString(newValue);
        }
        catch (Exception e)
        {
            throw new InvalidUUIDException(e);
        }
    }

    @Override
    public String get()
    {
        if(value == null)
            return "";
        return value.toString();
    }

    @Override
    public boolean equals(Object rawOther)
    {
        if(!(rawOther instanceof UUIDData))
            return false;

        UUIDData other = (UUIDData)rawOther;
        return get().equals(other.get());
    }

    @Override
    public boolean isUUIDData()
    {
        return true;
    }

    @Override
    public int hashCode()
    {
        return get().hashCode();
    }

    @Override
    public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
    {
        writer.writeUUIDData(baseObjectSchema, fieldSchema, get());
    }

    @Override
    public void readAsXmpz2XmlData(Xmpz2XmlImporter importer, Node node, ORef destinationRefToUse, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
    {
        importer.importUUIDField(node, baseObjectSchema.getXmpz2ElementName(), destinationRefToUse, fieldSchema.getTag());
    }

    @Override
    public String createXmpz2SchemaElementString(Xmpz2XmlSchemaCreator creator, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
    {
        return creator.createUUIDSchemaElement(baseObjectSchema, fieldSchema);
    }

    private UUID value;
}
