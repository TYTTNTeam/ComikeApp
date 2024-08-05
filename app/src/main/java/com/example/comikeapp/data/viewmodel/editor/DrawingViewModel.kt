package com.example.comikeapp.data.viewmodel.editor

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comikeapp.data.editorrendering.PathStyle
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
    private val _canvasSizePx = MutableLiveData(Offset.Zero)

    private val _isZoomable = MutableLiveData(false)

    // LiveDataを外部で変更できないように設定
    // getterを使用してデータを読み取るプロセスのみ実行可能
    val paths: LiveData<MutableList<Pair<Path, PathStyle>>>
        get() = _paths
    val pathStyle: LiveData<PathStyle>
        get() = _pathStyle

    val canvasSizePx: LiveData<Offset>
        get() = _canvasSizePx

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

    fun addPath(pair: Pair<List<Offset>, PathStyle>) {
        val p = Path()
        for (offset in pair.first) {
            if (p.isEmpty) {
                p.moveTo(offset.x, offset.y)
                continue
            }
            p.lineTo(offset.x, offset.y)
        }

        val list = _paths.value
        list.add(Pair(p, pair.second))
        _paths.value = list

        _pathsSaveData.value.add(pair)
    }

    fun erasePath(erasePosition: Pair<Offset, Offset>) {
        fun doLinesIntersect(a1: Offset, a2: Offset, b1: Offset, b2: Offset): Boolean {
            // 線分a1-a2のベクトル
            val a1a2 = Offset(a2.x - a1.x, a2.y - a1.y)
            // 線分b1-b2のベクトル
            val b1b2 = Offset(b2.x - b1.x, b2.y - b1.y)

            // ベクトルのクロスプロダクト
            val denominator = a1a2.x * b1b2.y - a1a2.y * b1b2.x

            // 線分が平行な場合
            if (denominator == 0.0f) {
                return false
            }

            // 線分a1-a2に対する線分b1-b2の位置を計算
            val a1b1 = Offset(b1.x - a1.x, b1.y - a1.y)
            val t = (a1b1.x * b1b2.y - a1b1.y * b1b2.x) / denominator
            val u = (a1b1.x * a1a2.y - a1b1.y * a1a2.x) / denominator

            // 線分が交差している場合、tとuが[0,1]の範囲にある必要がある
            return t in 0.0f..1.0f && u in 0.0f..1.0f
        }

        var toDeletePathsIndex: Int? = null
        _pathsSaveData.value.forEachIndexed { pathsIndex, path ->
            for (lineIndex in 0..path.first.size) {
                if (path.first[lineIndex] == path.first.last()) break
                if (
                    doLinesIntersect(
                        erasePosition.first,
                        erasePosition.second,
                        path.first[lineIndex],
                        path.first[lineIndex + 1]
                    )
                ) {
                    toDeletePathsIndex = pathsIndex
                    break
                }
            }
        }

        toDeletePathsIndex?.let {
            val c = _paths.value.toMutableList()
            val s = _pathsSaveData.value.toMutableList()
            c.removeAt(toDeletePathsIndex!!)
            s.removeAt(toDeletePathsIndex!!)
            _paths.value = c
            _pathsSaveData.value = s
        }
    }

    fun setCanvasSizePx(canvasSizePx: Offset) {
        _canvasSizePx.value = canvasSizePx
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
            this.addPath(
                Pair(
                    path.nodes.map { Offset(it.first, it.second) },
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
    ) : Serializable

    data class SaveDataPaths(
        val nodes: List<Pair<Float, Float>>,
        val color: Long,
        val alpha: Float,
        val width: Float
    ) : Serializable
}