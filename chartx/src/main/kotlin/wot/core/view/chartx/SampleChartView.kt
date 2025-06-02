package wot.core.view.chartx

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import wot.core.view.chartx.axis.XAxis
import wot.core.view.chartx.axis.YAxis
import wot.core.view.chartx.axis.model.AxisLabel
import wot.core.view.chartx.axis.renderer.XAxisRenderer
import wot.core.view.chartx.axis.renderer.YAxisRenderer
import wot.core.view.chartx.model.Panel
import wot.core.view.chartx.model.Viewport
import wot.core.view.chartx.renderer.LineDataRenderer

/**
 * 测试用示例控件
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class SampleChartView(context: Context, attrs: AttributeSet? = null) :
    BaseChartView(context, attrs) {

    override fun createPanels(viewport: Viewport): MutableList<Panel> {
        return mutableListOf(
            Panel(viewport).apply {
                addRenderers(LineDataRenderer(), LineDataRenderer(Color.RED))
                yAxis = YAxis(YAxisRenderer().apply {
                    addLabels(
                        AxisLabel(0f, "10"),
                        AxisLabel(0.25F, "20"),
                        AxisLabel(0.5F, "30"),
                        AxisLabel(0.75F, "40"),
                        AxisLabel(1F, "50")
                    )
                })
                xAxis = XAxis(XAxisRenderer().apply {
                    addLabels(
                        AxisLabel(0f, "10"),
                        AxisLabel(0.25F, "20"),
                        AxisLabel(0.5F, "30"),
                        AxisLabel(0.75F, "40"),
                        AxisLabel(1F, "50")
                    )
                })
            }
        )
    }

    override fun updatePanelBounds(
        panelList: MutableList<Panel>,
        viewWidth: Int,
        viewHeight: Int
    ) {
        panelList[0].setBounds(
            0f,
            0f,
            viewWidth.toFloat(),
            viewHeight.toFloat(),
            paddingBottom.toFloat(),
            paddingLeft.toFloat()
        )
    }
}