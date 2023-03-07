package com.decode.microtic


import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.decode.microtic.data.models.Devices
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

public fun Context.showInfo(text: String){
    Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
}

fun timePickerdialog(editText: TextView, context: Context){
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(context,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            editText.setText("" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year)
        }, year, month, day)
    dpd.show()
}

fun Context.compareDates (date : String, date2 : String) : Boolean{
    val sdf = SimpleDateFormat("dd/MM/yyyy")

    val firstDate : Date = sdf.parse(date)
    val secondDate: Date = sdf.parse(date2)

    val cmp = firstDate.compareTo(secondDate)

    if (cmp > 0){
        Log.d("datetest", "Test Failed - Data 2: ${date2} inferior a data 1: ${date} nao valido")
        Toast.makeText(this,"Data final da alocacao nao deve ser superior a data alocada", Toast.LENGTH_SHORT).show()
        return false
    }

    if (cmp<0)
    {
        Log.d("datetest", "Test Passed - Data 2:${date2} superior a data1: ${date} isto é valido")
        return true
    }

    Log.d("datetest", "Test Failed - Data 2 :${date2} igual a Data1 : ${date} nao valido")
    Toast.makeText(this,"Data final da alocacao nao deve ser igual a data alocada", Toast.LENGTH_SHORT).show()

    return false
}


fun Context.compareProdutDates (date : String, date2 : String) : Boolean{
    val sdf = SimpleDateFormat("dd/MM/yyyy")

    var string = date2.split("-")
    val secondDateString = string[2]+"/"+ string[1]+"/"+ string[0]

    val firstDate : Date = sdf.parse(date)
    val secondDate: Date = sdf.parse(secondDateString)

    val cmp = firstDate.compareTo(secondDate)

    if (cmp > 0){
        Log.d("datetest", "Test Failed - Data 2: ${date2} inferior a data 1: ${date} nao valido")
        Toast.makeText(this,"Data de aquisicao nao deve ser superior a data de garantia", Toast.LENGTH_SHORT).show()
        return false
    }

    if (cmp<0)
    {
        Log.d("datetest", "Test Passed - Data 2:${date2} superior a data1: ${date} isto é valido")
        return true
    }

    Log.d("datetest", "Test Failed - Data 2 :${date2} igual a Data1 : ${date} nao valido")
    Toast.makeText(this,"Data de Aquisicao  nao deve ser igual a Data de Garantia", Toast.LENGTH_SHORT).show()

    return false
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.calculateEndDate(date: String, days: Int):String{
    var string = date.split("/")
    var dayDate = if (string[0].length==1) "0"+string[0] else string[0]
    var monthDate = if (string[1].length==1) "0"+string[1] else string[1]
    val startDateString = string[2]+"-"+monthDate+"-"+dayDate
    val numberOfDays = days
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    val startDate = LocalDate.parse(startDateString, formatter)
    val endDate = startDate.plusDays(numberOfDays.toLong())
    val endDateString = endDate.format(formatter)
   Log.d("DateCalculation", "end Date: ${endDateString}")
   Log.d("DateCalculation", "end Days: ${days}")

    return endDateString
}

var deviceLive = MutableLiveData<Devices>()
var deviceClicked : Devices? = null
var IDCLICK : String? = null

