package com.example.voodoosdk.ui.composables

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.drawable.CrossfadeDrawable
import coil.request.ImageRequest
import com.amarchaud.voodoosdk.R

@Composable
fun ImageLoaderSubCompose(
    modifier: Modifier = Modifier,
    data : Any?,
    contentScale: ContentScale = ContentScale.FillHeight,
    contentDescription: String? = null,
    crossFadeDuration: Int = CrossfadeDrawable.DEFAULT_DURATION,
    loading: @Composable (BoxScope.() -> Unit)? = null,
    failure: @Composable (BoxScope.() -> Unit)? = null,
    success: @Composable (BoxScope.(Drawable) -> Unit)? = null,
    onSuccess: (AsyncImagePainter.State.Success) -> Unit = {},
    onError: (AsyncImagePainter.State.Error) -> Unit = {},
) {
    if (LocalInspectionMode.current) { // preview
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_error_24dp),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
        )
    } else {
        val context = LocalContext.current
        SubcomposeAsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(context)
                .data(data)
                .crossfade(crossFadeDuration)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            // We use our own signature, that's why we get an additional lambda
            // We don't want to create a dependence with a specific library scope
            loading = loading?.run {
                { loading() }
            },
            error = failure?.run {
                { failure() }
            },
            success = success?.run {
                { successState -> success(successState.result.drawable) }
            },
            onSuccess = onSuccess,
            onError = onError,
        )
    }
}

@Composable
@Preview
private fun ImageLoaderPreview() {
    ImageLoaderSubCompose(data = "") {}
}