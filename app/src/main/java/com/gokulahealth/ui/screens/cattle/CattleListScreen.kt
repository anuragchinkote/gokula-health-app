package com.gokulahealth.ui.screens.cattle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gokulahealth.ui.components.CowProfileCard
import com.gokulahealth.ui.components.EmptyStateView
import com.gokulahealth.ui.components.PremiumTopBar
import com.gokulahealth.viewmodel.CattleListViewModel

@Composable
fun CattleListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddCow: () -> Unit,
    onNavigateToDetails: (Long) -> Unit,
    viewModel: CattleListViewModel = hiltViewModel()
) {
    val cattleList by viewModel.cattleList.collectAsState()

    Scaffold(
        topBar = {
            PremiumTopBar(
                title = "My Cattle",
                subtitle = "Manage your livestock",
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddCow,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add My Cattle")
            }
        }
    ) { padding ->
        if (cattleList.isEmpty()) {
            EmptyStateView(
                message = "No cattle found",
                subMessage = "Tap the + button to register your first cattle."
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cattleList) { cow ->
                    CowProfileCard(
                        name = cow.name,
                        tagId = cow.earTagId,
                        breed = cow.breed,
                        imageUri = cow.imageUri,
                        onClick = { onNavigateToDetails(cow.id) }
                    )
                }
            }
        }
    }
}
