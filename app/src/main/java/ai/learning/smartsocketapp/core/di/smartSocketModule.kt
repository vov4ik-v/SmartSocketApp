package ai.learning.smartsocketapp.core.di

import ai.learning.smartsocketapp.core.network.api.SmartSocketService
import ai.learning.smartsocketapp.core.network.api.SmartSocketServiceImpl
import ai.learning.smartsocketapp.core.network.repository.SmartSocketRepository
import ai.learning.smartsocketapp.core.network.repository.SmartSocketRepositoryImpl
import ai.learning.smartsocketapp.feature.home.HomeViewModel
import ai.learning.smartsocketapp.feature.statistic.StatisticViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val smartSocketModule = module {
    single<SmartSocketService> { SmartSocketServiceImpl(get()) }
    single<SmartSocketRepository> { SmartSocketRepositoryImpl(get()) }

    viewModel { HomeViewModel(get()) }
    viewModel { StatisticViewModel(get()) }
}