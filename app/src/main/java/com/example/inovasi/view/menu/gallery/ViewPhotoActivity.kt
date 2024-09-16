package com.example.inovasi.view.menu.gallery

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.inovasi.view.menu.gallery.ui.theme.InovasiTheme

class ViewPhotoActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentUriString = intent.getStringExtra("uri")
        val contentUri = if (contentUriString != null) {
            Uri.parse(contentUriString)
        } else {
            null
        }

        val fileType = intent.getStringExtra("type")

        if (fileType == null) {
            finish()
        }

        val exoPlayer = ExoPlayer.Builder(this).build()

        enableEdgeToEdge()
        setContent {
            InovasiTheme {

                LaunchedEffect(contentUri) {
                    contentUri?.let {
                        try {
                            val mediaItemBuilder = MediaItem.Builder().setUri(contentUri)
                            val mediaItem = if (fileType == "image") {
                                mediaItemBuilder.setImageDurationMs(10000).build()
                            } else {
                                mediaItemBuilder.build()
                            }

                            exoPlayer.setMediaItem(mediaItem)
                            exoPlayer.prepare()
                        }
                        catch (_ : Exception) {
                            finish()
                        }
                    }
                }
                DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
                    }
                }

                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            player = exoPlayer
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}