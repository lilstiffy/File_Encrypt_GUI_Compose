package theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

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
}