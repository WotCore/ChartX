package wot.core.view.chartx

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import wot.core.view.chartx.axis.ChartAxis
import wot.core.view.chartx.axis.formatter.AxisLabelFormatter
import wot.core.view.chartx.axis.model.AxisLabel
import wot.core.view.chartx.axis.model.AxisLine
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide
import wot.core.view.chartx.model.ChartPanel
import wot.core.view.chartx.model.ChartViewport
import wot.core.view.chartx.renderer.LineDataRenderer

/**
 * 测试用示例控件
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class SampleChartView(context: Context, attrs: AttributeSet? = null) :
    BaseChartView(context, attrs) {

    override fun createChartPanels(viewport: ChartViewport): MutableList<ChartPanel> {
        return mutableListOf(
            ChartPanel(viewport).apply {
                addAxis(
                    ChartAxis(AxisSide.LEFT, AxisPosition.OUTSIDE, 60F).apply {
                        addLabels(
                            AxisLabel(0f, "10"),
                            AxisLabel(0.25F, "20"),
                            AxisLabel(0.5F, "30"),
                            AxisLabel(0.75F, "40"),
                            AxisLabel(1F, "50")
                        )

                        addLines(
                            AxisLine(0f),
                            AxisLine(0.25F),
                            AxisLine(0.5F),
                            AxisLine(0.75F),
                            AxisLine(1F)
                        )
                    },
                    ChartAxis(AxisSide.BOTTOM, AxisPosition.OUTSIDE, 60F).apply {
                        addLabels(
                            AxisLabel(0f),
                            AxisLabel(0.25F),
                            AxisLabel(0.5F),
                            AxisLabel(0.75F),
                            AxisLabel(1F)
                        )

                        addLines(
                            AxisLine(0f),
                            AxisLine(0.25F),
                            AxisLine(0.5F),
                            AxisLine(0.75F),
                            AxisLine(1F)
                        )

                        formatter = object : AxisLabelFormatter {

                            override fun format(
                                side: AxisSide, axisPosition: AxisPosition, value: Float
                            ): String {
                                return "$value"
                            }
                        }
                    },
//                    ChartAxis(AxisSide.LEFT, AxisPosition.INSIDE, 60F).apply {
//                        addLabels(
//                            AxisLabel(0f, "10"),
//                            AxisLabel(0.25F, "20"),
//                            AxisLabel(0.5F, "30"),
//                            AxisLabel(0.75F, "40"),
//                            AxisLabel(1F, "50")
//                        )
//                    },
//                    ChartAxis(AxisSide.TOP, AxisPosition.OUTSIDE, 60F).apply {
//                        addLabels(
//                            AxisLabel(0f, "10"),
//                            AxisLabel(0.25F, "20"),
//                            AxisLabel(0.5F, "30"),
//                            AxisLabel(0.75F, "40"),
//                            AxisLabel(1F, "50")
//                        )
//                    }, ChartAxis(AxisSide.TOP, AxisPosition.INSIDE, 60F).apply {
//                        addLabels(
//                            AxisLabel(0f, "10"),
//                            AxisLabel(0.25F, "20"),
//                            AxisLabel(0.5F, "30"),
//                            AxisLabel(0.75F, "40"),
//                            AxisLabel(1F, "50")
//                        )
//                    },
//                    ChartAxis(AxisSide.RIGHT, AxisPosition.OUTSIDE, 120F).apply {
//                        addLabels(
//                            AxisLabel(0f),
//                            AxisLabel(0.25F),
//                            AxisLabel(0.5F),
//                            AxisLabel(0.75F),
//                            AxisLabel(1F)
//                        )
//                        formatter = object : AxisLabelFormatter {
//
//                            override fun format(
//                                side: AxisSide, axisPosition: AxisPosition, value: Float
//                            ): String {
//                                return "$value"
//                            }
//                        }
//                    }, ChartAxis(AxisSide.RIGHT, AxisPosition.INSIDE, 120F).apply {
//                        addLabels(
//                            AxisLabel(0f),
//                            AxisLabel(0.25F),
//                            AxisLabel(0.5F),
//                            AxisLabel(0.75F),
//                            AxisLabel(1F)
//                        )
//                        formatter = object : AxisLabelFormatter {
//
//                            override fun format(
//                                side: AxisSide, axisPosition: AxisPosition, value: Float
//                            ): String {
//                                return "$value"
//                            }
//                        }
//                    },
//                    ChartAxis(AxisSide.BOTTOM, AxisPosition.INSIDE, 60F).apply {
//                        addLabels(
//                            AxisLabel(0f),
//                            AxisLabel(0.25F),
//                            AxisLabel(0.5F),
//                            AxisLabel(0.75F),
//                            AxisLabel(1F)
//                        )
//                        formatter = object : AxisLabelFormatter {
//
//                            override fun format(
//                                side: AxisSide, axisPosition: AxisPosition, value: Float
//                            ): String {
//                                return "$value"
//                            }
//                        }
//                    }
                )
                addDataRenderers(LineDataRenderer(), LineDataRenderer(Color.RED))
            }
        )
    }

    override fun updateChartPanelBounds(
        chartPanelList: MutableList<ChartPanel>, viewWidth: Int, viewHeight: Int
    ): Float {
        val panel = chartPanelList[0]
        panel.setBounds(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        return panel.getContentWidth()
    }
}