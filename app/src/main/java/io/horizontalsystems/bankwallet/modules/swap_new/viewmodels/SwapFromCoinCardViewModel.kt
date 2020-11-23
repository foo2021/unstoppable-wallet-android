package io.horizontalsystems.bankwallet.modules.swap_new.viewmodels

import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.entities.Coin
import io.horizontalsystems.bankwallet.modules.swap.provider.StringProvider
import io.horizontalsystems.bankwallet.modules.swap.view.SwapItemFormatter
import io.horizontalsystems.bankwallet.modules.swap_new.SwapModule
import io.horizontalsystems.bankwallet.modules.swap_new.SwapModule.CoinBalanceItem
import io.horizontalsystems.bankwallet.modules.swap_new.SwapService
import io.horizontalsystems.bankwallet.modules.swap_new.SwapService.SwapError
import io.horizontalsystems.bankwallet.modules.swap_new.SwapTradeService
import io.horizontalsystems.bankwallet.modules.swap_new.providers.SwapCoinProvider
import io.reactivex.schedulers.Schedulers

class SwapFromCoinCardViewModel(
        service: SwapService,
        tradeService: SwapTradeService,
        private val coinProvider: SwapCoinProvider,
        formatter: SwapItemFormatter,
        stringProvider: StringProvider,
) : SwapCoinCardViewModel(service, tradeService, formatter, stringProvider) {

    init {
        subscribeToService()
    }

    override val title = stringProvider.string(R.string.Swap_FromAmountTitle)

    override val tokensForSelection: List<CoinBalanceItem>
        get() = coinProvider.coins(enabledCoins = true)

    override val amountType = SwapModule.AmountType.ExactFrom

    override val coin: Coin?
        get() = tradeService.coinFrom

    override fun handleErrors(errors: List<Throwable>) {
        val insufficientBalanceFrom = errors.firstOrNull { it is SwapError.InsufficientBalanceFrom }
        balanceErrorLiveData.postValue(insufficientBalanceFrom != null)
    }

    override fun subscribeToService() {
        super.subscribeToService()

        onUpdateCoin(tradeService.coinFrom)
        onUpdateBalance(service.balanceFrom)

        tradeService.coinFromObservable
                .subscribeOn(Schedulers.io())
                .subscribe {
                    onUpdateCoin(it.orElse(null))
                }
                .let { disposables.add(it) }
        service.balanceFromObservable
                .subscribeOn(Schedulers.io())
                .subscribe {
                    onUpdateBalance(it.orElse(null))
                }.let { disposables.add(it) }

    }

    override fun onCoinSelected(coinBalanceItem: CoinBalanceItem) {
        tradeService.updateCoinFrom(coinBalanceItem.coin)
    }

    override fun onAmountChanged(amount: String?) {
        TODO("not implemented")
    }
}
