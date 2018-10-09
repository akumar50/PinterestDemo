package testing.cricut.com.pinterestdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.pinterest.android.pdk.PDKException
import com.pinterest.android.pdk.PDKResponse
import com.pinterest.android.pdk.PDKCallback
import com.pinterest.android.pdk.PDKClient
import com.pinterest.android.pdk.Utils
import org.json.JSONObject

class CreateBoardActivity : AppCompatActivity() {

  private var boardName: EditText? = null
  private var boardDesc:EditText? = null
  private var saveButton: Button? = null
  private var responseView: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_create_board)
    title = "New Board"
    boardName = findViewById(R.id.board_create_name)
    boardDesc = findViewById(R.id.board_create_desc)
    responseView = findViewById(R.id.board_response_view)
    saveButton = findViewById(R.id.save_button)
    saveButton?.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View) {
        onSaveBoard()
      }
    })
  }

  private fun onSaveBoard() {
    val bName = boardName?.getText().toString()
    if (!Utils.isEmpty(bName)) {
      PDKClient.getInstance()
          .createBoard(bName, boardDesc?.getText().toString(), object : PDKCallback() {
            override fun onSuccess(response: PDKResponse) {
              Log.d(javaClass.name, response.data.toString())
              responseView?.setText(response.data.toString())
              prefs.baordID = (response.data as JSONObject).getString("id")
              Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(exception: PDKException) {
              Log.e(javaClass.name, exception.detailMessage)
              responseView?.setText(exception.detailMessage)
            }
          })
    } else {
      Toast.makeText(this, "Board name cannot be empty", Toast.LENGTH_SHORT).show()
    }
  }
}