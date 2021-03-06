package net.halawata.artich

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import net.halawata.artich.model.ConfigListAdapter
import net.halawata.artich.model.config.ConfigList

class ConfigActivity : AppCompatActivity() {

    var configTypeNum = ConfigList.Type.MENU.num

    companion object {
        const val configTypeKey = "configTypeKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        // list setup
        val listView = findViewById<ListView>(R.id.config_list)

        configTypeNum = intent.getIntExtra(ConfigActivity.configTypeKey, ConfigList.Type.MENU.num)

        when (configTypeNum) {
            ConfigList.Type.MENU.num -> {
                title = resources.getString(R.string.menu_management_activity_title)

                val configList = ConfigList(resources, ConfigList.Type.MENU)
                val adapter = ConfigListAdapter(this, configList.getMenuList(), R.layout.config_list_item)

                listView.adapter = adapter

                listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val mediaType = ((view as LinearLayout).getChildAt(0) as TextView).text as String
                    val intent = Intent(this, MenuManagementActivity::class.java)
                    intent.putExtra(MenuManagementActivity.mediaTypeKey, mediaType)
                    startActivity(intent)
                }
            }
            ConfigList.Type.MUTE.num -> {
                title = resources.getString(R.string.mute_management_activity_title)

                val configList = ConfigList(resources, ConfigList.Type.MUTE)
                val adapter = ConfigListAdapter(this, configList.getMenuList(), R.layout.config_list_item)

                listView.adapter = adapter

                listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val mediaType = ((view as LinearLayout).getChildAt(0) as TextView).text as String
                    val intent = Intent(this, MuteManagementActivity::class.java)
                    intent.putExtra(MuteManagementActivity.mediaTypeKey, mediaType)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // ミュート編集から戻る場合は更新フラグを立てておく
        if (configTypeNum == ConfigList.Type.MUTE.num) {
            val intent = Intent()
            intent.putExtra("reload_article", true)
            setResult(Activity.RESULT_OK, intent)
        }

        return super.onKeyDown(keyCode, event)
    }
}
