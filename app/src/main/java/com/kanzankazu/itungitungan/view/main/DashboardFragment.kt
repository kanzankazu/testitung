package com.kanzankazu.itungitungan.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.view.base.BaseFragment
import retrofit2.Call

class DashboardFragment : BaseFragment(), DashboardFragmentContract.View, FirebaseDatabaseUtil.ValueListenerData {
    override fun onSuccess(dataSnapshot: DataSnapshot?) {}

    override fun onFailure(message: String?) {}

    private var mParam1: String? = ""
    private var mParam2: String? = ""
    
    var mPresenter = DashboardFragmentPresenter(mActivity,this)

    companion object {
        val ARG_PARAM1 = "param1"
        val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): DashboardFragment {
            val fragment = DashboardFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): Fragment {
            return DashboardFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
    }

    override fun showToastView(message: String?) {showToast(message)}

    override fun showSnackbarView(message: String?) {showSnackbar(message)}

    override fun showRetryDialogView(call: Call<*>?) {}

    override fun showProgressDialogView() {showProgressDialog()}

    override fun dismissProgressDialogView() {dismissProgressDialog()}

    private fun initView() {
        mPresenter.getHutang(this)
    }

    private fun initListener() {

    }
}