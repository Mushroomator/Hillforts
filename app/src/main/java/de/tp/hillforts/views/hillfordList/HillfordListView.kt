package de.tp.hillforts.views.hillfordList

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.hillford_list_view_portrait.*

class HillfordListView : BaseView(), HillfordListener {

  lateinit var presenter: HillfordListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.hillford_list_view_portrait)

    // init presenter
    presenter = initPresenter(HillfordListPresenter(this)) as HillfordListPresenter

    // init toolbar
    init(toolbar, false) //disable up-support later; just for test purposes

    rvHillforts.layoutManager = LinearLayoutManager(this)
    presenter.loadPlacemarks()
  }

  /**
   * Executed by HillfordViewHolder::bind() when a hillford is clicked on.
   * @param hillford hillford which was clicked on
   */
  override fun onHillfordClick(hillford: HillfortModel) {
    presenter.doEditHillfort(hillford)
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
      R.id.itemSave -> presenter.doAddHillfort()
      R.id.itemLogout -> presenter.doLogout()
      R.id.itemSettings -> presenter.doShowSettings()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_list_view, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    presenter.loadPlacemarks() // notify recyclerView that data has been changed
  }

}
