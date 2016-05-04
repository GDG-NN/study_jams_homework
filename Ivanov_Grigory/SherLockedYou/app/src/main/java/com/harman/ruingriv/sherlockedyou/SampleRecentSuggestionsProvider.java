package com.harman.ruingriv.sherlockedyou;


import android.content.SearchRecentSuggestionsProvider;

public class SampleRecentSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY =
            SampleRecentSuggestionsProvider.class.getName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public SampleRecentSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
