package com.mendoxy.mnotes.domain.model

import com.mendoxy.mnotes.data.remote.firebase.entity.SettingsEntity
import com.mendoxy.mnotes.ui.utils.AppFontSize
import com.mendoxy.mnotes.ui.utils.AppTheme
import com.mendoxy.mnotes.ui.utils.SortOrder

data class SettingsModel(
    val userId: String = "",
    val theme: AppTheme = AppTheme.DARK,
    val fontSize: AppFontSize = AppFontSize.MIDDLE,
    val order: SortOrder = SortOrder.DESCENDING
)

fun SettingsModel.toEntity(): SettingsEntity = SettingsEntity(
    userId = this.userId,
    theme = this.theme.name,
    fontsize = this.fontSize.name,
    order = this.order.name
)