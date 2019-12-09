package com.kanzankazu.itungitungan.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.util.PictureUtil
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.view.base.BaseFragment
import com.kanzankazu.itungitungan.view.main.ProfileSub.AccountActivity
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * Created by Faisal Bahri on 2019-11-05.
 */
class ProfileFragment : BaseFragment(), ProfileFragmentContract.View {

    private lateinit var mPresenter: ProfileFragmentPresenter
    private lateinit var profileListAdapter: ProfileListAdapter

    private var uid: String? = ""
    private var mParam1: String? = null
    private var mParam2: String? = null

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /*fun newInstance(param1: String, param2: String): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }*/

        fun newInstance(): Fragment {
            return ProfileFragment()
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initParam()
        initSession()
        initContent()
        initListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Lihat", "onActivityResult ProfileFragment1 $requestCode , $resultCode")
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Lihat", "onActivityResult ProfileFragment2 $requestCode , $resultCode")
    }

    override fun setProfileOptionList() {
        val profileModels = arrayListOf<ProfileModel>()
        profileModels.add(ProfileModel(R.drawable.ic_clear, mActivity.getString(R.string.account), true))
        profileModels.add(ProfileModel(R.drawable.ic_back_black, mActivity.getString(R.string.share_friend_family), true))
        profileModels.add(ProfileModel(R.drawable.ic_clear, mActivity.getString(R.string.idea), true))
        profileModels.add(ProfileModel(R.drawable.ic_clear, mActivity.getString(R.string.help), true))
        profileModels.add(ProfileModel(R.drawable.ic_clear, mActivity.getString(R.string.donate), true))
        profileModels.add(ProfileModel(R.drawable.ic_clear, mActivity.getString(R.string.give_star), true))
        profileModels.add(ProfileModel(R.drawable.ic_clear, mActivity.getString(R.string.about), true))
        profileListAdapter.setData(profileModels)
    }

    override fun itemAdapterClick(position: Int) {
        when (position) {
            0 -> {
                Utils.intentTo(mActivity, AccountActivity::class.java, false)
            }
            1 -> {
                showSnackbar("1")
            }
            2 -> {
                showSnackbar("2")
            }
            3 -> {
                showSnackbar("3")
            }
            4 -> {
                showSnackbar("4")
            }
            5 -> {
                showSnackbar("5")
            }
            6 -> {
                showSnackbar("6")
            }
        }
    }

    private fun initParam() {

    }

    private fun initSession() {
        uid = UserPreference.getInstance().uid
    }

    private fun initContent() {
        loginUtil = FirebaseLoginUtil(mActivity, this)
        mPresenter = ProfileFragmentPresenter(mActivity, this)

        profileListAdapter = ProfileListAdapter(mActivity, this)
        //rv_profile_settings.setRecycledViewPool(RecyclerView.RecycledViewPool())
        rv_profile_settings.layoutManager = LinearLayoutManager(mActivity)
        rv_profile_settings.adapter = profileListAdapter

        setProfileData()
        setProfileOptionList()
    }

    private fun initListener() {
        iv_profile_settings.setOnClickListener { }
        civ_profile_photo.setOnClickListener { }
        civ_profile_photo_edit.setOnClickListener { PictureUtil.setupChooseImageDialog(mActivity) }
        cv_profile_signout.setOnClickListener {
            showProgressDialog()
            loginUtil.signOut(databaseUtil)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sv_profile_settings.setOnScrollChangeListener { _, _, _, _, _ ->
                fl_profile_header.isSelected = sv_profile_settings.canScrollVertically(-1)
            }
        }
    }

    private fun setProfileData() {
        tv_profile_name.setText(UserPreference.getInstance().name)
        tv_profile_email.setText(UserPreference.getInstance().email)
    }
}
