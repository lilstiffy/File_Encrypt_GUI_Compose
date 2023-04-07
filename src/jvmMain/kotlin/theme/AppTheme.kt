package theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

object AppTheme {
    @OptIn(ExperimentalUnitApi::class)
    fun titleStyle(): TextStyle {
        return TextStyle(
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = TextUnit(22f, TextUnitType.Sp),
            textDecoration = TextDecoration.Underline
        )
    }

    @OptIn(ExperimentalUnitApi::class)
    fun subTitleStyle(): TextStyle {
        return TextStyle(
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = TextUnit(18f, TextUnitType.Sp),
            textDecoration = TextDecoration.Underline
        )
    }

    @OptIn(ExperimentalUnitApi::class)
    fun bodyStyle(): TextStyle {
        return TextStyle(
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = TextUnit(14f, TextUnitType.Sp)
        )
    }

    @Composable
    fun Background(color: Color, content: @Composable () -> Unit) {
        Box(modifier = Modifier.background(color)) { content() }
    }

    @Composable
    fun DropdownSelector(
        values: List<Int>,
        suffix: String = "",
        onValueSelected: (Int) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedValue by remember { mutableStateOf(values.last()) }

        Background(color = Color.White) {
            Box(modifier = Modifier.clickable(onClick = { expanded = true })) {
                Text("$selectedValue $suffix", modifier = Modifier.padding(16.dp))

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(200.dp)
                ) {
                    values.forEach { value ->
                        DropdownMenuItem(onClick = {
                            selectedValue = value
                            onValueSelected(value)
                            expanded = false
                        }) {
                            Text(text = "$value $suffix")
                        }
                    }
                }
            }
        }
    }
}