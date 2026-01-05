package com.nig.gopaddi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nig.gopaddi.R

val Satoshi = FontFamily(
    Font(R.font.satoshi_regular, FontWeight.Normal),
    Font(R.font.satoshi_medium, FontWeight.Medium),
    Font(R.font.satoshi_bold, FontWeight.Bold),
    Font(R.font.satoshi_black, FontWeight.Black)
)

val Typography = Typography(
    bodyLarge = TextStyle(fontFamily = Satoshi),
    bodyMedium = TextStyle(fontFamily = Satoshi),
    bodySmall = TextStyle(fontFamily = Satoshi),
    titleLarge = TextStyle(fontFamily = Satoshi),
    titleMedium = TextStyle(fontFamily = Satoshi),
    titleSmall = TextStyle(fontFamily = Satoshi),
    labelLarge = TextStyle(fontFamily = Satoshi),
    labelMedium = TextStyle(fontFamily = Satoshi),
    labelSmall = TextStyle(fontFamily = Satoshi)
)

//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = Satoshi,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    ),
//
//    titleLarge = TextStyle(
//        fontFamily = Satoshi,
//        fontWeight = FontWeight.Normal,
//        fontSize = 22.sp,
//        lineHeight = 28.sp,
//        letterSpacing = 0.sp
//    ),
//    labelSmall = TextStyle(
//        fontFamily = Satoshi,
//        fontWeight = FontWeight.Medium,
//        fontSize = 11.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
//    )
//)