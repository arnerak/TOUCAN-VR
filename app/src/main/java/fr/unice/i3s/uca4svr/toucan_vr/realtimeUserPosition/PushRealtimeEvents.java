/*
 * Copyright 2017 Université Nice Sophia Antipolis (member of Université Côte d'Azur), CNRS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.unice.i3s.uca4svr.toucan_vr.realtimeUserPosition;


import android.os.AsyncTask;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRTransform;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class PushRealtimeEvents extends AsyncTask<RealtimeEvent, Integer, Boolean> {

    private GVRContext context;
    private PushResponse callback;
    private String serverIP;
    private final static String pushInfoPath = "/infos";


    public PushRealtimeEvents(GVRContext context, String serverIP, PushResponse callback) {
        this.context = context;
        this.serverIP = serverIP;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(RealtimeEvent... events) {
        for (RealtimeEvent event : events) {
            if (!push(event)) return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        callback.pushResponse(result);
    }

    private Boolean push(RealtimeEvent event) {
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(serverIP.split(":")[0]);
            int port = 9999;
            if (serverIP.split(":").length == 2)
                port = Integer.parseInt(serverIP.split(":")[1]);
            byte[] buf = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN).putFloat(-event.z).putFloat(event.y).putFloat(event.x).putFloat(event.w).array();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, port);
            udpSocket.send(packet);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public String getServerIP() {
        return serverIP;
    }
}
