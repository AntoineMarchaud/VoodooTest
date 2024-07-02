package com.amarchaud.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amarchaud.VoodooTest.R
import com.amarchaud.ui.theme.VoodooTestTheme
import com.example.voodoosdk.VoodooSdkState

@Composable
fun MainComposable(
    viewModel: MainComposableViewModelImpl = hiltViewModel(),
    onDisplayAd: () -> Unit,
) {
    val sdkState by viewModel.sdkState.collectAsState()


    MainComposableScreen(
        sdkState = sdkState,
        onLoad = {
            viewModel.onLoad()
        },
        onShow = onDisplayAd
    )
}

@Composable
private fun MainComposableScreen(
    sdkState: VoodooSdkState,
    onLoad: () -> Unit,
    onShow: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .fillMaxWidth(fraction = 0.5f),
            painter = painterResource(id = R.drawable.voodoo_logo), contentDescription = "logo"
        )

        Button(
            modifier = Modifier
                .align(alignment = Alignment.BottomStart)
                .padding(bottom = 16.dp, start = 16.dp)
                .defaultMinSize(minWidth = 128.dp)
                .navigationBarsPadding(),
            onClick = { onLoad() }) {
            Text(text = stringResource(id = R.string.loading))
        }

        Button(
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .defaultMinSize(minWidth = 128.dp)
                .navigationBarsPadding()
                .alpha(
                    alpha = if (sdkState.canDisplayAd) {
                        1.0f
                    } else {
                        0.5f
                    }
                ),
            onClick = {
                if (sdkState.canDisplayAd) {
                    onShow()
                }
            }) {
            Text(text = stringResource(id = R.string.show))
        }
    }
}

@Preview
@Composable
private fun MainComposablePreview() {
    VoodooTestTheme {
        MainComposableScreen(
            sdkState = VoodooSdkState(),
            onLoad = {},
            onShow = {}
        )
    }
}