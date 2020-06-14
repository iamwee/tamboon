package dev.iamwee.android.tamboon.ui.donation.succeeded

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import dev.iamwee.android.tamboon.R
import dev.iamwee.android.tamboon.data.CharityInfo
import kotlinx.android.synthetic.main.activity_donation_succeeded.*
import java.text.DecimalFormat

@AndroidEntryPoint
class DonationSucceededActivity : AppCompatActivity(R.layout.activity_donation_succeeded) {

    companion object {
        private const val EXTRA_CHARITY = "dev.iamwee.android.tamboon.ui.donation.succeeded.EXTRA_CHARITY"
        private const val EXTRA_AMOUNT = "dev.iamwee.android.tamboon.ui.donation.succeeded.EXTRA_AMOUNT"
        fun launch(context: Context, charity: CharityInfo, amount: Long) {
            val intent = Intent(context, DonationSucceededActivity::class.java).apply {
                putExtra(EXTRA_CHARITY, charity)
                putExtra(EXTRA_AMOUNT, amount)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Donate Succeeded"

        val amount = intent.getLongExtra(EXTRA_AMOUNT, -1)
        val charity = intent.getParcelableExtra<CharityInfo>(EXTRA_CHARITY)

        if (amount < 0 || charity == null) {
            textViewDonationSuccessMessage.isVisible = false
        } else {
            textViewDonationSuccessMessage.text = getString(
                R.string.message_donate_successfully,
                DecimalFormat("#,###,###,###").format(amount),
                charity.name
            )
        }

        buttonDonationSuccessClose.setOnClickListener { finish() }
    }
}