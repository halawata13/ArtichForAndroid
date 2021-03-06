package net.halawata.artich

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.view.LayoutInflater
import android.os.Bundle
import android.widget.EditText
import net.halawata.artich.enum.Media
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.Log
import net.halawata.artich.model.menu.MediaMenuFactory

class MenuAdditionFragment : DialogFragment() {
    var mediaType: Media? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val content = inflater.inflate(R.layout.fragment_menu_addition, null)

        builder.setView(content)

        builder.setTitle(getString(R.string.add_item))
                .setPositiveButton(getString(R.string.add), { dialogInterface, i ->
                    val editText = content.findViewById(R.id.menu_addition_text) as EditText

                    mediaType?.let {
                        val text = editText.text.toString()
                        val helper = DatabaseHelper(activity)
                        val mediaMenu = MediaMenuFactory.create(it, helper, activity.resources)
                        val activity = activity as MenuManagementActivity

                        try {
                            // activity.listView.insert() で最後尾に追加しようとすると落ちるので普通に突っ込む
                            activity.mediaList.add(text)
                            mediaMenu.save(activity.mediaList)
                            activity.adapter.notifyDataSetChanged()
                            activity.listView.invalidateViews()

                        } catch (ex: Exception) {
                            Log.e(ex.message)
                            activity.showError(getString(R.string.loading_fail_data))
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)

        return builder.create()
    }
}
