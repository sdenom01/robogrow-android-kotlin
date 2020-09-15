package io.robogrow.classes

import android.os.Build
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class TimeOfDayFormatter(private val xValsDateLabel: ArrayList<Date>) : ValueFormatter() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    private var hackyIndex = -1

    // override this for custom formatting of XAxis or YAxis labels
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        hackyIndex++

        return if (hackyIndex <= xValsDateLabel.size - 1) {
            formatter.format(
                xValsDateLabel[hackyIndex].toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            )
        } else {
            ("").toString()
        }
    }
}