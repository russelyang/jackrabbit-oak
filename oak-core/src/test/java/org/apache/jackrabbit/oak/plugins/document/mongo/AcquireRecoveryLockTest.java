/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.oak.plugins.document.mongo;

import java.util.List;

import org.apache.jackrabbit.oak.plugins.document.AbstractMongoConnectionTest;
import org.apache.jackrabbit.oak.plugins.document.ClusterNodeInfo;
import org.apache.jackrabbit.oak.plugins.document.ClusterNodeInfoDocument;
import org.apache.jackrabbit.oak.plugins.document.DocumentMK;
import org.junit.Ignore;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AcquireRecoveryLockTest extends AbstractMongoConnectionTest {

    // OAK-4131
    @Ignore
    @Test
    public void recoveryBy() throws Exception {
        MongoDocumentStore store = new MongoDocumentStore(
                mongoConnection.getDB(), new DocumentMK.Builder());
        MongoMissingLastRevSeeker seeker = new MongoMissingLastRevSeeker(store);
        List<ClusterNodeInfoDocument> infoDocs = newArrayList(seeker.getAllClusters());
        assertEquals(1, infoDocs.size());
        int clusterId = infoDocs.get(0).getClusterId();
        int otherClusterId = clusterId + 1;
        seeker.acquireRecoveryLock(clusterId, otherClusterId);
        ClusterNodeInfoDocument doc = seeker.getClusterNodeInfo(clusterId);
        Object recoveryBy = doc.get(ClusterNodeInfo.REV_RECOVERY_BY);
        assertNotNull(recoveryBy);
        assertEquals(Long.class, recoveryBy.getClass());
    }
}