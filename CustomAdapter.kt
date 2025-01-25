
package com.example.mydictionary
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mydictionary.R
import com.example.mydictionary.models.DictionaryItem

class CustomAdapter(private val context: Context, private val originalData: List<DictionaryItem>) : BaseAdapter() {
    private var filteredData: List<DictionaryItem> = originalData

    override fun getCount(): Int {
        return filteredData.size
    }

    override fun getItem(position: Int): Any {
        return filteredData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_item_layout, null)

            viewHolder = ViewHolder()
            viewHolder.wordTextView = view.findViewById(R.id.wordTextViewitem)
         /*   viewHolder.meaningTextView = view.findViewById(R.id.meaningTextViewitem2)
            viewHolder.categoryTextView = view.findViewById(R.id.categoryTextViewitem3)*/

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val item = getItem(position) as DictionaryItem

        viewHolder.wordTextView.text =" ${item.word}"
        return view!!
    }

    // Filter method to update the displayed data based on the user input
    fun filter(newText: String?) {
        filteredData = if (newText.isNullOrBlank()) {
            originalData
        } else {
            originalData.filter { it.word.contains(newText, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }



    private class ViewHolder {
        lateinit var wordTextView: TextView

    }
}
