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

package com.wajeez.features.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.wajeez.features.data.local.database.DataItemType
import com.wajeez.features.data.local.database.DataItemTypeDao
import com.wajeez.features.data.network.model.SubscriptionModel
import retrofit2.http.GET
import javax.inject.Inject

interface ApiService {

    @GET("Free")
    suspend fun getFreeItems(): List<SubscriptionModel>

    @GET("Premium")
    suspend fun getPremiumItems(): List<SubscriptionModel>
}

interface SubsciptionDataRepository {

    suspend fun getFree(): List<SubscriptionModel>
    suspend fun getPreimuim(): List<SubscriptionModel>
}

class SubsciptionNetworkRepository @Inject constructor(
    private val apiService: ApiService
) : SubsciptionDataRepository {
    override suspend fun getFree(): List<SubscriptionModel> {
       return apiService.getFreeItems()
    }

    override suspend fun getPreimuim() : List<SubscriptionModel>{
        return apiService.getPremiumItems()
    }
}
