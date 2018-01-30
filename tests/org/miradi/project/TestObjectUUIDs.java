package org.miradi.project;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;

import java.util.HashSet;

public class TestObjectUUIDs extends TestCaseWithProject
{
    public TestObjectUUIDs(String name)
    {
        super(name);
    }

    public void testObjectUUIDs() throws Exception
    {
        HashSet<String> UUIDList = new HashSet<String>();

        getProject().populateEverything();

        for (int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
        {
            EAMObjectPool pool = getProject().getPool(objectType);
            if(pool == null)
                continue;

            for (BaseObject object : pool.getAllObjects())
            {
                if (ObjectType.requiresUUID(objectType))
                {
                    assertTrue("Object schema expects UUID field", object.getSchema().containsField(BaseObject.TAG_UUID));
                    ObjectData uuidField = object.getField(BaseObject.TAG_UUID);
                    assertFalse("UUID cannot be empty?", uuidField.isEmpty());
                    String uuid = uuidField.get();
                    assertFalse("UUID should be unique within project?", UUIDList.contains(uuid));
                    UUIDList.add(uuid);
                }
                else
                {
                    assertFalse("Object schema does not expect UUID field", object.getSchema().containsField(BaseObject.TAG_UUID));
                }
            }
        }
    }
}
