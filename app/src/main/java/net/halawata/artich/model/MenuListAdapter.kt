package net.halawata.artich.model

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import net.halawata.artich.R
import net.halawata.artich.entity.SideMenuItem
import kotlin.collections.ArrayList

class MenuListAdapter(val context: Context, var data: ArrayList<SideMenuItem>, val resource: Int): BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = data[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val activity = context as Activity
        val item = getItem(position) as SideMenuItem

        val view = convertView ?: activity.layoutInflater.inflate(resource, null)

        (view.findViewById(R.id.list_title) as TextView).text = item.title

        return view
    }
}
