package io.horizontalsystems.bankwallet.modules.send

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.horizontalsystems.bankwallet.SingleLiveEvent
import io.horizontalsystems.bankwallet.modules.transactions.Coin

class SendViewModel : ViewModel(), SendModule.IRouter, SendModule.IView {

    lateinit var delegate: SendModule.IViewDelegate

    val hintInfoLiveData = MutableLiveData<SendModule.HintInfo>()
    val sendButtonEnabledLiveData = MutableLiveData<Boolean>()
    val coinLiveData = MutableLiveData<Coin>()
    val amountInfoLiveData = MutableLiveData<SendModule.AmountInfo>()
    val switchButtonEnabledLiveData = MutableLiveData<Boolean>()
    val addressInfoLiveData = MutableLiveData<SendModule.AddressInfo>()
    val dismissWithSuccessLiveEvent = SingleLiveEvent<Unit>()
    val primaryFeeAmountInfoLiveData = MutableLiveData<SendModule.AmountInfo>()
    val secondaryFeeAmountInfoLiveData = MutableLiveData<SendModule.AmountInfo>()
    val errorLiveData = MutableLiveData<Throwable>()

    fun init(coin: String) {
        SendModule.init(this, this, coin)
        delegate.onViewDidLoad()
    }

    override fun setCoin(coin: Coin) {
        coinLiveData.value = coin
    }

    override fun setAmountInfo(amountInfo: SendModule.AmountInfo?) {
        amountInfoLiveData.value = amountInfo
    }

    override fun setSwitchButtonEnabled(enabled: Boolean) {
        switchButtonEnabledLiveData.value = enabled
    }

    override fun setHintInfo(amountInfo: SendModule.HintInfo?) {
        hintInfoLiveData.value = amountInfo
    }

    override fun setAddressInfo(addressInfo: SendModule.AddressInfo?) {
        addressInfoLiveData.value = addressInfo
    }

    override fun setPrimaryFeeInfo(primaryFeeInfo: SendModule.AmountInfo?) {
        primaryFeeAmountInfoLiveData.value = primaryFeeInfo
    }

    override fun setSecondaryFeeInfo(secondaryFeeInfo: SendModule.AmountInfo?) {
        secondaryFeeAmountInfoLiveData.value = secondaryFeeInfo
    }

    override fun setSendButtonEnabled(sendButtonEnabled: Boolean) {
        sendButtonEnabledLiveData.value = sendButtonEnabled
    }

    override fun showError(error: Throwable) {
        errorLiveData.value = error
    }

    override fun dismissWithSuccess() {
        dismissWithSuccessLiveEvent.call()
    }

}