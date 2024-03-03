package org.fossasia.badgemagic.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.licenses.MITLicense
import de.psdev.licensesdialog.model.Notice
import de.psdev.licensesdialog.model.Notices
import org.fossasia.badgemagic.R
import org.fossasia.badgemagic.databinding.FragmentAboutUsBinding
import org.fossasia.badgemagic.ui.base.BaseFragment

class AboutFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            AboutFragment()
    }

    private var _binding: FragmentAboutUsBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAboutUsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            aboutFossasia.setOnClickListener {
                contributorsLink()
            }

            githubLayout.setOnClickListener {
                github()
            }

            llAboutLicense.setOnClickListener {
                license()
            }

            llAboutLibs.setOnClickListener {
                libraryLicenseDialog()
            }
        }
    }

    private fun contributorsLink() {
        val url = "https://github.com/fossasia/badge-magic-android/graphs/contributors"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun github() {
        val url = "https://github.com/fossasia/badge-magic-android"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun license() {
        val url = "https://github.com/fossasia/badge-magic-android/blob/development/LICENSE"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun libraryLicenseDialog() {
        val notices = Notices()

        notices.addNotice(
            Notice(
                context?.getString(R.string.moshi),
                context?.getString(R.string.moshi_github),
                context?.getString(R.string.moshi_copy),
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                context?.getString(R.string.gif),
                context?.getString(R.string.gif_github),
                context?.getString(R.string.gif_copy),
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                context?.getString(R.string.timber),
                context?.getString(R.string.timber_github),
                context?.getString(R.string.timber_copy),
                MITLicense()
            )
        )
        notices.addNotice(
            Notice(
                context?.getString(R.string.scanner),
                context?.getString(R.string.scanner_github),
                context?.getString(R.string.scanner_copy),
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                context?.getString(R.string.licences),
                context?.getString(R.string.licences_github),
                context?.getString(R.string.licences_copy),
                ApacheSoftwareLicense20()
            )
        )

        LicensesDialog.Builder(context)
            .setNotices(notices)
            .setIncludeOwnLicense(true)
            .build()
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
