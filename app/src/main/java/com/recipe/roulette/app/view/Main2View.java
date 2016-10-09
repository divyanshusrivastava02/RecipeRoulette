package com.recipe.roulette.app.view;

import android.support.annotation.UiThread;

@UiThread
public interface Main2View {

    void setToolbar(String title);

    void showSnackbarNotification(String text, int duration);

    void normalizeToolbar();

    void setBackButton(boolean hasBackButton);
}