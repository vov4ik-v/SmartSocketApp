package ai.learning.smartsocketapp.feature.home

import ai.learning.smartsocketapp.core.network.repository.SmartSocketRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: SmartSocketRepository
) : ViewModel() {

    private val _isRelayEnabled = MutableStateFlow<Boolean?>(null)
    val isRelayEnabled: StateFlow<Boolean?> = _isRelayEnabled.asStateFlow()

    init {
        fetchRelayState()
    }

    private fun fetchRelayState() {
        viewModelScope.launch {
            try {
                val relayState = repository.getRelay()
                _isRelayEnabled.value = relayState
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching relay state: ${e.message}")
            }
        }
    }

    fun toggleRelay() {
        viewModelScope.launch {
            try {
                val currentState = _isRelayEnabled.value
                currentState?.let {
                    repository.toggleRelay(!it)
                    _isRelayEnabled.value = !it
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error toggling relay: ${e.message}")
            }
        }
    }



}
