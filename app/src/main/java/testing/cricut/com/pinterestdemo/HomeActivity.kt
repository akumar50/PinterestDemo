package testing.cricut.com.pinterestdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.pinterest.android.pdk.PDKUser
import com.pinterest.android.pdk.PDKClient
import com.pinterest.android.pdk.PDKException
import com.pinterest.android.pdk.PDKResponse
import com.pinterest.android.pdk.PDKCallback
import com.pinterest.android.pdk.Utils
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity(){

  private val DEBUG = true
  private var shareButton: Button? = null
  private var logoutButton: Button? = null
  private var nameTv: TextView? = null
  private var profileIv: ImageView? = null
  private val USER_FIELDS = "id,image,counts,created_at,first_name,last_name,bio"
  var user: PDKUser? = null

  @Override
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    setTitle("Pinterest Demo")

    nameTv = findViewById(R.id.name_textview) as TextView
    profileIv = findViewById(R.id.profile_imageview) as ImageView
    shareButton = findViewById(R.id.share_button) as Button
    shareButton?.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View) {
        sharePins()
      }
    })

    logoutButton = findViewById(R.id.logout_button) as Button
    logoutButton?.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View) {
        onLogout()
      }
    })
    getMe()
  }

  private fun setUser() {
    nameTv?.setText(user?.firstName + " " + user?.lastName)
    Picasso.with(this).load(user?.imageUrl).into(profileIv)
  }

  private fun getMe() {
    PDKClient.getInstance().getMe(USER_FIELDS, object : PDKCallback() {
      override fun onSuccess(response: PDKResponse) {
        if (DEBUG) log(String.format("status: %d", response.statusCode))
        user = response.user
        setUser()
      }

      override fun onFailure(exception: PDKException) {
        if (DEBUG) log(exception.detailMessage)
        Toast.makeText(this@HomeActivity, "/me Request failed", Toast.LENGTH_SHORT).show()
      }
    })
  }

  private fun sharePins() {
    val i = Intent(this, PinterestShareActivity::class.java)
    startActivity(i)
  }

  private fun onLogout() {
    PDKClient.getInstance().logout()
    val i = Intent(this, MainActivity::class.java)
    startActivity(i)
    finish()
  }


  private fun log(msg: String) {
    if (!Utils.isEmpty(msg))
      Log.d(javaClass.name, msg)
  }

}