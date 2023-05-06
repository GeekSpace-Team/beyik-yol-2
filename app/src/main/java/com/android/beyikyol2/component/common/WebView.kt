package com.android.beyikyol2.component.common

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.google.accompanist.web.rememberWebViewStateWithHTMLData

@Composable
fun WebViewComponent(htmlPage: String, color: Color) {
    val html = "<DOCTYPE html>" +
            "<head>" +
            "<style>" +
            ".main: {" +
            " background-color: rgba(${color.red},${color.green},${color.blue},${color.alpha});" +
            " padding:16px;" +
            "}" +
            "</style>" +
            "" +
            "</head>" +
            "<body class='main'>" +
            htmlPage +
            "</body>" +
            "</html>"
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color)) {
        val state = rememberWebViewStateWithHTMLData(html)

        WebView(
            state,
            onCreated = { it.settings.javaScriptEnabled = true },
            captureBackPresses = false
        )
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PageBodyWithWebView(
    htmlPage: String,
    onWebpageScroll: (x: Int, y: Int) -> Unit,
    modifier: Modifier = Modifier,
    color: Color
) {
    val html = "<DOCTYPE html>" +
            "<head>" +
            "<style>" +
            ".main: {" +
            " background-color: rgba(${color.red},${color.green},${color.blue},${color.alpha});" +
            " padding:16px;" +
            "}" +
            "</style>" +
            "" +
            "</head>" +
            "<body class='main'>" +
            htmlPage +
            "</body>" +
            "</html>"
    val context = LocalContext.current
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        factory = {
            MyWebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                onScrollListener = onWebpageScroll
                loadData(htmlPage,"text/html","UTF-8")
            }
        })
}

class MyWebView(context: Context) : WebView(context) {
    var onScrollListener: ((x: Int, y: Int) -> Unit)? = null

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollListener?.invoke(l, t)
    }
}