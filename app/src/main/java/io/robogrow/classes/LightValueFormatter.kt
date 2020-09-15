package io.robogrow.classes

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class LightValueFormatter : ValueFormatter() {
    private val format = DecimalFormat("##k")
    // override this for custom formatting of XAxis or YAxis labels
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return format.format(value)
    }
}