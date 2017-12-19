package com.scorpius_enterprises.apps.companion;

import android.provider.BaseColumns;

/**
 * Created by rickm on 2/7/2017.
 */

public final class CompanionDataContract
{
    private CompanionDataContract()
    {
    }

    public static class PriorityTask implements BaseColumns
    {
        public static final String TABLE_NAME        = "entry";
        public static final String COLUMN_NAME       = "name";
        public static final String COLUMN_PRIORITY   = "priority";
        public static final String COLUMN_CREATED    = "created";
        public static final String COLUMN_LAST_FIRED = "fired";
    }
}
