package de.tp.hillforts.views.hillfordList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import de.tp.hillforts.R
import de.tp.hillforts.models.HillfortModel
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.hillford_list_view_portrait.*
import org.jetbrains.anko.info

class HillfordListView : BaseView(), HillfordListener {

  lateinit var presenter: HillfordListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.hillford_list_view_portrait)

    // init presenter
    presenter = initPresenter(HillfordListPresenter(this)) as HillfordListPresenter

    // init toolbar
    init(toolbar, true) //disable up-support later; just for test purposes

    rvHillforts.layoutManager = LinearLayoutManager(this)
    presenter.loadPlacemarks()
  }

  /**
   * Executed by HillfordViewHolder::bind() when a hillford is clicked on.
   * @param hillford hillford which was clicked on
   */
  override fun onHillfordClick(hillford: HillfortModel) {
    info("Hillford was clicked:\n$hillford")
  }

  /**
   * Show all hillforts on the screen.
   * @param hillforts hillforts to be displayed
   * @author Thomas Pilz
   */
  override fun showHillforts(hillforts: List<HillfortModel>) {
    rvHillforts.adapter = HillfortAdapter(hillforts, this)
    rvHillforts.adapter?.notifyDataSetChanged()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
      R.id.itemAdd -> presenter.doAddHillfort()
      R.id.itemLogout -> presenter.doLogout()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_list_view, menu)
    return super.onCreateOptionsMenu(menu)
  }

}
