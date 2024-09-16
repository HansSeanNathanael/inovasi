package com.example.inovasi.view.menu.gallery

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri

fun getFileName(context: Context, uri: Uri): String {
    var fileName = ""
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        fileName = cursor.getString(nameIndex)
    }
    return fileName
}

@Composable
fun PhotoItem(bitmap : ImageBitmap, onClick : () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            onClick()
        }
    ) {
        Image(
            bitmap = bitmap,
            "",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}

@Composable
fun GalleryMenu(
    modifier : Modifier,
    loadFile : Boolean = false
) {
    val context = LocalContext.current

    var bitmapUris by remember { mutableStateOf<Map<Uri, Bitmap>>(HashMap()) }
    var fileType by remember { mutableStateOf<Map<Uri, String>>(HashMap()) }
    var bitmapUrisList by remember { mutableStateOf<List<Uri>>(ArrayList()) }

    if (!loadFile) {
        val filesDir = context.filesDir
        val files = filesDir.listFiles()

        files?.let {
            val newBitmapUris = HashMap<Uri, Bitmap>()
            val newBitmapUrisList = ArrayList<Uri>()
            val newFileType = HashMap<Uri, String>()

            for (oldBitmap in bitmapUris) {
                oldBitmap.value.recycle()
            }

            for (file in it) {
                try {
                    if (android.os.Build.VERSION.SDK_INT >= 29) {
                        var loaded = false

                        try {
                            val newBitmap = ThumbnailUtils.createImageThumbnail(file, Size(128, 128), null)

                            val uri = file.toUri()
                            newBitmapUrisList.add(uri)
                            newBitmapUris[uri] = newBitmap
                            newFileType[uri] = "image"
                            loaded = true
                        }
                        catch (_ : Exception) {}

                        if (!loaded) {
                            try {
                                val newBitmap = ThumbnailUtils.createVideoThumbnail(file, Size(128, 128), null)

                                val uri = file.toUri()
                                newBitmapUrisList.add(uri)
                                newBitmapUris[uri] = newBitmap
                                newFileType[uri] = "video"
                            }
                            catch (_ : Exception) {}
                        }
                    }
                }
                catch (e : Exception) {
                    e.printStackTrace()
                }
            }

            bitmapUris = newBitmapUris
            bitmapUrisList = newBitmapUrisList
            fileType = newFileType
        }
    }



    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier
            .padding(16.dp)
    ) {
        items(bitmapUrisList.size) { i ->
            val bitmapUri = bitmapUrisList[i]

            bitmapUris[bitmapUri]?.let {
                PhotoItem(it.asImageBitmap(), fun() {
                    val intent = Intent(context, ViewPhotoActivity::class.java)
                    intent.putExtra("uri", bitmapUri.toString())
                    intent.putExtra("type", fileType[bitmapUri])
                    context.startActivity(intent)
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryMenuPreview() {
    GalleryMenu(Modifier)
}