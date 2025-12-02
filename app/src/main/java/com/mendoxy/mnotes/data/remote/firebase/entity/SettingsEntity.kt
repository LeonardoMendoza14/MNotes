package com.mendoxy.mnotes.data.remote.firebase.entity

import com.mendoxy.mnotes.domain.model.SettingsModel
import com.mendoxy.mnotes.ui.utils.AppFontSize
import com.mendoxy.mnotes.ui.utils.AppTheme
import com.mendoxy.mnotes.ui.utils.SortOrder

data class SettingsEntity(
    val userId: String = "",
    val theme: String = AppTheme.DARK.name,
    val fontsize: String = AppFontSize.MIDDLE.name,
    val order: String = SortOrder.DESCENDING.name
)

fun SettingsEntity.toModel() : SettingsModel = SettingsModel(
    userId = this.userId,
    theme = try{
        AppTheme.valueOf(this.theme)
    }catch (e: IllegalArgumentException){
        AppTheme.DARK
    },
    fontSize = try{
        AppFontSize.valueOf(this.fontsize)
    }catch (e: IllegalArgumentException) {
        AppFontSize.MIDDLE
    },
    order = try{
        SortOrder.valueOf(this.order)
    }catch (e: IllegalArgumentException){
        SortOrder.DESCENDING
    }
)
