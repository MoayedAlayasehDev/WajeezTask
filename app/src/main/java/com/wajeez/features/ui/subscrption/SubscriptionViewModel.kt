/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wajeez.features.ui.subscrption

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wajeez.features.data.SubsciptionNetworkRepository
import com.wajeez.features.data.network.model.SubscriptionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val repository: SubsciptionNetworkRepository
) : ViewModel() {
    private val _freeSubscriptions = MutableStateFlow<List<SubscriptionModel>>(emptyList())
    val freeSubscriptions: StateFlow<List<SubscriptionModel>> = _freeSubscriptions

    private val _premiumSubscriptions = MutableStateFlow<List<SubscriptionModel>>(emptyList())
    val premiumSubscriptions: StateFlow<List<SubscriptionModel>> = _premiumSubscriptions

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchFreeSubscriptions() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _freeSubscriptions.value = repository.getFree()
            } catch (e: Exception) {
                _error.value = "Failed to load free subscriptions"
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchPremiumSubscriptions() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _premiumSubscriptions.value = repository.getPreimuim()
            } catch (e: Exception) {
                _error.value = "Failed to load premium subscriptions"
            } finally {
                _loading.value = false
            }
        }
    }
}

sealed interface DataItemTypeUiState {
    object Loading : DataItemTypeUiState
    data class Error(val throwable: Throwable) : DataItemTypeUiState
    data class Success(val data: List<String>) : DataItemTypeUiState
}
