package com.mendoxy.mnotes.domain.repository

import com.mendoxy.mnotes.domain.model.SettingsModel
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun updateSettingsForUser(userId: String, settings : SettingsModel)

    fun getSettingsByUser(userId: String): Flow<SettingsModel?>
}