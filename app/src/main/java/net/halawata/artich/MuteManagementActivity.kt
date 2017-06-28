package net.halawata.artich

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter
import net.halawata.artich.entity.ListItem
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.MuteManagementListAdapter
import net.halawata.artich.model.config.ConfigList
import net.halawata.artich.model.mute.MediaMuteFactory
import net.halawata.artich.model.mute.MediaMuteInterface

class MuteManagementActivity : AppCompatActivity() {

    companion object {
        val mediaTypeKey = "muteManagementMediaTypeKey"
    }

    lateinit var mediaMute : MediaMuteInterface
    lateinit var mediaList: ArrayList<ListItem>
    lateinit var adapter: AlphaInAnimationAdapter
    lateinit var listView: DynamicListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mute_management)

        val mediaString = intent.getStringExtra(MuteManagementActivity.mediaTypeKey)
        val configList = ConfigList(resources, ConfigList.Type.MUTE)
        val mediaType = configList.getMediaId(mediaString) ?: return

        title = resources.getString(R.string.mute_management_activity_title) + "（" + mediaString + "）"

        val helper = DatabaseHelper(this)

        mediaMute = MediaMuteFactory.create(mediaType, helper)
        try {
            mediaList = mediaMute.get()

        } catch (ex: Exception) {
            showError("データの読み込みに失敗しました")
            return
        }

        val listAdapter = MuteManagementListAdapter(this, mediaList)
        listView = findViewById(R.id.mute_management_list) as DynamicListView

        val simpleSwipeUndoAdapter = SimpleSwipeUndoAdapter(listAdapter, this, OnDismissCallback { listView, reverseSortedPositions ->
        })

        adapter = AlphaInAnimationAdapter(simpleSwipeUndoAdapter)
        adapter.setAbsListView(listView)
        adapter.viewAnimator?.setInitialDelayMillis(300)
        listView.adapter = adapter

        listView.enableSwipeToDismiss { listView, reverseSortedPositions ->
            for (position in reverseSortedPositions) {
                try {
                    val selectedItem = adapter.getItem(position) as ListItem
                    mediaMute.remove(selectedItem.id.toInt())

                    mediaList.removeAt(position)
                    adapter.notifyDataSetChanged()
                    this.listView.invalidateViews()

                } catch (ex: Exception) {
                    showError("項目の削除に失敗しました")
                }
            }
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, "左にスワイプするとミュートを解除します", Toast.LENGTH_LONG).show()
        }
    }

    fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}