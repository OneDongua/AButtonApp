package com.onedongua.abutton;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;

import com.onedongua.abutton.databinding.ActivitySettingBinding;
import com.onedongua.abutton.manager.ServerManager;

public class SettingActivity extends BaseActivity {
    private ActivitySettingBinding binding;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.serverItem.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                .setTitle(R.string.server_setting)
                .setView(R.layout.dialog_setting_server)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    EditText editText = ((AlertDialog) dialog).findViewById(R.id.edit_text);
                    if (editText != null) {
                        String text = editText.getText().toString();
                        if (!text.isEmpty()) {
                            ServerManager.getInstance().addServer(text);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create());

    }
}
