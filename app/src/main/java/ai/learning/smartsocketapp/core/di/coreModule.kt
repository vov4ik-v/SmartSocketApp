package ai.learning.smartsocketapp.core.di

import ai.learning.smartsocketapp.core.network.KtorClientBuilder
import org.koin.dsl.module

val coreModule = module {
    single { KtorClientBuilder.build() }
}