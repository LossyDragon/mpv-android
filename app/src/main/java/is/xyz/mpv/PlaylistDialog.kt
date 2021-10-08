package `is`.xyz.mpv

import `is`.xyz.mpv.databinding.DialogPlaylistBinding
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class PlaylistDialog(private val player: MPVView) {
    private lateinit var binding: DialogPlaylistBinding

    private var playlist = listOf<MPVView.PlaylistFile>()

    private var pickFileAction: View.OnClickListener? = null
    private var openUrlAction: View.OnClickListener? = null
    private var pickItemListener: ((MPVView.PlaylistFile) -> Unit)? = null

    fun setPickFileAction(listener: View.OnClickListener) { pickFileAction = listener }
    fun setOpenUrlAction(listener: View.OnClickListener) { openUrlAction = listener }
    fun setPickItemListener(listener: (MPVView.PlaylistFile) -> Unit) { pickItemListener = listener }

    fun buildView(layoutInflater: LayoutInflater): View {
        binding = DialogPlaylistBinding.inflate(layoutInflater)

        // Set up recycler view
        val selectedIndex = MPVLib.getPropertyInt("playlist-pos") ?: -1
        playlist = player.loadPlaylist()
        Log.v(TAG, "PlaylistDialog: loaded ${playlist.size} items")
        binding.list.adapter = CustomAdapter(this, selectedIndex)
        binding.list.setHasFixedSize(true)
        binding.list.scrollToPosition(playlist.indexOfFirst { it.index == selectedIndex })

        binding.fileBtn.setOnClickListener(pickFileAction)
        binding.urlBtn.setOnClickListener(openUrlAction)

        return binding.root
    }

    private fun clickItem(position: Int) {
        val item = playlist[position]
        pickItemListener?.invoke(item)
    }

    class CustomAdapter(private val parent: PlaylistDialog, private val selected: Int) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        class ViewHolder(private val parent: PlaylistDialog, view: View) :
            RecyclerView.ViewHolder(view) {
            private val textView: TextView
            var selfPosition: Int = -1

            init {
                textView = view.findViewById(R.id.text)
                view.setOnClickListener {
                    parent.clickItem(selfPosition)
                }
            }

            fun bind(file: MPVView.PlaylistFile, selected: Boolean) {
                textView.text = file.name
                textView.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.dialog_playlist_item, viewGroup, false)

            return ViewHolder(parent, view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.selfPosition = position
            val item = parent.playlist[position]
            viewHolder.bind(item, item.index == selected)
        }

        override fun getItemCount() = parent.playlist.size
    }

    companion object {
        private const val TAG = "mpv"
    }
}
