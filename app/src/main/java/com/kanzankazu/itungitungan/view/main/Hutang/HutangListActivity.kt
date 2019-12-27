package com.kanzankazu.itungitungan.view.main.Hutang

import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hutang_list.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.util.*

class HutangListActivity : BaseActivity(), HutangListContract.View {

    private lateinit var hutangListAdapter: HutangListAdapter
    private var hutangNominal: Int = 0
    private var piutangNominal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_list)

        Utils.setupAppToolbarForActivity(this, toolbar, "Hutang")

        setView()
        setListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hutang_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menuHutangListAdd) {
            Utils.intentTo(this, HutangAddEditActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun itemViewOnClick(hutang: Hutang) {
    }

    private fun setView() {
        setRecyclerView()
        getHutang()
    }

    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_hutang_list.layoutManager = linearLayoutManager
        //toggleEmptyDataLayout(false)
        hutangListAdapter = HutangListAdapter(this, this)
        rv_hutang_list.adapter = hutangListAdapter
        //swipe_refresh.setColorSchemeResources(R.color.cyan)
        //swipe_refresh.setOnRefreshListener(this)
    }

    private fun setListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rv_hutang_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (recyclerView.canScrollVertically(-1)) {
                        toolbar.elevation = 0f
                    } else {
                        toolbar.elevation = 50f
                    }
                }
            })
        }
    }

    private fun getHutang() {
        showProgressDialog()
        Hutang.getHutangs(databaseUtil.getRootRef(false, false), false, object : FirebaseDatabaseUtil.ValueListenerDatas {
            override fun onSuccess(objects: MutableIterable<DataSnapshot>) {
                dismissProgressDialog()
                val hutangs = ArrayList<Hutang>()
                hutangNominal = 0
                piutangNominal = 0

                for (snapshot in objects) {
                    val hutang = snapshot.getValue(Hutang::class.java)
                    hutangs.add(hutang!!)

                    if (!hutang.hutangNominal.isNullOrEmpty()){
                        if (hutang.hutangRadioIndex == 0) {
                            hutangNominal = hutangNominal + hutang.hutangNominal.toInt()
                        } else {
                            piutangNominal = piutangNominal + hutang.hutangNominal.toInt()
                        }
                    }

                    tv_hutang_list_hutang.text = Utils.setRupiah(hutangNominal.toString())
                    tv_hutang_list_piutang.text = Utils.setRupiah(piutangNominal.toString())
                }
                hutangListAdapter.setData(hutangs)
            }

            override fun onFailure(message: String?) {
                dismissProgressDialog()
                showSnackbar(message)
            }
        })
    }
}
