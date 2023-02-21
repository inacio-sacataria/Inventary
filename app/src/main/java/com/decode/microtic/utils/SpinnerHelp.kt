import android.R
import android.content.Context
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.ArrayRes



    fun Spinner.populate(context: Context?, item: MutableList<String>?) {
//        val list: MutableList<String?> = ArrayList()
//        list.add(item)
        populate2(context, item!!)
    }

    fun Spinner.populate2(context: Context?, spinnerItems: MutableList<String>?) {
        var adapter: ArrayAdapter<Any?>? =
            context?.let { ArrayAdapter<Any?>(it, R.layout.simple_spinner_item, spinnerItems as List<String>) }
        adapter!!.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }

    fun Spinner.populate(context: Context?, @ArrayRes textArrayResId: Int) {
        var adapter =
            ArrayAdapter.createFromResource(context!!, textArrayResId, R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }
