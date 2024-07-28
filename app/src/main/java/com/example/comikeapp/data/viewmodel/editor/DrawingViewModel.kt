package com.example.comikeapp.data.viewmodel.editor

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawingViewModel: ViewModel() {
    // MutableLiveDataは変更可能
    private val _paths = NonNullLiveData<MutableList<Pair<Path, PathStyle>>>(
        mutableListOf()
    )
    private val _pathStyle = NonNullLiveData(
        PathStyle()
    )

    private val removedPaths = mutableListOf<Pair<Path, PathStyle>>()

    private val _background = MutableLiveData<Bitmap?>(null)
    private val _isZoomable = MutableLiveData(false)
    // LiveDataを外部で変更できないように設定
    // getterを使用してデータを読み取るプロセスのみ実行可能
    val paths: LiveData<MutableList<Pair<Path, PathStyle>>>
        get() = _paths
    val pathStyle: LiveData<PathStyle>
        get() = _pathStyle

    val background: LiveData<Bitmap?>
        get() = _background
    val isZoomable: LiveData<Boolean>
        get() = _isZoomable
    fun updateWidth(width: Float) {
        val style = _pathStyle.value
        style.width = width

        _pathStyle.value = style
    }

    fun updateColor(color: Color) {
        val style = _pathStyle.value
        style.color = color

        _pathStyle.value = style
    }

    fun updateAlpha(alpha: Float) {
        val style = _pathStyle.value
        style.alpha = alpha

        _pathStyle.value = style
    }

    fun addPath(pair: Pair<Path, PathStyle>) {
        val list = _paths.value
        list.add(pair)
        _paths.value = list
    }

    fun undoPath() {
        val pathList = _paths.value
        if (pathList.isEmpty())
            return
        val last = pathList.last()
        val size = pathList.size

        removedPaths.add(last)
        _paths.value = pathList.subList(0, size-1)
    }

    fun redoPath() {
        if (removedPaths.isEmpty())
            return
        _paths.value = (_paths.value + removedPaths.removeLast()) as MutableList<Pair<Path, PathStyle>>
    }

    fun setBackground(bitmap: Bitmap?) {
        _background.value = bitmap
    }

    fun setIsZoomable(isZoomable: Boolean) {
        _isZoomable.value = isZoomable
    }
}