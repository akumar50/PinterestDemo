package testing.cricut.com.pinterestdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.pinterest.android.pdk.PDKCallback
import com.pinterest.android.pdk.PDKClient
import com.pinterest.android.pdk.PDKException
import com.pinterest.android.pdk.PDKResponse

class MainActivity : AppCompatActivity(), View.OnClickListener {

  private var pdkClient: PDKClient? = null
  private var loginButton: Button? = null
  private val appID: String = "4993350386034172395"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    loginButton = findViewById(R.id.login)
    loginButton?.setOnClickListener(this)
    pdkClient = PDKClient.configureInstance(this, appID)
    pdkClient?.onConnect(this)
    //pdkClient?.setDebugMode(true)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    pdkClient?.onOauthResponse(requestCode, resultCode,
        data)
  }

  fun onLogin() {
    val scopes = ArrayList<String>()
    scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
    scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
    scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
    scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);

    pdkClient?.login(this, scopes, object : PDKCallback() {
      override fun onSuccess(response: PDKResponse) {
        Log.d(javaClass.name, response.data.toString())
        onLoginSuccess()
        //user logged in, use response.getUser() to get PDKUser object
      }

      override fun onFailure(exception: PDKException) {
        Log.e(javaClass.name, exception.detailMessage)
      }
    })
  }

  override fun onClick(v: View?) {
    val vid = v?.id;
    when (vid) {
      R.id.login -> onLogin()
    }
  }

  fun onLoginSuccess() {
    val i = Intent(this, HomeActivity::class.java)
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(i);
    finish();
  }
}
