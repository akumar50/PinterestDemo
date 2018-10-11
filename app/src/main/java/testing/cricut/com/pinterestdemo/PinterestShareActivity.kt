package testing.cricut.com.pinterestdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class PinterestShareActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pin_share)
    if (savedInstanceState == null) {
      supportFragmentManager
          .beginTransaction()
          .add(R.id.fragment_container, PinterestShareFragmnet.newInstance(), "pinshare")
          .commit()
    }
  }
}