package com.scorpius_enterprises.apps.companion;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by rickm on 2/7/2017.
 */

public class CompanionIntentService extends IntentService
{
    public CompanionIntentService()
    {
        this("CompanionIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CompanionIntentService(String name)
    {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

    }
}
