package com.mendoxy.mnotes.ui.presentation.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mendoxy.mnotes.ui.theme.MNotesTheme
import com.mendoxy.mnotes.ui.theme.dimenMiddle

@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    label: String = "Label",
    fieldModifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    placeholder: String = "Placeholder",
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    isPassword: Boolean = false,
    showPassword: Boolean = true

){

    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        DefaultText(
            text = label,
            modifier = modifier,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(dimenMiddle))

        TextField(
            value = value,
            onValueChange = {onValueChange(it)},
            modifier = fieldModifier
                .fillMaxWidth()
                .height(56.dp)
                .border(width = 1.dp, color =if(!isFocused) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                .align(Alignment.Start)
                .onFocusChanged{focusState ->
                    isFocused = focusState.isFocused
                }
            ,
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                DefaultText(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            leadingIcon = leadingIcon?.let { leadingIcon },
            trailingIcon = trailingIcon?.let {trailingIcon },
            isError = isError,
            visualTransformation = if(!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if(isPassword) KeyboardType.Password else KeyboardType.Email),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent

            )
        )

    }
}

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    label: String = "Label",
    fieldModifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    placeholder: String = "Placeholder",
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
){

    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = Modifier.background(backgroundColor)) {
        DefaultText(
            text = label,
            modifier = modifier,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(dimenMiddle))

        TextField(
            value = value,
            onValueChange = {onValueChange(it)},
            modifier = fieldModifier
                .fillMaxWidth()
                .height(56.dp)
                .border(width = 1.dp, color =if(!isFocused) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                .align(Alignment.Start)
                .onFocusChanged{focusState ->
                    isFocused = focusState.isFocused
                }
            ,
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                DefaultText(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            leadingIcon = leadingIcon?.let { leadingIcon },
            trailingIcon = trailingIcon?.let {trailingIcon },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent

            )
        )

    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    MNotesTheme {
        DefaultTextField()
    }
}