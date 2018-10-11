package testing.cricut.com.pinterestdemo

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pinterest.android.pdk.PDKBoard


class BoardsAdapter (val context: Context?, _boardList: List<PDKBoard>) : BaseAdapter(){

 private val boardList: List<PDKBoard> = _boardList
  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
    val viewHolder: ViewHolderItem
    val view: View
    if (convertView == null) {
      val inflater = (context as Activity).getLayoutInflater()
      view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
      viewHolder = ViewHolderItem(view)
      view.tag = viewHolder
    } else {
      view = convertView
      viewHolder = convertView.tag as ViewHolderItem
    }

    viewHolder.textViewItem?.text = boardList[position].name

    return view
  }

  override fun getItem(position: Int): Any {
     return boardList.get(position)
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    return boardList.size
  }

  private inner class ViewHolderItem(row: View?) {
    internal var textViewItem: TextView? = null

    init {
      this.textViewItem = row?.findViewById(android.R.id.text1) as TextView
    }
  }
}