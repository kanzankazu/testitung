package com.kanzankazu.itungitungan.view.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.InboxHistory
import com.kanzankazu.itungitungan.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_inbox.*

@Suppress("SameParameterValue")
class InboxHistoryFragment : BaseFragment(), InboxHistoryContract.View, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var mPresenter: InboxHistoryPresenter
    private lateinit var inboxAdapter: InboxHistoryAdapter

    private var mParam1: String? = ""
    private var mParam2: String? = ""

    companion object {

        val ARG_PARAM1 = "param1"
        val ARG_PARAM2 = "param2"
        fun newInstance(param1: String, param2: String): InboxHistoryFragment {
            val fragment = InboxHistoryFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): InboxHistoryFragment {
            return InboxHistoryFragment()
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
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = InboxHistoryPresenter(mActivity, this)

        initView()
        initListener()
    }

    override fun onRefresh() {
        if (srl_inbox_history.isRefreshing) {
            srl_inbox_history.isRefreshing = false
        }
    }

    override fun showToastView(message: String?) {
        showToast(message)
    }

    override fun showSnackbarView(message: String?) {
        showSnackbar(message)
    }

    override fun showRetryDialogView() {
        showRetryDialog { }
    }

    override fun showProgressDialogView() {
        showProgressDialog()
    }

    override fun dismissProgressDialogView() {
        dismissProgressDialog()
    }

    override fun toggleEmptyDataLayout(isVisible: Boolean) {
        if (isVisible) {
            visibleView(false, rv_inbox_history, srl_inbox_history)
        } else {
            visibleView(true, rv_inbox_history, srl_inbox_history)
        }
    }

    override fun setInboxHistoryData(dataSnapshot: DataSnapshot?) {
        val datas = mutableListOf<InboxHistory>()
        for (snapshot in dataSnapshot!!.children) {
            val data = snapshot.getValue(InboxHistory::class.java)
            if (data!!.inboxSenderReceiverUId.toLowerCase().contains(UserPreference.getInstance().uid)) {
                datas.add(data)
            }
        }
        inboxAdapter.addDatas(datas)
    }

    private fun initView() {
        setupInboxAdapter()

        mPresenter.getInboxHistoryData()
    }

    private fun initListener() {

    }

    @SuppressLint("ResourceType")
    private fun setupInboxAdapter() {
        val linearLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_inbox_history.layoutManager = linearLayoutManager
        toggleEmptyDataLayout(false)
        inboxAdapter = InboxHistoryAdapter(mActivity, this)
        rv_inbox_history.adapter = inboxAdapter
        srl_inbox_history.setColorSchemeResources(R.color.colorPrimaryDark)
        srl_inbox_history.setOnRefreshListener(this)
    }
}