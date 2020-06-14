package dev.iamwee.android.tamboon.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import dagger.hilt.android.AndroidEntryPoint
import dev.iamwee.android.tamboon.R
import dev.iamwee.android.tamboon.data.CharityInfo
import dev.iamwee.android.tamboon.ui.charity.CharityListFragment
import dev.iamwee.android.tamboon.ui.donation.DonationFragment
import dev.iamwee.android.tamboon.ui.donation.succeeded.DonationSucceededActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CharityListFragment.Delegate, DonationFragment.Delegate {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragmentContainer,
                    CharityListFragment()
                )
                .commit()
        }
    }

    override fun onCharityClicked(info: CharityInfo) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragmentContainer, DonationFragment.fromCharity(info))
            .addToBackStack(null)
            .commit()
    }

    override fun onDonateSucceeded(charity: CharityInfo, amount: Long) {
        supportFragmentManager.popBackStack()
        DonationSucceededActivity.launch(this, charity, amount)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount != 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}