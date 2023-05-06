package com.android.beyikyol2.component.page.map

sealed class AutoCompleteUIAction {
    object OnDoneClick : AutoCompleteUIAction()
    object OnClearClick : AutoCompleteUIAction()

    data class OnQueryChange(val query: String) : AutoCompleteUIAction()
    data class OnItemSelected<T>(val selectedItem: T) : AutoCompleteUIAction()
}