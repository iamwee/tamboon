package dev.iamwee.android.tamboon.ui.donation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import co.omise.android.models.Token
import co.omise.android.ui.CreditCardActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import dev.iamwee.android.tamboon.BuildConfig
import dev.iamwee.android.tamboon.R
import dev.iamwee.android.tamboon.common.Result
import dev.iamwee.android.tamboon.data.CharityInfo
import dev.iamwee.android.tamboon.extensions.errorMessage
import kotlinx.android.synthetic.main.fragment_donation.*
import java.text.DecimalFormat

@AndroidEntryPoint
class DonationFragment : Fragment(R.layout.fragment_donation) {

    companion object {
        private const val RC_CREDIT_CARD = 123
        private const val ARGS_CHARITY = "dev.iamwee.android.tamboon.ui.donation.ARGS_CHARITY"

        fun fromCharity(charity: CharityInfo) = DonationFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARGS_CHARITY, charity)
            }
        }
    }

    private lateinit var charity: CharityInfo

    private val viewModel by viewModels<DonationViewModel>()

    private val formatter by lazy {
        DecimalFormat("#,###,###,###,###")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        charity = arguments?.getParcelable(ARGS_CHARITY) ?: error("Charity is required.")

        textViewDonationTo.text = getString(R.string.label_donation_to, charity.name)

        Glide.with(this@DonationFragment)
            .load(charity.imageUrl)
            .fitCenter()
            .into(imageViewCharityThumbnail)

        editTextDonationAmount.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val amount = s.toString().toLongOrNull() ?: 0
                buttonDonation.isEnabled = amount >= 20
                buttonDonation.text = if (amount > 0) {
                    getString(R.string.button_donation, formatter.format(amount))
                } else {
                    getString(R.string.button_please_fill_donation_amount)
                }
            }
        })
        buttonDonation.setOnClickListener(::launchCreditCardActivity)

        viewModel.donateResult.observe(viewLifecycleOwner, Observer {
            buttonDonation.isEnabled = it !is Result.Loading
            when (it) {
                is Result.Success -> (activity as? Delegate)?.onDonateSucceeded(charity, editTextDonationAmount.text.toString().toLong())
                is Result.Error -> AlertDialog.Builder(requireContext())
                        .setMessage(it.throwable.errorMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_CREDIT_CARD && resultCode == Activity.RESULT_OK) {
            val token = data?.getParcelableExtra<Token>(CreditCardActivity.EXTRA_TOKEN_OBJECT) ?: return
            val amountSatang = editTextDonationAmount.text.toString().toLong().times(100)
            viewModel.donate(token.id, token.card.name, amountSatang)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun launchCreditCardActivity(view: View) {
        val intent = Intent(requireContext(), CreditCardActivity::class.java).apply {
            putExtra(CreditCardActivity.EXTRA_PKEY, BuildConfig.OMISE_PKEY)
        }
        startActivityForResult(intent, RC_CREDIT_CARD)
    }

    interface Delegate {
        fun onDonateSucceeded(charity: CharityInfo, amount: Long)
    }

}