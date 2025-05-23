package com.onedongua.abutton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onedongua.abutton.adapter.MessageAdapter;
import com.onedongua.abutton.databinding.ActivityChatBinding;
import com.onedongua.abutton.manager.ServerManager;
import com.onedongua.abutton.model.MessageData;
import com.onedongua.abutton.model.UserInfo;
import com.onedongua.abutton.util.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("NotifyDataSetChanged")
public class ChatActivity extends BaseActivity {
    private final static String TAG = "ChatActivity";
    private ActivityChatBinding binding;
    private MessageAdapter adapter;
    private final List<MessageData> messages = new ArrayList<>();
    private final OkHttpClient client = new OkHttpClient();
    private final ServerManager serverManager = ServerManager.getInstance();
    private long chatId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (actionBar != null) {
            actionBar.setTitle(R.string.chat);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        chatId = intent.getLongExtra("id", 0L);
        if (chatId == 0L) {
            print(R.string.unknown_error);
            finish();
            return;
        }

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(messages);
        binding.recyclerView.setAdapter(adapter);

        loadMessages();

        binding.btnSend.setOnClickListener(v -> {
            String msg = binding.etMessage.getText().toString().trim();
            UserInfo user = getUser();
            if (user == null) {
                print(R.string.unknown_error);
                return;
            }
            if (!msg.isEmpty()) {
                sendMessage(user.getId(), user.getName(), msg);
                binding.etMessage.setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private UserInfo getUser() {
        try {
            File userFile = new File(getFilesDir(), "user.json");
            if (!userFile.exists()) {
                throw new Exception("Not logged in");
            }
            return JsonUtils.fromJsonFile(userFile, UserInfo.class);
        } catch (Exception e) {
            Log.e(TAG, "loadMessages: ", e);
            return null;
        }
    }

    private void loadMessages() {
        Request request = new Request.Builder()
                .url(serverManager.getServer() + "api/chat/" + chatId)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "加载失败", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    List<MessageData> data = Arrays.asList(JsonUtils.fromJson(response.body().string(), MessageData[].class));
                    messages.clear();
                    messages.addAll(data);
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                }
            }
        });
    }

    private void sendMessage(String id, String user, String message) {
        try {
            RequestBody body = RequestBody.create(
                    JsonUtils.toJson(new MessageData(id, user, message, null)),
                    MediaType.parse("application/json")
            );
            Request request = new Request.Builder()
                    .url(serverManager.getServer() + "api/chat/" + chatId)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    loadMessages();
                }
            });
        } catch (JsonProcessingException e) {
            Log.e(TAG, "sendMessage: ", e);
        }
    }
}
