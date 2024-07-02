package com.example.voodoosdk.ui

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.amarchaud.ui.composables.ShimmerAnimationItem
import com.amarchaud.voodoosdk.R
import com.example.voodoosdk.ui.composables.ImageLoaderSubCompose
import com.example.voodoosdk.ui.model.AdScreenViewState

@Composable
fun AdComposable(
    viewModel: AdScreenViewModelImpl = hiltViewModel(),
    onAdClose: () -> Unit
) {
    val viewState by viewModel.viewState.collectAsState()

    AdScreen(
        viewState = viewState,
        onAdDisplay = viewModel::onAdDisplay,
        onAdClick = viewModel::onAdClick,
        onAdClose = {
            viewModel.onAdClose()
            onAdClose() // inform parent for closing this screen manually
        }
    )
}

@Composable
private fun AdScreen(
    viewState: AdScreenViewState,
    onAdDisplay: () -> Unit,
    onAdClick: () -> Unit,
    onAdClose: () -> Unit,
) {
    BackHandler {
        if (viewState.canCloseAd) {
            onAdClose()
        }
    }

    LaunchedEffect(key1 = Unit) {
        // call only one time a startup
        onAdDisplay()
    }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        ImageLoaderSubCompose(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    // launch webView
                    val map = viewState.redirectionUrl
                    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(map))
                    context.startActivity(mapIntent)

                    onAdClick()
                },
            contentScale = ContentScale.Crop,
            data = viewState.adUrl,
            loading = {
                ShimmerAnimationItem()
            },
            failure = {
                Image(
                    painter = painterResource(id = R.drawable.ic_error_24dp),
                    contentDescription = "error loading"
                )
            }
        )

        Button(
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 16.dp)
                .alpha(
                    alpha = if (viewState.canCloseAd) {
                        1f
                    } else {
                        0.5f
                    }
                ),
            onClick = {
                if (viewState.canCloseAd) {
                    onAdClose()
                }
            }
        ) {
            if (viewState.timeBeforeClosing > 0) {
                Text(text = stringResource(id = R.string.close_add) + " " + viewState.timeBeforeClosing)
            } else {
                Text(text = stringResource(id = R.string.close_add))
            }
        }
    }
}

@Preview
@Composable
private fun AdScreenPreview() {
    AdScreen(
        viewState = AdScreenViewState(),
        onAdClose = {},
        onAdDisplay = {},
        onAdClick = {}
    )
}
