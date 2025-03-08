package com.onedongua.abutton;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.onedongua.abutton.databinding.ActivitySettingBinding;
import com.onedongua.abutton.manager.ServerManager;

public class SettingActivity extends BaseActivity {
    private ActivitySettingBinding binding;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (actionBar != null) {
            actionBar.setTitle(R.string.setting);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
                                    print(R.string.success);
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
