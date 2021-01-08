package de.tp.hillforts.views.hillfordList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.tp.hillforts.R
import de.tp.hillforts.models.HillfortModel
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
    init(toolbar)

    presenter.loadPlacemarks()
  }

  /**
   * Executed when a hillford is clicked on in recycler view.
   * @param hillford hillford which was clicked on
   */
  override fun onHillfordClick(hillford: HillfortModel) {
    TODO("Not yet implemented")
  }

  /**
   * Show all hillforts on the screen.
   * @param hillforts hillforts to be displayed
   * @author Thomas Pilz
   */
  override fun showHillforts(hillforts: List<HillfortModel>) {
    TODO("Not yet implemented")
  }
}
