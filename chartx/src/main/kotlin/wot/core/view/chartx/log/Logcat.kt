package wot.core.view.chartx.log

import android.util.Log

/**
 * 日志输出
 *
 * @author : yangsn
 * @date : 2025/6/16
 */
object Logcat {

    private const val TAG = "ChartX"

    fun i(msg: String) {
        Log.i(TAG, msg)
    }

    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    fun w(msg: String) {
        Log.w(TAG, msg)
    }
}