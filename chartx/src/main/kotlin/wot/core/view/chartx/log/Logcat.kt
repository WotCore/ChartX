package wot.core.view.chartx.log

/**
 * 日志输出
 *
 * @author : yangsn
 * @date : 2025/6/16
 */
object Logcat {

    private const val TAG = "ChartX"

    fun i(msg: String) {
        android.util.Log.i(TAG, msg)
    }

    fun d(msg: String) {
        android.util.Log.d(TAG, msg)
    }
}