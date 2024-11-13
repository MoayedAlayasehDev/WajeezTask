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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SubscriptionScreen(
    modifier: Modifier = Modifier,
    viewModel: SubscriptionViewModel = hiltViewModel()
) {
    // Collect subscription data from the view model
    val freeSubscriptions by viewModel.freeSubscriptions.collectAsState()
    val premiumSubscriptions by viewModel.premiumSubscriptions.collectAsState()

    val state = rememberScrollState()

    LaunchedEffect(Unit) {
        state.animateScrollTo(100)
        viewModel.fetchFreeSubscriptions()
        viewModel.fetchPremiumSubscriptions()
    }

    // Prepare the feature comparison data
    val freeFeaturesMap = freeSubscriptions.associateBy { it.id }
    val premiumFeaturesMap = premiumSubscriptions.associateBy { it.id }

    // Get all unique feature IDs from both plans
    val allFeatureIds = freeFeaturesMap.keys.union(premiumFeaturesMap.keys).toList()

    // UI Layout
    Scaffold(topBar = {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Subscription Plans",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Headers for Free and Premium
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Free",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Premium",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(allFeatureIds) { featureId ->
                        val freeFeature = freeFeaturesMap[featureId]
                        if (freeFeature?.positiveText?.isNotEmpty() == true) {
                            Text(
                                text = freeFeature.positiveText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider()
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(allFeatureIds) { featureId ->
                        val premiumFeature = premiumFeaturesMap[featureId]
                        if (premiumFeature?.positiveText?.isNotEmpty() == true) {
                            Text(
                                text = premiumFeature.positiveText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SubscriptionScreenPreview() {
    SubscriptionScreen()
}
