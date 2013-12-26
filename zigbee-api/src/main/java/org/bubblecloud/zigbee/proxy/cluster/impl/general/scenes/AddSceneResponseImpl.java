/*
   Copyright 2008-2013 CNR-ISTI, http://isti.cnr.it
   Institute of Information Science and Technologies 
   of the Italian National Research Council 


   See the NOTICE file distributed with this work for additional 
   information regarding copyright ownership

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.bubblecloud.zigbee.proxy.cluster.impl.general.scenes;

import org.bubblecloud.zigbee.proxy.cluster.api.core.Response;
import org.bubblecloud.zigbee.proxy.cluster.api.core.Status;
import org.bubblecloud.zigbee.proxy.cluster.api.core.ZBDeserializer;
import org.bubblecloud.zigbee.proxy.cluster.api.core.ZigBeeClusterException;
import org.bubblecloud.zigbee.proxy.cluster.api.general.scenes.AddSceneResponse;
import org.bubblecloud.zigbee.proxy.cluster.impl.core.DefaultDeserializer;
import org.bubblecloud.zigbee.proxy.cluster.impl.core.ResponseImpl;

/**
 * @author <a href="mailto:stefano.lenzi@isti.cnr.it">Stefano "Kismet" Lenzi</a>
 * @author <a href="mailto:francesco.furfari@isti.cnr.it">Francesco Furfari</a>
 * @version $LastChangedRevision: 799 $ ($LastChangedDate: 2013-08-06 19:00:05 +0300 (Tue, 06 Aug 2013) $)
 */
public class AddSceneResponseImpl extends ResponseImpl implements AddSceneResponse {

    private byte status;
    private int groupId;
    private short sceneId;

    public AddSceneResponseImpl(Response response) throws ZigBeeClusterException {
        super(response);
        ResponseImpl.checkSpecificCommandFrame(response, AddSceneResponse.ID);
        ZBDeserializer deserializer = new DefaultDeserializer(getPayload(), 0);
        status = deserializer.read_byte();
        groupId = deserializer.read_short();
        sceneId = deserializer.read_byte();
    }

    public int getGroupId() {
        return groupId;
    }

    public short getSceneId() {
        return sceneId;
    }

    public Status getStatus() {
        return Status.getStatus(status);
    }

}
