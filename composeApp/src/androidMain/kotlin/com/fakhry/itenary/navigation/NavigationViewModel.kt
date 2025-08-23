package com.fakhry.itenary.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.itenary.shared.util.DispatcherProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val dispatcher: DispatcherProvider,
) : ViewModel() {

    private val _itineraryRequestState = MutableSharedFlow<ItineraryRequestUiModel>()
    val itineraryRequestState = _itineraryRequestState.asSharedFlow()

    fun setItineraryRequest(
        request: ItineraryRequestUiModel,
    ) = viewModelScope.launch(dispatcher.main) {
        _itineraryRequestState.emit(request)
    }
}