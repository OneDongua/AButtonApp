package com.onedongua.abutton.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.onedongua.abutton.R;

public class ToggleSwitchView extends LinearLayout {
    private TextView leftButton;
    private TextView middleButton;
    private TextView rightButton;
    private CardView leftCard;
    private CardView middleCard;
    private CardView rightCard;
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
        leftButton = findViewById(R.id.left_button);
        middleButton = findViewById(R.id.middle_button);
        rightButton = findViewById(R.id.right_button);
        leftCard = findViewById(R.id.left_card);
        middleCard = findViewById(R.id.middle_card);
        rightCard = findViewById(R.id.right_card);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToggleSwitchView);
        selectedColor = a.getColor(R.styleable.ToggleSwitchView_selectedColor, 0xffffffff);
        unselectedColor = a.getColor(R.styleable.ToggleSwitchView_unselectedColor, 0xffcccccc);
        a.recycle();

        leftCard.setCardBackgroundColor(selectedColor);
        leftCard.setCardElevation(32);
        middleCard.setCardBackgroundColor(unselectedColor);
        rightCard.setCardBackgroundColor(unselectedColor);

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

    private void selectButton(CardView card) {
        leftCard.setCardBackgroundColor(unselectedColor);
        middleCard.setCardBackgroundColor(unselectedColor);
        rightCard.setCardBackgroundColor(unselectedColor);
        leftCard.setCardElevation(0);
        middleCard.setCardElevation(0);
        rightCard.setCardElevation(0);
        card.setCardBackgroundColor(selectedColor);
        card.setCardElevation(32);
    }

    public void setOnToggleSwitchListener(OnToggleSwitchListener listener) {
        this.listener = listener;
    }

    public interface OnToggleSwitchListener {
        void onToggleSelected(int position);
    }
}