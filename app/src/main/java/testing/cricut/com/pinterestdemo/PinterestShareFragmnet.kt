package testing.cricut.com.pinterestdemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.pinterest.android.pdk.PDKBoard
import com.squareup.picasso.Picasso
import com.pinterest.android.pdk.PDKException
import com.pinterest.android.pdk.PDKResponse
import com.pinterest.android.pdk.PDKCallback
import com.pinterest.android.pdk.PDKClient
import com.pinterest.android.pdk.Utils

class PinterestShareFragmnet : Fragment(), View.OnClickListener,
    BoardDialogFragment.UpdateListListener {

  companion object  {
    fun newInstance(): PinterestShareFragmnet {
      return PinterestShareFragmnet()
    }
  }

  private var imageView : ImageView? = null
  private var mySpinner : Spinner? = null
  private  val imageUrl : String = "https://s3-us-west-2.amazonaws.com/cricut-public/users/1140378/projects/5bbb30f1ce733d061a34d9ea/296584-q3-and-q4-mik-projects-apron-6819-large.jpg"
  private var myBoardsCallback: PDKCallback? = null
  private val BOARD_FIELDS = "id,name,description,creator,image,counts,created_at"
  private var createButton : Button? = null
  private var saveButton : Button? = null
  private var boardId : String? = null
  private var boardsAdapter : BoardsAdapter? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_pin_share, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    imageView = view.findViewById(R.id.image_view)
    mySpinner = view.findViewById(R.id.boards)
    createButton = view.findViewById(R.id.createButton)
    saveButton = view.findViewById(R.id.saveButton)
    createButton?.setOnClickListener(this)
    saveButton?.setOnClickListener(this)
    initUI()
  }

  private fun fetchBoards() {
    PDKClient.getInstance().getMyBoards(BOARD_FIELDS, myBoardsCallback)
  }

  fun initUI(){
    Picasso.with(activity).load(imageUrl).into(imageView)

    myBoardsCallback = object : PDKCallback() {
      override fun onSuccess(response: PDKResponse) {
        if(response.boardList.size > 0) {
          mySpinner?.visibility = View.VISIBLE
          boardsAdapter = BoardsAdapter(context, response.boardList)
          mySpinner?.adapter = boardsAdapter
        }
      }

      override fun onFailure(exception: PDKException) {
        Log.e(javaClass.name, exception.detailMessage)
      }
    }
    mySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
      }

      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        boardId = (p0?.selectedItem as PDKBoard).uid
      }

    }
  }

  override fun onResume() {
    super.onResume()
    fetchBoards()
  }

  override fun onClick(v: View?) {
    when(v?.id) {
      R.id.createButton -> createBoard()
      R.id.saveButton -> onSavePin()
      else -> { }
    }
  }

  private fun createBoard() {

    val ft = fragmentManager!!.beginTransaction()
    val prev = fragmentManager!!.findFragmentByTag("dialog")
    if (prev != null) {
      ft.remove(prev)
    }
    ft.addToBackStack(null)
    val dialogFragment = BoardDialogFragment()
    dialogFragment.show(ft, "dialog")
  }

  private fun onSavePin() {
    val pinImageUrl = imageUrl
    val board = boardId
    val noteText = "Testing Application"
    val link = "https://www.google.com"
    if (!Utils.isEmpty(noteText) && !Utils.isEmpty(board) && !Utils.isEmpty(pinImageUrl)) {
      PDKClient
          .getInstance().createPin(noteText, board, pinImageUrl, link,
              object : PDKCallback() {
                override fun onSuccess(response: PDKResponse) {
                  Log.d(javaClass.name, response.data.toString())
                  Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(exception: PDKException) {
                  Log.e(javaClass.name, exception.detailMessage)
                }
              })
    } else {
      Toast.makeText(context, "Required fields cannot be empty", Toast.LENGTH_SHORT).show()
    }
  }

  override fun updatelist() {
    myBoardsCallback = object : PDKCallback() {
      override fun onSuccess(response: PDKResponse) {
        if(response.boardList.size > 0) {
          mySpinner?.visibility = View.VISIBLE
          boardsAdapter = BoardsAdapter(context, response.boardList)
          mySpinner?.adapter = boardsAdapter
        }
      }

      override fun onFailure(exception: PDKException) {
        Log.e(javaClass.name, exception.detailMessage)
      }
    }
    fetchBoards()
  }

}