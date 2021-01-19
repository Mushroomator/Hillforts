package de.tp.hillforts.views.search

import android.content.SearchRecentSuggestionsProvider

class HillfortSuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "de.tp.hillforts.views.search.HillfortSuggestionProvider"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
}
