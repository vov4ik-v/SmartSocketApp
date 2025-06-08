package ai.learning.smartsocketapp.core.di

import ai.learning.smartsocketapp.core.network.api.TestService
import ai.learning.smartsocketapp.core.network.api.TestServiceImpl
import ai.learning.smartsocketapp.core.network.repository.TestRepository
import ai.learning.smartsocketapp.core.network.repository.TestRepositoryImpl
import ai.learning.smartsocketapp.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testModule = module {
    single<TestService> { TestServiceImpl(get())  }
    single<TestRepository> { TestRepositoryImpl(get()) }

}