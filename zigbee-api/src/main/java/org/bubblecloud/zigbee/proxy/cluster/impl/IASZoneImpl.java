/*
   Copyright 2012-2013 CNR-ISTI, http://isti.cnr.it
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

package org.bubblecloud.zigbee.proxy.cluster.impl;

import org.bubblecloud.zigbee.network.ZigBeeDevice;
import org.bubblecloud.zigbee.proxy.cluster.glue.security_safety.IASZone;
import org.bubblecloud.zigbee.proxy.ZigBeeHAException;
import org.bubblecloud.zigbee.proxy.cluster.api.core.Attribute;
import org.bubblecloud.zigbee.proxy.cluster.api.core.Response;
import org.bubblecloud.zigbee.proxy.cluster.api.core.Subscription;
import org.bubblecloud.zigbee.proxy.cluster.api.core.ZigBeeClusterException;
import org.bubblecloud.zigbee.proxy.cluster.api.general.alarms.GetAlarmResponse;
import org.bubblecloud.zigbee.proxy.cluster.api.global.DefaultResponse;
import org.bubblecloud.zigbee.proxy.cluster.api.security_safety.ias_zone.ZoneEnrollRequestPayload;
import org.bubblecloud.zigbee.proxy.cluster.api.security_safety.ias_zone.ZoneEnrollResponse;
import org.bubblecloud.zigbee.proxy.cluster.api.security_safety.ias_zone.ZoneStatusChangeNotificationListener;
import org.bubblecloud.zigbee.proxy.cluster.api.security_safety.ias_zone.ZoneStatusChangeNotificationPayload;
import org.bubblecloud.zigbee.proxy.cluster.api.security_safety.ias_zone.ZoneStatusChangeNotificationResponse;
import org.bubblecloud.zigbee.proxy.cluster.impl.security_safety.IASZoneCluster;

/**
 * @author <a href="mailto:manlio.bacco@isti.cnr.it">Manlio Bacco</a>
 * @version $LastChangedRevision$ ($LastChangedDate$)
 * @since 0.7.0
 */
public class IASZoneImpl implements IASZone {

    private final IASZoneCluster cluster;

    private final Attribute zoneState;
    private final Attribute zoneType;
    private final Attribute zoneStatus;
    private final Attribute iasCIEaddress;

    public IASZoneImpl(ZigBeeDevice zbDevice) {

        cluster = new IASZoneCluster(zbDevice);
        zoneState = cluster.getAttributeZoneState();
        zoneType = cluster.getAttributeZoneType();
        zoneStatus = cluster.getAttributeZoneStatus();
        iasCIEaddress = cluster.getAttributeIASCIEAddress();
    }

    public int getId() {
        return cluster.getId();
    }

    public String getName() {
        return cluster.getName();
    }

    public Subscription[] getActiveSubscriptions() {
        return cluster.getActiveSubscriptions();
    }

    public Attribute[] getAttributes() {
        return cluster.getAvailableAttributes();
    }

    public Attribute getAttribute(int id) {

        Attribute[] attributes = cluster.getAvailableAttributes();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getId() == id)
                return attributes[i];
        }
        return null;
    }

    public Attribute getZoneState() {

        return zoneState;
    }

    public Attribute getZoneType() {

        return zoneType;
    }

    public Attribute getZoneStatus() {

        return zoneStatus;
    }

    public Attribute getIASCIEAddress() {

        return iasCIEaddress;
    }

    public ZoneEnrollResponse zoneEnrollRequest(ZoneEnrollRequestPayload payload) throws ZigBeeHAException {

        try {
            ZoneEnrollResponse response = (ZoneEnrollResponse) cluster.zoneEnrollRequest(payload);
            return response;
        } catch (ZigBeeClusterException e) {
            throw new ZigBeeHAException(e);
        }
    }

    public Response zoneStatusChangeNotification(ZoneStatusChangeNotificationPayload payload) throws ZigBeeHAException {
        try {
            Response response = cluster.zoneStatusChangeNotification(payload);
            if (response.getZCLHeader().getCommandId() != ZoneStatusChangeNotificationResponse.ID)
                throw new ZigBeeHAException(((DefaultResponse) response).getStatus().toString());

            return (GetAlarmResponse) response;
        } catch (ZigBeeClusterException e) {
            throw new ZigBeeHAException(e);
        }

        /*try {
            cluster.zoneStatusChangeNotification();
        }
        catch (ZigBeeClusterException e) {
            throw new ZigBeeHAException(e);
        }*/
    }

    public boolean addZoneStatusChangeNotificationListener(ZoneStatusChangeNotificationListener listener) {
        return cluster.addZoneStatusChangeNotificationListener(listener);
    }

    public boolean removeZoneStatusChangeNotificationListener(ZoneStatusChangeNotificationListener listener) {
        return cluster.removeZoneStatusChangeNotificationListener(listener);
    }
}