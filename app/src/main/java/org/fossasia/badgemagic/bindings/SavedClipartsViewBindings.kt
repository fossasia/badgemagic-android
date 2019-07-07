package org.fossasia.badgemagic.bindings

import android.graphics.Bitmap
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.fossasia.badgemagic.adapter.SavedClipartsAdapter
import org.fossasia.badgemagic.ui.custom.SquareImageView
import org.fossasia.badgemagic.util.ImageUtils

@BindingAdapter("setAdapter")
fun setSavedClipRecyclerAdapter(recyclerView: RecyclerView, adapter: SavedClipartsAdapter) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
}

@BindingAdapter("bindImage")
fun bindImage(imageView: SquareImageView, image: Bitmap) {
    imageView.setImageBitmap(ImageUtils.trim(image, 200))
}