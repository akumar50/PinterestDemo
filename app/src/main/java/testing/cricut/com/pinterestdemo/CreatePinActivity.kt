package testing.cricut.com.pinterestdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.pinterest.android.pdk.PDKException
import com.pinterest.android.pdk.PDKResponse
import com.pinterest.android.pdk.PDKCallback
import android.util.Log
import android.view.View
import android.widget.Toast
import com.pinterest.android.pdk.PDKClient
import com.pinterest.android.pdk.Utils

class CreatePinActivity : AppCompatActivity() {

  private var imageUrl: EditText? = null
  private var link:EditText? = null
  private var boardId:EditText? = null
  private var note:EditText? = null
  private var saveButton: Button? = null
  private var selectImagebutton:Button? = null
  private var responseView: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_create_pin)
    title = "New Pin"
    imageUrl = findViewById(R.id.pin_create_url) as EditText
    link = findViewById(R.id.pin_create_link) as EditText
    note = findViewById(R.id.pin_create_note) as EditText
    responseView = findViewById(R.id.pin_response_view) as TextView
    boardId = findViewById(R.id.create_pin_board_id) as EditText
    saveButton = findViewById(R.id.save_button) as Button
    saveButton?.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View) {
        onSavePin()
      }
    })
  }

  private fun onSavePin() {
    val pinImageUrl = imageUrl?.text.toString()
    val board = boardId?.text.toString()
    val noteText = note?.text.toString()
    if (!Utils.isEmpty(noteText) && !Utils.isEmpty(board) && !Utils.isEmpty(pinImageUrl)) {
      PDKClient
          .getInstance().createPin(noteText, board, pinImageUrl, pinImageUrl,
              object : PDKCallback() {
                override fun onSuccess(response: PDKResponse) {
                  Log.d(javaClass.name, response.data.toString())
                  responseView?.setText(response.data.toString())

                }

                override fun onFailure(exception: PDKException) {
                  Log.e(javaClass.name, exception.detailMessage)
                  responseView?.setText(exception.detailMessage)
                }
              })
    } else {
      Toast.makeText(this, "Required fields cannot be empty", Toast.LENGTH_SHORT).show()
    }
  }
}