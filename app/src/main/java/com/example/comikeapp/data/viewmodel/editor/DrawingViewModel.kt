package com.example.comikeapp.data.viewmodel.editor

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.Serializable

class DrawingViewModel : ViewModel() {
    // MutableLiveDataは変更可能
    private val _paths = NonNullLiveData(
        mutableListOf<Pair<Path, PathStyle>>()
    )
    private val _pathStyle = NonNullLiveData(
        PathStyle()
    )

    private val _pathsSaveData = NonNullLiveData(
        mutableListOf<Pair<List<Offset>, PathStyle>>()
    )

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

    fun addPath(pair: Pair<Path, PathStyle>, points: List<Offset>) {
        val list = _paths.value
        list.add(pair)
        _paths.value = list

        _pathsSaveData.value.add(Pair(points, pair.second))
    }

    fun setIsZoomable(isZoomable: Boolean) {
        _isZoomable.value = isZoomable
    }

    // ViewModelを保存と読み取り機能
    fun getSaveData(): DrawingViewModelSaveData {
        val paths = _pathsSaveData.value.map { path ->

            val l = path.first.map {
                Pair(it.x, it.y)
            }

            SaveDataPaths(
                nodes = l,
                color = path.second.color.value.toLong(),
                alpha = path.second.alpha,
                width = path.second.width
            )
        }
        return DrawingViewModelSaveData(paths = paths)
    }

    fun restoreSaveData(saveData: DrawingViewModelSaveData) {
        saveData.paths.forEach { path ->
            val p = Path()
            for (node in path.nodes) {
                if(p.isEmpty){
                    p.moveTo(node.first, node.second)
                    continue
                }
                p.lineTo(node.first, node.second)
            }

            this.addPath(
                Pair(
                    p,
                    PathStyle(
                        Color(path.color.toULong()),
                        path.alpha,
                        path.width
                    )
                ),
                path.nodes.map { Offset(it.first, it.second) }
            )
        }
    }

    data class DrawingViewModelSaveData(
        val paths: List<SaveDataPaths>
    ): Serializable

    data class SaveDataPaths(
        val nodes: List<Pair<Float, Float>>,
        val color: Long,
        val alpha: Float,
        val width: Float
    ): Serializable
}