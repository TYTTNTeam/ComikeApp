package com.example.comikeapp.data.viewmodel.editor

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.Serializable

class DrawingViewModel : ViewModel() {
    // MutableLiveDataは変更可能
    private val _paths = NonNullLiveData<MutableList<Pair<Path, PathStyle>>>(
        mutableListOf()
    )
    private val _pathStyle = NonNullLiveData(
        PathStyle()
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

    fun addPath(pair: Pair<Path, PathStyle>) {
        val list = _paths.value
        list.add(pair)
        _paths.value = list
    }

    fun setIsZoomable(isZoomable: Boolean) {
        _isZoomable.value = isZoomable
    }

    // ViewModelを保存と読み取り機能
    fun getSaveData(): DrawingViewModelSaveData {
        val paths = paths.value!!.map { path ->

            val l = emptyList<Pair<Float, Float>>()

            // TODO テスト用処理
            SaveDataPaths(
                nodes = l,
                color = path.second.color.value.toInt(),
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
                )
            )
        }
    }

    data class DrawingViewModelSaveData(
        val paths: List<SaveDataPaths>
    ): Serializable

    data class SaveDataPaths(
        val nodes: List<Pair<Float, Float>>,
        val color: Int,
        val alpha: Float,
        val width: Float
    ): Serializable
}