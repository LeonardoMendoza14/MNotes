package com.mendoxy.mnotes.domain.model

import com.mendoxy.mnotes.ui.utils.AppFontSize
import com.mendoxy.mnotes.ui.utils.AppTheme
import com.mendoxy.mnotes.ui.utils.SortOrder

data class SettingsModel(
    val userId: String = "",
    val theme: AppTheme = AppTheme.DARK,
    val fontSize: AppFontSize = AppFontSize.MIDDLE,
    val order: SortOrder = SortOrder.DESCENDING,
    val lastEdit: Long = System.currentTimeMillis(),
    val isSync: Boolean = false
)
