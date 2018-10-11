package testing.cricut.com.pinterestdemo

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.pinterest.android.pdk.PDKCallback
import com.pinterest.android.pdk.PDKClient
import com.pinterest.android.pdk.PDKException
import com.pinterest.android.pdk.PDKResponse
import com.pinterest.android.pdk.Utils
import org.json.JSONObject

class BoardDialogFragment : DialogFragment() {

  private var boardName: EditText? = null
  private var boardDesc: EditText? = null
  private var saveButton: Button? = null
  private var responseView: TextView? = null
  private lateinit var updateList: UpdateListListener

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(DialogFragment.STYLE_NORMAL, R.style.App)
    try {
      updateList = PinterestShareFragmnet()
    } catch (e: ClassCastException) {
      throw ClassCastException("Calling Fragment must implement UpdateListListener")
    }

  }
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.board_dialog_fragment, container, false);
    boardName = view.findViewById(R.id.board_create_name)
    boardDesc = view.findViewById(R.id.board_create_desc)
    responseView = view.findViewById(R.id.board_response_view)
    saveButton = view.findViewById(R.id.save_button)
    saveButton?.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View) {
        onSaveBoard()
      }
    })

    return view;
  }

  private fun onSaveBoard() {
    val bName = boardName?.getText().toString()
    if (!Utils.isEmpty(bName)) {
      PDKClient.getInstance()
          .createBoard(bName, boardDesc?.getText().toString(), object : PDKCallback() {
            override fun onSuccess(response: PDKResponse) {
              Log.d(javaClass.name, response.data.toString())
              responseView?.setText(response.data.toString())
              Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
              updateList.updatelist()
              dismiss();
            }

            override fun onFailure(exception: PDKException) {
              Log.e(javaClass.name, exception.detailMessage)
              responseView?.setText(exception.detailMessage)
            }
          })
    } else {
      Toast.makeText(context, "Board name cannot be empty", Toast.LENGTH_SHORT).show()
    }
  }

  interface UpdateListListener {
    fun updatelist()
  }
}