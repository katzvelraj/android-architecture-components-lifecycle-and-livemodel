package com.tinmegali.daggerwithkotlin.dagger

import com.tinmegali.daggerwithkotlin.App
import com.tinmegali.daggerwithkotlin.dagger.activities.ActivitiesModule
import com.tinmegali.daggerwithkotlin.dagger.fragments.FragmentsModule
import com.tinmegali.daggerwithkotlin.dagger.viewModels.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ViewModelModule::class
))
interface AppComponent {
    fun inject(app: App)
}