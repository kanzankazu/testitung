package com.kanzankazu.itungitungan.util.widget;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class SpannableText extends ClickableSpan {

    private boolean isUnderline = true;

    /**
     * Constructor
     */
    public SpannableText(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {

        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.RED);

    }

    @Override
    public void onClick(View widget) {

    }
}
