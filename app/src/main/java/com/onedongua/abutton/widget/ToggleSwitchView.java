package com.onedongua.abutton.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.onedongua.abutton.R;

public class ToggleSwitchView extends LinearLayout {
    private LinearLayout leftCard;
    private LinearLayout middleCard;
    private LinearLayout rightCard;
    private int selectedColor;
    private int unselectedColor;
    private OnToggleSwitchListener listener;

    public ToggleSwitchView(Context context) {
        super(context);
        init(context, null);
    }

    public ToggleSwitchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ToggleSwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.toggle_switch, this, true);
        leftCard = findViewById(R.id.left_card);
        middleCard = findViewById(R.id.middle_card);
        rightCard = findViewById(R.id.right_card);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToggleSwitchView);
        selectedColor = a.getColor(R.styleable.ToggleSwitchView_selectedColor, 0xffffffff);
        unselectedColor = a.getColor(R.styleable.ToggleSwitchView_unselectedColor, 0xffcccccc);
        a.recycle();

        leftCard.setBackgroundColor(selectedColor);
        leftCard.setElevation(32);
        middleCard.setBackgroundColor(unselectedColor);
        rightCard.setBackgroundColor(unselectedColor);

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        leftCard.setOnClickListener(v -> {
            selectButton(leftCard);
            if (listener != null) listener.onToggleSelected(0);
        });
        middleCard.setOnClickListener(v -> {
            selectButton(middleCard);
            if (listener != null) listener.onToggleSelected(1);
        });
        rightCard.setOnClickListener(v -> {
            selectButton(rightCard);
            if (listener != null) listener.onToggleSelected(2);
        });
    }

    private void selectButton(LinearLayout card) {
        leftCard.setBackgroundColor(unselectedColor);
        middleCard.setBackgroundColor(unselectedColor);
        rightCard.setBackgroundColor(unselectedColor);
        leftCard.setElevation(0);
        middleCard.setElevation(0);
        rightCard.setElevation(0);
        card.setBackgroundColor(selectedColor);
        card.setElevation(32);
    }

    public void setOnToggleSwitchListener(OnToggleSwitchListener listener) {
        this.listener = listener;
    }

    public interface OnToggleSwitchListener {
        void onToggleSelected(int position);
    }
}