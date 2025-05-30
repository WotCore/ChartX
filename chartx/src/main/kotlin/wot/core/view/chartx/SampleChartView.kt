package wot.core.view.chartx

import android.content.Context
import android.util.AttributeSet
import wot.core.view.chartx.axis.XAxis
import wot.core.view.chartx.axis.YAxis
import wot.core.view.chartx.axis.model.AxisLabel
import wot.core.view.chartx.axis.renderer.XAxisRenderer
import wot.core.view.chartx.axis.renderer.YAxisRenderer
import wot.core.view.chartx.model.ChartPanel

/**
 * 测试用示例控件
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class SampleChartView(context: Context, attrs: AttributeSet? = null) :
    BaseChartView(context, attrs) {

    override fun createChartPanel(): MutableList<ChartPanel> {
        return mutableListOf(
            ChartPanel(
                yAxis = YAxis(YAxisRenderer().apply {
                    addLabels(
                        AxisLabel(0f, "10"),
                        AxisLabel(0.25F, "20"),
                        AxisLabel(0.5F, "30"),
                        AxisLabel(0.75F, "40"),
                        AxisLabel(1F, "50")
                    )
                }, 50F),
                xAxis = XAxis(XAxisRenderer().apply {
                    addLabels(
                        AxisLabel(0f, "10"),
                        AxisLabel(0.25F, "20"),
                        AxisLabel(0.5F, "30"),
                        AxisLabel(0.75F, "40"),
                        AxisLabel(1F, "50")
                    )
                }, 50F),
                dataRenderer = SampleDataRenderer(),
            )
        )
    }

    override fun onSizeChanged(
        panelList: MutableList<ChartPanel>,
        viewWidth: Int,
        viewHeight: Int
    ) {
        panelList[0].setBounds(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
    }
}