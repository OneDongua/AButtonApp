package com.onedongua.abutton.manager;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServerManager {
    private static volatile ServerManager instance;
    private final OkHttpClient client;
    private final ArrayList<String> allServers = new ArrayList<>();
    private final ArrayList<String> availableServers = new ArrayList<>();

    private ServerManager() {
        client = new OkHttpClient();
        allServers.add("http://192.168.2.185:3001/");
        allServers.add("http://192.168.2.30:3001/");
        checkAvailable();
    }

    public static ServerManager getInstance() {
        if (instance == null) {
            synchronized (ServerManager.class) {
                if (instance == null) {
                    instance = new ServerManager();
                }
            }
        }
        return instance;
    }

    public void addServer(String server) {
        allServers.add(server);
        checkAvailable();
    }

    public String getServer() {
        if (!availableServers.isEmpty()) {
            return availableServers.get(0);
        } else {
            return allServers.get(0);
        }
    }

    private void checkAvailable() {
        for (String server : allServers) {
            Request request = new Request.Builder()
                    .url(server)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.isSuccessful()) {
                        availableServers.add(server);
                    }
                }
            });
        }
    }
}
