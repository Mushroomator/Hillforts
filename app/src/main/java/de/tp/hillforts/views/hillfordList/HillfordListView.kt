package de.tp.hillforts.views.hillfordList

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.hillford_list_view.*
import org.jetbrains.anko.info

class HillfordListView : BaseView(), HillfordListener, TabLayout.OnTabSelectedListener {

  lateinit var presenter: HillfordListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.hillford_list_view)

    // init presenter
    presenter = initPresenter(HillfordListPresenter(this)) as HillfordListPresenter

    // init toolbar
    init(toolbar, false) //disable up-support later; just for test purposes

    tabLayout.addOnTabSelectedListener(this)

    rvHillforts.layoutManager = LinearLayoutManager(this)
    presenter.loadHillforts()
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
      R.id.itemMap -> presenter.doShowMap()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_list_view, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    //presenter.loadHillforts() // notify recyclerView that data has been changed --> not required anymore as on resume will load data anyway
  }

  override fun onBackPressed() {
    super.onBackPressed()
    presenter.doLogout()
  }

  override fun onResume() {
    super.onResume()
    presenter.doLoadData()
  }

  /**
   * Called when a tab enters the selected state.
   *
   * @param tab The tab that was selected
   */
  override fun onTabSelected(tab: TabLayout.Tab?) {
    if (tab != null){
      if(tab.position == 1){
        presenter.loadFavourites()
      }
      else{
        presenter.loadHillforts()
      }
    }
  }

  /**
   * Called when a tab exits the selected state.
   *
   * @param tab The tab that was unselected
   */
  override fun onTabUnselected(tab: TabLayout.Tab?) {
    /* no-op */
  }

  /**
   * Called when a tab that is already selected is chosen again by the user. Some applications may
   * use this action to return to the top level of a category.
   *
   * @param tab The tab that was reselected.
   */
  override fun onTabReselected(tab: TabLayout.Tab?) {
    /* no-op */
  }
}
