package com.kanzankazu.itungitungan.view.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.util.DialogUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.view.base.BaseFragment
import com.kanzankazu.itungitungan.view.main.ProfileSub.AboutActivity
import com.kanzankazu.itungitungan.view.main.ProfileSub.AccountActivity
import com.kanzankazu.itungitungan.view.main.ProfileSub.DonateOptionDialogFragment
import com.kanzankazu.itungitungan.view.main.ProfileSub.GiveStarIdeaDialogFragment
import kotlinx.android.synthetic.main.fragment_profile.*


/**
 * Created by Faisal Bahri on 2019-11-05.
 */
class ProfileFragment : BaseFragment(), ProfileFragmentContract.View {

    private lateinit var mPresenter: ProfileFragmentPresenter
    private lateinit var profileListAdapter: ProfileAccountOptionAdapter

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

        fun newInstance(): ProfileFragment {
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

    override fun onResume() {
        super.onResume()

        initContent()
    }

    override fun setProfileOptionList() {
        val profileModels = arrayListOf<ProfileAccountModel>()
        profileModels.add(ProfileAccountModel(R.drawable.ic_share, mActivity.getString(R.string.share_friend_family), "", "", true))
        profileModels.add(ProfileAccountModel(R.drawable.ic_idea, mActivity.getString(R.string.idea), "", "", true))
        profileModels.add(ProfileAccountModel(R.drawable.ic_help, mActivity.getString(R.string.help), "", "", true))
        profileModels.add(ProfileAccountModel(R.drawable.ic_donate, mActivity.getString(R.string.donate), "", "", true))
        profileModels.add(ProfileAccountModel(R.drawable.ic_rate_app, mActivity.getString(R.string.give_star), "", "", true))
        profileModels.add(ProfileAccountModel(R.drawable.ic_info, mActivity.getString(R.string.about), "", "", true))
        profileListAdapter.setData(profileModels)
    }

    override fun itemAdapterClick(data: ProfileAccountModel) {
        when (data.title) {
            mActivity.getString(R.string.share_friend_family) -> {
                share()
            }
            mActivity.getString(R.string.idea) -> {
                val fm = mActivity.supportFragmentManager
                val giveStarDialogFragment = GiveStarIdeaDialogFragment.newInstance(false)
                giveStarDialogFragment.show(fm, "fragment_giveStarDialogFragment")
            }
            mActivity.getString(R.string.help) -> {
                helpDialog()
            }
            mActivity.getString(R.string.donate) -> {
                val fm = mActivity.supportFragmentManager
                val donateOptionDialogFragment = DonateOptionDialogFragment.newInstance()
                donateOptionDialogFragment.show(fm, "fragment_donateOptionDialogFragment")
            }
            mActivity.getString(R.string.give_star) -> {
                val fm = mActivity.supportFragmentManager
                val giveStarDialogFragment = GiveStarIdeaDialogFragment.newInstance(true)
                giveStarDialogFragment.show(fm, "fragment_giveStarDialogFragment")
            }
            mActivity.getString(R.string.about) -> {
                Utils.intentTo(mActivity, AboutActivity::class.java, false)
            }
        }
    }

    private fun share() {

        //Share text:
        /*val intentText = Intent()
        intentText.action = Intent.ACTION_SEND
        intentText.type = "text/plain"
        intentText.putExtra(Intent.EXTRA_TEXT, R.string.share_friend_family_message)
        startActivity(Intent.createChooser(intentText, "Share via"))*/

        //via Email:
        /*val intent2 = Intent()
        intent2.action = Intent.ACTION_SEND
        intent2.type = "message/rfc822"
        intent2.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(EMAIL1, EMAIL2))
        intent2.putExtra(Intent.EXTRA_SUBJECT, "Email Subject")
        intent2.putExtra(Intent.EXTRA_TEXT, "Your text here")
        startActivity(intent2)*/

        //Share Files:
        //Image:
        /*val isPNG = path.toLowerCase().endsWith(".png")
        val i = Intent(Intent.ACTION_SEND)
        if (isPNG) {
            i.type = "image/png"
        } else {
            i.type = "image/jpeg"
        }

        val imgUri = Uri.fromFile(File(path))//Absolute Path of image
        i.putExtra(Intent.EXTRA_STREAM, imgUri)//Uri of image
        startActivity(Intent.createChooser(i, "Share via"))*/

        //APK:
        /*val f = File(path1)
        if (f.exists()) {
            val intent2 = Intent()
            intent2.action = Intent.ACTION_SEND
            intent2.type = "application/vnd.android.package-archive"//APk file type
            intent2.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f))
            startActivity(Intent.createChooser(intent2, "Share via"))
        }*/

        val shareBody = mActivity.getString(R.string.profile_share_body)
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, mActivity.getString(R.string.profile_share_subject))
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    private fun initParam() {

    }

    private fun initSession() {
        uid = UserPreference.getInstance().uid
    }

    private fun initContent() {
        loginUtil = FirebaseLoginUtil(mActivity, this)
        mPresenter = ProfileFragmentPresenter(mActivity, this)

        profileListAdapter = ProfileAccountOptionAdapter(mActivity, object : ProfileAccountOptionAdapter.ProfileAccountAdapterListener {
            override fun onItemAdapterClick(position: Int, data: ProfileAccountModel) {
                itemAdapterClick(data)
            }
        })
        //rv_profile_settings.setRecycledViewPool(RecyclerView.RecycledViewPool())
        rv_profile_settings.layoutManager = LinearLayoutManager(mActivity)
        rv_profile_settings.adapter = profileListAdapter

        setProfileData()
        setProfileOptionList()
    }

    private fun initListener() {
        iv_profile_settings.setOnClickListener { }
        civ_profile_photo.setOnClickListener {
            moveTo(AccountActivity::class.java, false)
        }
        civ_profile_edit.setOnClickListener { }
        cv_profile_signout.setOnClickListener {
            confirmSignOutDialog()
        }
    }

    private fun helpDialog() {
        val listOf = mutableListOf("Email", "Whatsapp")
        val sequenceArray = DialogUtil.convertListStringToCharSequenceArray(listOf)
        val alertDialog = DialogUtil.setupRadioAlertDialog(mActivity, "Opsi Minta Bantuan", sequenceArray, -1, 0, "") { index, identifier, mode ->
            when (index) {
                0 -> {
                    helpEmail()
                }
                1 -> {
                    helpWhatsapp()
                }
            }
        }
        alertDialog.show()
    }

    private fun helpEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "message/rfc822"
        //emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.ADMIN_EMAIL))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mActivity.getString(R.string.profile_help_subject))
        emailIntent.putExtra(Intent.EXTRA_TEXT, mActivity.getString(R.string.profile_help_body))
        //val root = Environment.getExternalStorageDirectory()
        //val pathToMyAttachedFile = "temp/attachement.xml"
        //val file = File(root, pathToMyAttachedFile)
        //if (!file.exists() || !file.canRead()) {
        //    return
        //}
        //val uri = Uri.fromFile(file)
        //emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"))

    }

    private fun helpWhatsapp() {
        try {
            val intentAction = Intent(Intent.ACTION_VIEW)
            intentAction.data = Uri.parse(Constants.URI_WA_FORMAT)
            startActivity(intentAction)
        } catch (e: ActivityNotFoundException) {
            showToast(mActivity.getString(R.string.message_apps_not_found))
        }
    }

    private fun confirmSignOutDialog() {
        DialogUtil.showYesNoDialog(mActivity, "Konfirmasi", "Apakah anda ingin keluar akun?", object : DialogUtil.IntroductionButtonListener {
            override fun onFirstButtonClick() {
                showProgressDialog()
                loginUtil.signOut(true)
            }

            override fun onSecondButtonClick() {
            }
        })
    }

    private fun moveTo(targetClass: Class<*>, isFinish: Boolean) {
        Utils.intentTo(mActivity, targetClass, isFinish)
    }

    private fun setProfileData() {
        tv_account_user_name_big.text = UserPreference.getInstance().name
        tv_profile_email.text = UserPreference.getInstance().email
    }
}
